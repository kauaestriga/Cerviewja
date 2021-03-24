package com.example.cerviewja.ui.login

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import com.example.cerviewja.R
import com.example.cerviewja.data.local.UserSharedPreferences
import com.example.cerviewja.extensions.getString
import com.example.cerviewja.ui.base.BaseFragment
import com.example.cerviewja.ui.beerlist.BeerListFragment
import com.example.cerviewja.ui.signup.SignUpFragment
import com.example.cerviewja.utils.Constants
import com.example.cerviewja.utils.SystemUtils.Companion.hideKeyboard
import com.example.cerviewja.utils.ValidateUtils

class LoginFragment : BaseFragment() {

    override val layout = R.layout.fragment_login

    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var tvForgotPass: TextView
    private lateinit var tvSignUp: TextView
    private lateinit var cbStayConnect: CheckBox
    private lateinit var btnSignin: Button

    private lateinit var userSharedPreferences: UserSharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userSharedPreferences = UserSharedPreferences(requireContext())
        if (isLogged()) {
            replaceFragment(BeerListFragment())
        }

        setUpView(view)
        hideToolbarNavigationBar()
    }

    private fun setUpView(view: View) {
        edtEmail = view.findViewById(R.id.edt_email_login)
        edtPassword = view.findViewById(R.id.edt_password_login)
        tvForgotPass = view.findViewById(R.id.tv_reset_password_login)
        tvSignUp = view.findViewById(R.id.tv_new_account_login)
        cbStayConnect = view.findViewById(R.id.cb_stay_connect)
        btnSignin = view.findViewById(R.id.btn_sign_in_login)
        setUpListeners()
    }

    private fun setUpListeners() {
        tvForgotPass.setOnClickListener {
            recoveryPassword()
        }
        btnSignin.setOnClickListener {
            validateFields()// Force a crash
        }
        edtPassword.setOnEditorActionListener { view, actionId, _ -> hideKeyboard(view, actionId) }
        tvSignUp.setOnClickListener {
            replaceFragment(SignUpFragment())
        }
    }

    private fun validateFields() {
        var isError = false

        if (!ValidateUtils.isValidEmail(edtEmail.getString())) {
            edtEmail.error = getString(R.string.email_error)
            isError = true
        }

        if (edtPassword.getString().isEmpty()) {
            edtPassword.error = getString(R.string.insert_pass)
            isError = true
        }

        if (!isError)
            doLogin()
    }

    private fun hideKeyboard(view: View, actionId: Int): Boolean {
        var handled = false
        if (actionId == EditorInfo.IME_ACTION_SEND) {
            view.hideKeyboard()
            handled = true
        }
        return handled
    }

    private fun doLogin() {
        showLoading()

        mAuth.signInWithEmailAndPassword(edtEmail.getString(), edtPassword.getString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (cbStayConnect.isChecked)
                        userSharedPreferences.saveValue(Constants.UID, mAuth.currentUser?.uid)
                    replaceFragment(BeerListFragment())
                } else {
                    showErrorMessage(task.exception?.message.toString())
                }

                hideLoading()
            }
    }

    private fun recoveryPassword() {
        if (!ValidateUtils.isValidEmail(edtEmail.getString())) {
            edtEmail.error = getString(R.string.email_error)
            return
        }

        showLoading()

        mAuth.sendPasswordResetEmail(edtEmail.getString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    showHappyMessage(R.string.recovery_email_text)
                } else {
                    showErrorMessage(R.string.recovery_email_text_error)
                }
                hideLoading()
            }
    }

    private fun isLogged(): Boolean {
        return userSharedPreferences.isLogged(Constants.UID)
    }
}