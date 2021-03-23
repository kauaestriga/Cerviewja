package com.example.cerviewja.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.cerviewja.R
import com.example.cerviewja.ui.base.BaseFragment
import com.example.cerviewja.ui.login.LoginFragment
import com.example.cerviewja.utils.FragmentFlow
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        FragmentFlow(supportFragmentManager).replaceFragment(LoginFragment())
        BaseFragment.addFragment(supportFragmentManager, LoginFragment())
    }

    fun replaceFragment(fragment: Fragment, title: String) {
        if (title.isNotEmpty())
            toolbar_title.text = title
        val transaction = supportFragmentManager.beginTransaction()
        supportFragmentManager.popBackStack()
        transaction.replace(R.id.mainFragment, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun showToolbarNavigationBar() {
        bottomNavigationView.visibility = View.VISIBLE
        toolbar.visibility = View.VISIBLE
    }

    fun hideToolbarNavigationBar() {
        bottomNavigationView.visibility = View.GONE
        toolbar.visibility = View.GONE
    }
}