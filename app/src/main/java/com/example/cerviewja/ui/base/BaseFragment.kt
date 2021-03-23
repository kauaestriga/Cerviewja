package com.example.cerviewja.ui.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.example.cerviewja.BuildConfig
import com.example.cerviewja.R
import com.example.cerviewja.ui.main.MainActivity
import com.example.cerviewja.utils.CustomDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*

abstract class BaseFragment : Fragment() {

    abstract val layout: Int

    private lateinit var loadingView: View
    lateinit var mAuth: FirebaseAuth
    lateinit var mFirestore: FirebaseFirestore
    lateinit var mainActivity: MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mAuth = FirebaseAuth.getInstance()
        mFirestore = FirebaseFirestore.getInstance()
        mainActivity = activity as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val screenRootView = FrameLayout(requireContext())

        val screenView = inflater.inflate(layout, container, false)

        loadingView = inflater.inflate(R.layout.loading_progress, container, false)

        mAuth.currentUser?.reload()

        screenRootView.addView(screenView)
        screenRootView.addView(loadingView)

        return screenRootView
    }

    fun showLoading() {
        loadingView.visibility = View.VISIBLE
    }

    fun hideLoading() {
        loadingView.visibility = View.GONE
    }

    fun showErrorMessage(message: String?) {
        CustomDialog(requireContext()).showError(message)
    }

    fun showHappyMessage(message: String?) {
        CustomDialog(requireContext()).showMessage(message)
    }

    fun showErrorMessage(message: Int?) {
        if (message != null) {
            CustomDialog(requireContext()).showError(message)
        }
    }

    fun showHappyMessage(message: Int?) {
        if (message != null) {
            CustomDialog(requireContext()).showMessage(message)
        }
    }

    fun showToastMessage(message: String?) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    fun showToolbarNavigationBar() {
        mainActivity.showToolbarNavigationBar()
    }

    fun hideToolbarNavigationBar() {
        mainActivity.hideToolbarNavigationBar()
    }

    fun replaceFragment(fragment: Fragment, title: String = "") {
        mainActivity.replaceFragment(fragment, title)
    }

    fun replaceFragment(fragment: Fragment, title: Int) {
        mainActivity.replaceFragment(fragment, getString(title))
    }

    fun backPressed() {
        childFragmentManager.popBackStack()
    }

    companion object {
        fun addFragment(manager: FragmentManager, fragment: Fragment) {
            val transaction = manager.beginTransaction()
            transaction.replace(R.id.mainFragment, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }
}