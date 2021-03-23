package com.example.cerviewja.ui.beerlist

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.example.cerviewja.R
import com.example.cerviewja.domain.entity.Description
import com.example.cerviewja.extensions.getDouble
import com.example.cerviewja.extensions.getString
import com.example.cerviewja.ui.base.BaseFragment
import com.example.cerviewja.utils.Constants
import com.google.firebase.firestore.FieldValue


class BeerFragmentAdd(
    var save: Boolean
) : BaseFragment() {

    override val layout: Int = R.layout.fragment_add_beer

    lateinit var edtName: EditText
    lateinit var edtBrewery: EditText
    lateinit var edtStyle: EditText
    lateinit var edtSource: EditText
    lateinit var edtPrice: EditText
    lateinit var edtAlcoholContent: EditText
    lateinit var btnSaveEdit: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpView(view)
    }

    fun setUpView(view: View) {
        edtName = view.findViewById(R.id.edt_name_beerfrag)
        edtBrewery = view.findViewById(R.id.edt_brewery_beerfrag)
        edtStyle = view.findViewById(R.id.edt_style_beerfrag)
        edtSource = view.findViewById(R.id.edt_source_beerfrag)
        edtPrice = view.findViewById(R.id.edt_price_beerfrag)
        edtAlcoholContent = view.findViewById(R.id.edt_alcohol_content_beerfrag)
        btnSaveEdit = view.findViewById(R.id.btn_save_edit_beerfrag)

        if (save)
            btnSaveEdit.text = getString(R.string.save)
        else
            btnSaveEdit.text = getString(R.string.edit)

        setListeners()
    }

    fun setListeners() {
        btnSaveEdit.setOnClickListener {
            validateFields()
        }
    }

    fun validateFields() {
        var isError = false

        val description = Description()

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
            saveBeer(description)
        }
    }

    private fun saveBeer(description: Description) {
        showLoading()
        mAuth.currentUser?.reload()

        val userDocument = mFirestore.collection(Constants.BEERS_USERS)
            .document(mAuth.currentUser!!.uid)

        userDocument
            .collection(description.nome.toString())
            .document(Constants.DESCRIPTION)
            .set(description)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    showToastMessage(getString(R.string.save_success))
                } else
                    showErrorMessage(task.exception?.message.toString())
            }

        userDocument
            .update(Constants.COLLECTION, FieldValue.arrayUnion(description.nome))
            .addOnCompleteListener { task ->
                if (task.isSuccessful)
                    replaceFragment(BeerListFragment(), getString(R.string.beers))
            }

        hideLoading()
    }
}