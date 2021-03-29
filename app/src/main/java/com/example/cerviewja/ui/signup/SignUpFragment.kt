package com.example.cerviewja.ui.signup

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.example.cerviewja.R
import com.example.cerviewja.domain.entity.User
import com.example.cerviewja.extensions.getString
import com.example.cerviewja.ui.base.BaseFragment
import com.example.cerviewja.ui.beerlist.BeerListFragment
import com.example.cerviewja.utils.Constants
import com.example.cerviewjalib.ValidateUtils

class SignUpFragment : BaseFragment() {

    override val layout = R.layout.fragment_sign_up

    private lateinit var edtName: EditText
    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var edtConfirmPassword: EditText
    private lateinit var btnConfirm: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpView(view)
    }

    private fun setUpView(view: View) {
        hideToolbarNavigationBar()

        edtName = view.findViewById(R.id.edt_name_signup)
        edtEmail = view.findViewById(R.id.edt_email_signup)
        edtPassword = view.findViewById(R.id.edt_password_signup)
        edtConfirmPassword = view.findViewById(R.id.edt_confirm_password_signup)
        btnConfirm = view.findViewById(R.id.btn_signup_signup)
        setUpListeners()
    }

    private fun setUpListeners() {
        btnConfirm.setOnClickListener { validadeFields() }
    }

    private fun validadeFields() {
        var isError = false

        if (edtName.getString().isEmpty()) {
            edtName.error = getString(R.string.name_error)
            isError = true
        }

        if (!ValidateUtils.isValidEmail(edtEmail.getString())) {
            edtEmail.error = getString(R.string.email_error)
            isError = true
        }

        if (edtPassword.getString().isEmpty()) {
            edtPassword.error = getString(R.string.password_error)
            isError = true
        }

        if (edtConfirmPassword.getString().isEmpty()) {
            edtConfirmPassword.error = getString(R.string.password_error)
            isError = true
        }

        if (!ValidateUtils.isValidPassword(edtPassword.getString(), edtConfirmPassword.getString())) {
            edtConfirmPassword.error = getString(R.string.confirm_password_error)
            isError = true
        }

        if (!isError)
            doSignUp()
    }

    private fun doSignUp() {
        showLoading()

        mAuth.createUserWithEmailAndPassword(edtEmail.getString(), edtConfirmPassword.getString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    createUser(mAuth.currentUser?.uid)
                } else {
                    showErrorMessage(task.exception?.message.toString())
                    hideLoading()
                }
            }
            .addOnFailureListener { e ->
                showErrorMessage(e.message.toString())
            }
    }

    private fun createUser(userId: String?) {
        val user = User(userId, edtName.getString(), edtEmail.getString())

        if (userId != null) {
            mFirestore.collection(Constants.USERS)
                .document(userId)
                .set(user)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        replaceFragment(BeerListFragment())
                    } else {
                        deleteUserAuth()
                        showErrorMessage(R.string.impossible_account_create)
                    }
                }
                .addOnFailureListener { e ->
                    deleteUserAuth()
                    showErrorMessage(e.message.toString())
                }
        }
    }

    private fun deleteUserAuth(){
        val user = mAuth.currentUser!!

        user.delete()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("CREATE", "User account deleted.")
                }
            }
    }
}