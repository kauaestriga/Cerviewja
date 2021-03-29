package com.example.cerviewja.ui.main

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.cerviewja.R
import com.example.cerviewja.ui.about.AboutFragment
import com.example.cerviewja.ui.aroundworld.AroundWorldFragment
import com.example.cerviewja.ui.base.BaseFragment
import com.example.cerviewja.ui.beerlist.BeerListFragment
import com.example.cerviewja.ui.login.LoginFragment
import com.example.cerviewja.ui.profile.ProfileFragment
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        BaseFragment.addFragment(supportFragmentManager, LoginFragment())
        setListeners()
    }

    private fun setListeners() {
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.menu_beers-> {
                    replaceFragment(BeerListFragment())
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.menu_profile-> {
                    replaceFragment(ProfileFragment())
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.menu_around_world-> {
                    replaceFragment(AroundWorldFragment())
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.menu_about-> {
                    replaceFragment(AboutFragment())
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false

        }
    }

    fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.mainFragment, fragment, null)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun showToolbarNavigationBar() {
        bottomNavigationView.visibility = View.VISIBLE
    }

    fun hideToolbarNavigationBar() {
        bottomNavigationView.visibility = View.GONE
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }
}