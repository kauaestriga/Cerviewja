package com.example.cerviewja.ui.beeradd

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.cerviewja.R
import com.example.cerviewja.domain.entity.Description
import com.example.cerviewja.extensions.getDouble
import com.example.cerviewja.extensions.getString
import com.example.cerviewja.ui.base.BaseFragment
import com.example.cerviewja.utils.Constants


class BeerFragmentAdd(
    private var description: Description = Description()
) : BaseFragment() {

    override val layout: Int = R.layout.fragment_add_beer

    lateinit var edtName: EditText
    lateinit var edtBrewery: EditText
    lateinit var edtStyle: EditText
    lateinit var edtSource: EditText
    lateinit var edtPrice: EditText
    lateinit var edtAlcoholContent: EditText
    lateinit var btnSaveEdit: Button
    lateinit var tvCancel: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpView(view)
    }

    private fun setUpView(view: View) {
        edtName = view.findViewById(R.id.edt_name_beerfrag)
        edtBrewery = view.findViewById(R.id.edt_brewery_beerfrag)
        edtStyle = view.findViewById(R.id.edt_style_beerfrag)
        edtSource = view.findViewById(R.id.edt_source_beerfrag)
        edtPrice = view.findViewById(R.id.edt_price_beerfrag)
        edtAlcoholContent = view.findViewById(R.id.edt_alcohol_content_beerfrag)
        btnSaveEdit = view.findViewById(R.id.btn_save_edit_beerfrag)
        tvCancel = view.findViewById(R.id.tv_link_cancel_beerfrag)

        if (description.id == null)
            btnSaveEdit.text = getString(R.string.save)
        else {
            btnSaveEdit.text = getString(R.string.edit)
            loadFields()
        }

        setListeners()
    }

    private fun setListeners() {
        btnSaveEdit.setOnClickListener {
            validateFields()
        }

        tvCancel.setOnClickListener {
            backPress()
        }
    }

    private fun validateFields() {
        var isError = false

        if (edtName.getString().isEmpty()){
            edtName.error = getString(R.string.empty_error)
            isError = true
        } else
            description.nome = edtName.getString()

        if (edtBrewery.getString().isEmpty()){
            edtBrewery.error = getString(R.string.empty_error)
            isError = true
        } else
            description.cervejaria = edtBrewery.getString()

        if (edtStyle.getString().isEmpty()){
            edtStyle.error = getString(R.string.empty_error)
            isError = true
        } else
            description.estilo = edtStyle.getString()

        if (edtSource.getString().isEmpty()){
            edtSource.error = getString(R.string.empty_error)
            isError = true
        } else
            description.origem = edtSource.getString()

        if (edtPrice.getString().isEmpty()){
            edtPrice.error = getString(R.string.empty_error)
            isError = true
        } else
            description.preco = edtPrice.getDouble()

        if (edtAlcoholContent.getString().isEmpty()){
            edtAlcoholContent.error = getString(R.string.empty_error)
            isError = true
        } else
            description.teorAlcoolico = edtAlcoholContent.getDouble()


        if (!isError) {
            if (description.id == null)
                saveBeer()
            else
                updateBeer()
        }
    }

    private fun loadFields() {
        edtName.setText(description.nome)
        edtBrewery.setText(description.cervejaria)
        edtStyle.setText(description.estilo)
        edtSource.setText(description.origem)
        edtPrice.setText(description.preco.toString())
        edtAlcoholContent.setText(description.teorAlcoolico.toString())
    }

    private fun saveBeer() {
        try {
            showLoading()
            mAuth.currentUser?.reload()

            val beerRef = mFirestore
                .collection(Constants.BEERS_USERS)
                .document(mAuth.currentUser!!.uid)
                .collection(Constants.DESCRIPTION)
                .document()

            description.id = beerRef.id

            beerRef
                .set(description)
                .addOnCompleteListener { result ->
                    if (result.isSuccessful) {
                        showToastMessage(getString(R.string.save_success))
                        clearFields()
                    } else {
                        showErrorMessage(result.exception?.message)
                    }
                    hideLoading()
                }
        } catch (e: Exception) {
            e.printStackTrace()
            hideLoading()
        }
    }

    private fun updateBeer() {
        try {
            showLoading()

            mAuth.currentUser?.reload()

            mFirestore
                .collection(Constants.BEERS_USERS)
                .document(mAuth.currentUser!!.uid)
                .collection(Constants.DESCRIPTION)
                .document(description.id!!)
                .update(description.toMap())
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        showToastMessage(getString(R.string.save_success))
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

    private fun clearFields() {
        edtName.setText("")
        edtBrewery.setText("")
        edtStyle.setText("")
        edtSource.setText("")
        edtPrice.setText("")
        edtAlcoholContent.setText("")
    }

//    private fun saveBeer(description: Description) {
//        showLoading()
//        mAuth.currentUser?.reload()
//
//        val userDocument = mFirestore.collection(Constants.BEERS_USERS)
//            .document(mAuth.currentUser!!.uid)
//
//        if (oldDescription != null) {
//            userDocument
//                .collection(description.nome.toString())
//                .document(Constants.DESCRIPTION)
//                .delete()
//        }
//
//        userDocument
//            .collection(description.nome.toString())
//            .document(Constants.DESCRIPTION)
//            .set(description)
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    showToastMessage(getString(R.string.save_success))
//                } else
//                    showErrorMessage(task.exception?.message.toString())
//            }
//
//        userDocument
//            .update(Constants.COLLECTION, FieldValue.arrayUnion(description.id))
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful)
//                    replaceFragment(BeerListFragment())
//            }
//
//        hideLoading()
//    }
//
//    private fun updateBeer(description: Description) {
//        mAuth.currentUser?.reload()
//
//        usersBeersRefs
//            .collection(description.id.toString())
//            .document(Constants.DESCRIPTION)
//            .delete()
//            .addOnSuccessListener {
//                saveBeer(description)
//            }
//            .addOnFailureListener { e ->
//                showErrorMessage(e.message.toString())
//            }
//    }
}