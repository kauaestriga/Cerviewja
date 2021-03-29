package com.example.cerviewja.ui.profile

import android.os.Bundle
import android.provider.SyncStateContract
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.example.cerviewja.R
import com.example.cerviewja.data.local.UserSharedPreferences
import com.example.cerviewja.domain.entity.User
import com.example.cerviewja.ui.base.BaseFragment
import com.example.cerviewja.utils.Constants
import java.lang.Exception

class ProfileFragment : BaseFragment() {

    override val layout: Int = R.layout.fragment_profile

    private lateinit var ivAvatar: ImageView
    private lateinit var tvEditPhoto: TextView
    private lateinit var edtName: EditText
    private lateinit var edtEmail: EditText
    private lateinit var btnSave: Button
    private lateinit var tvLogoff: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpView(view)
    }

    private fun setUpView(view: View) {
        ivAvatar = view.findViewById(R.id.iv_avatar)
        tvEditPhoto = view.findViewById(R.id.tv_edit_avatar)
        edtName = view.findViewById(R.id.edt_name_profile)
        edtEmail = view.findViewById(R.id.edt_email_profile)
        btnSave = view.findViewById(R.id.btn_save_profile)
        tvLogoff = view.findViewById(R.id.tv_logoff_profile)
        setUpListeners()

        loadFields()
    }

    private fun setUpListeners() {
        tvEditPhoto.setOnClickListener {
            showToastMessage("Indisponível no momento")
        }

        btnSave.setOnClickListener {
            showToastMessage("Indisponível no momento")
        }

        tvLogoff.setOnClickListener {
            mAuth.signOut()
            UserSharedPreferences(requireContext()).cleanValue(Constants.UID)
            logoff()
        }
    }

    private fun loadFields() {
        try {
            showLoading()

            mAuth.currentUser?.reload()

            mAuth.currentUser?.uid?.let {
                mFirestore.collection(Constants.USERS)
                    .document(it)
                    .get()
                    .addOnSuccessListener { document ->
                        val user = document.toObject(User::class.java)
                        edtName.setText(user?.name)
                        edtEmail.setText(user?.email)
                        hideLoading()
                    }
                    .addOnFailureListener { e ->
                        showErrorMessage(e.message)
                        hideLoading()
                    }
            }
        } catch (e: Exception) {
            showErrorMessage(getString(R.string.stranger_error))
            e.printStackTrace()
            hideLoading()
        }
    }
}