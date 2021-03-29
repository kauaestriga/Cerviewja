package com.example.cerviewja.ui.beerlist

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.cerviewja.R
import com.example.cerviewja.domain.entity.Description
import com.example.cerviewja.ui.base.BaseFragment
import com.example.cerviewja.ui.beeradd.BeerFragmentAdd
import com.example.cerviewja.utils.Constants

class BeerListFragment : BaseFragment(), MyClickListener {

    override val layout: Int = R.layout.fragment_beer_list

    lateinit var rvBeer: RecyclerView
    lateinit var btnAdd: Button
    lateinit var tvEmptyList: TextView

    //    lateinit var beersDocument: DocumentReference
    lateinit var descriptions: ArrayList<Description>
    lateinit var beerAdapter: BeerAdapter

    var REQUEST_PHONE_CALL = 1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpView(view)
        hideItens()
        showToolbarNavigationBar()
    }

    override fun onResume() {
        super.onResume()
//        loadCollections()
//        loadBeers()
        loadBeerListener()
    }

    private fun setUpView(view: View) {
        rvBeer = view.findViewById(R.id.rv_beer_list)
        btnAdd = view.findViewById(R.id.btn_add_beer)
        tvEmptyList = view.findViewById(R.id.tv_empty_list)

        setListeners()
    }

    private fun setListeners() {
        btnAdd.setOnClickListener {
            replaceFragment(BeerFragmentAdd())
        }
    }

    private fun loadBeers() {
        try {
            showLoading()

            val uid = mAuth.currentUser?.uid

            if (uid != null) {

                mFirestore.collection(Constants.BEERS_USERS)
                    .document(uid)
                    .collection(Constants.DESCRIPTION)
                    .get()
                    .addOnSuccessListener { documentSnapshot ->
                        descriptions =
                            documentSnapshot.toObjects(Description::class.java) as ArrayList<Description>
                        loadAdapter(descriptions)
                        hideLoading()
                    }
                    .addOnFailureListener { exception ->
                        showErrorMessage(exception.message)
                        hideLoading()
                    }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            hideLoading()
        }
    }

    fun loadAdapter(descriptions: ArrayList<Description>) {
        beerAdapter = BeerAdapter(descriptions, this)
        rvBeer.adapter = beerAdapter

        showItens()
    }

    fun loadBeerListener() {
        try {
            val uid = mAuth.currentUser?.uid

            if (uid != null) {

                mFirestore.collection(Constants.BEERS_USERS)
                    .document(uid)
                    .collection(Constants.DESCRIPTION)
                    .addSnapshotListener { value, e ->
                        if (e != null) {
                            Log.w("Listener", "Listen failed.", e)
                            return@addSnapshotListener
                        }

                        descriptions =
                            value!!.toObjects(Description::class.java) as ArrayList<Description>
                        loadAdapter(descriptions)
                    }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun showItens() {
        rvBeer.visibility = View.VISIBLE
        tvEmptyList.visibility = View.GONE
    }

    private fun hideItens() {
        rvBeer.visibility = View.GONE
        tvEmptyList.visibility = View.VISIBLE
    }

    override fun onClick(description: Description) {
        replaceFragment(BeerFragmentAdd(description))
    }

    override fun onLongClick(description: Description) {
        showDialog(description)
    }

    override fun onShareClick(description: Description) {
        shareItem(description)
    }

    override fun onCallClick() {
        showAlertCall()
    }

    private fun shareItem(description: Description) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            val packageName = activity?.packageName
            val text = getString(
                R.string.sharing_text,
                description.nome,
                "https://play.google.com/store/apps/details?id=$packageName"
            )
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    private fun showAlertCall() {
        val builder = AlertDialog.Builder(requireContext())

        with(builder) {
            setMessage(getString(R.string.call_atention))
            setPositiveButton(R.string.yes) { _, _ ->
                makeCall()
            }
            setNegativeButton(R.string.no) { dialog, _ ->
                dialog.dismiss()
            }
            show()
        }
    }

    private fun showDialog(description: Description) {
        val builder = AlertDialog.Builder(requireContext())

        with(builder) {
            setMessage(getString(R.string.delete_message, description.nome))
            setPositiveButton(R.string.yes) { _, _ ->
                deleteBeer(description)
            }
            setNegativeButton(R.string.no) { dialog, _ ->
                dialog.dismiss()
            }
            show()
        }
    }

    private fun deleteBeer(description: Description) {
        showLoading()
        try {

            beersRefs
                .document(description.id!!)
                .delete()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        showToastMessage(getString(R.string.delete_message_success))
                    } else {
                        showErrorMessage(task.exception?.message)
                    }
                    hideLoading()
                }
        } catch (e: Exception) {
            e.printStackTrace()
            hideLoading()
        }
    }

    private fun makeCall() {
        val numberText = "5511997863208"
        val intent = Intent(Intent.ACTION_CALL)
        intent.data = Uri.parse("tel:$numberText")

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(mainActivity,
                    Manifest.permission.CALL_PHONE)) {
                Toast.makeText(
                    activity,
                    "Nos autorize a fazer ligação",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                ActivityCompat.requestPermissions(requireActivity(),
                    arrayOf(Manifest.permission.CALL_PHONE),
                    REQUEST_PHONE_CALL)
            }

            return
        }
        startActivity(intent)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_PHONE_CALL) {
            makeCall()
        }
    }
}