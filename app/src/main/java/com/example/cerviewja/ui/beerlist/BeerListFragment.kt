package com.example.cerviewja.ui.beerlist

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.cerviewja.R
import com.example.cerviewja.domain.entity.Description
import com.example.cerviewja.ui.base.BaseFragment
import com.example.cerviewja.utils.Constants
import com.google.firebase.firestore.DocumentReference

class BeerListFragment : BaseFragment() {

    override val layout: Int = R.layout.fragment_beer_list

    lateinit var rvBeer: RecyclerView
    lateinit var btnAdd: Button
    lateinit var beersDocument: DocumentReference
    lateinit var descriptions: ArrayList<Description>
    lateinit var beerAdapter: BeerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpView(view)
        showToolbarNavigationBar()
    }

    override fun onResume() {
        super.onResume()
        loadCollections()
    }

    private fun setUpView(view: View) {
        rvBeer = view.findViewById(R.id.rv_beer_list)
        btnAdd = view.findViewById(R.id.btn_add_beer)

        descriptions = ArrayList()
        beerAdapter = BeerAdapter(descriptions)
        rvBeer.adapter = beerAdapter

        setListeners()
    }

    private fun setListeners() {
        btnAdd.setOnClickListener {
            replaceFragment(BeerFragmentAdd(true), R.string.the_beer)
        }
    }

    private fun loadCollections() {
        showLoading()

        val uid = mAuth.currentUser?.uid

        if (uid != null) {
            beersDocument = mFirestore.collection(Constants.BEERS_USERS).document(uid)

            beersDocument
                .get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        loadBeers(document.data?.get(Constants.COLLECTION) as ArrayList<String>)
                    }
                }
                .addOnFailureListener { e ->
                    showErrorMessage(e.message.toString())
                    hideLoading()
                }
        }
    }

    private fun loadBeers(listCollection: ArrayList<String>) {
        listCollection.forEach {
            beersDocument
                .collection(it)
                .document(Constants.DESCRIPTION)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    val description = documentSnapshot.toObject(Description::class.java)
                    if (description != null) {
                        beerAdapter.addBeer(description)
                    }
                }
                .addOnFailureListener { e ->
                    showErrorMessage(e.message.toString())
                }
            hideLoading()
        }
    }
}