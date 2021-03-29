package com.example.cerviewja.ui.beerlist

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
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
                        descriptions = documentSnapshot.toObjects(Description::class.java) as ArrayList<Description>
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

                        descriptions = value!!.toObjects(Description::class.java) as ArrayList<Description>
                        loadAdapter(descriptions)
                    }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

//    private fun loadCollections() {
//        showLoading()
//
//        val uid = mAuth.currentUser?.uid
//
//        if (uid != null) {
//            try {
//                beersDocument = mFirestore.collection(Constants.BEERS_USERS).document(uid)
//
//                beersDocument
//                    .get()
//                    .addOnSuccessListener { document ->
//                        if (document != null) {
//                            if (document.data?.count()!! > 0) {
//                                loadBeers(document.data?.get(Constants.COLLECTION) as ArrayList<String>)
//                                showItens()
//                            } else {
//                                hideLoading()
//                            }
//                        }
//                    }
//                    .addOnFailureListener { e ->
//                        showErrorMessage(e.message.toString())
//                        hideLoading()
//                    }
//            } catch (e: Exception) {
//                e.printStackTrace()
//                hideLoading()
//            }
//        }
//    }
//
//    private fun loadBeers(listCollection: ArrayList<String>) {
//        listCollection.forEach {
//            beersDocument
//                .collection(it)
//                .document(Constants.DESCRIPTION)
//                .get()
//                .addOnSuccessListener { documentSnapshot ->
//                    val description = documentSnapshot.toObject(Description::class.java)
//                    if (description != null) {
//                        beerAdapter.addBeer(description)
//                    }
//                }
//                .addOnFailureListener { e ->
//                    showErrorMessage(e.message.toString())
//                }
//            hideLoading()
//        }
//    }

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

    fun showDialog(description: Description) {
        val builder = AlertDialog.Builder(requireContext())

        with (builder) {
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

    fun deleteBeer(description: Description) {
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

//    fun deleteBeer(description: Description) {
//        usersBeersRefs
//            .update(Constants.COLLECTION, FieldValue.arrayRemove(description.nome))
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    showToastMessage(getString(R.string.delete_message_success))
//                    beerAdapter.removeBeer(description)
//                } else
//                    showErrorMessage(task.exception?.message)
//            }
//    }
}