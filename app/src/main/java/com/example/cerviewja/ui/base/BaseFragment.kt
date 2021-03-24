package com.example.cerviewja.ui.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.FragmentManager
import com.example.cerviewja.BuildConfig
import com.example.cerviewja.R
import com.example.cerviewja.ui.main.MainActivity
import com.example.cerviewja.utils.Constants
import com.example.cerviewja.utils.CustomDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

abstract class BaseFragment : Fragment() {

    abstract val layout: Int

    private lateinit var loadingView: View
    private lateinit var flavourView: View
    lateinit var mAuth: FirebaseAuth
    lateinit var mFirestore: FirebaseFirestore
    lateinit var usersBeersRefs: DocumentReference
    lateinit var mainActivity: MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mAuth = FirebaseAuth.getInstance()
        mFirestore = FirebaseFirestore.getInstance()
        mainActivity = activity as MainActivity
        usersBeersRefs = mFirestore.collection(Constants.BEERS_USERS).document(mAuth.currentUser!!.uid)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val screenRootView = FrameLayout(requireContext())

        val screenView = inflater.inflate(layout, container, false)

        loadingView = inflater.inflate(R.layout.loading_progress, container, false)

        val flavourScreen = inflater.inflate(R.layout.include_flavour,
            container, false)
        flavourView = flavourScreen.findViewById(R.id.flavourScreen)

        configureEnvironment(
            flavourView,
            flavourScreen.findViewById(R.id.tvEnvironment) as TextView
        )

        mAuth.currentUser?.reload()

        screenRootView.addView(screenView)
        screenRootView.addView(loadingView)
        screenRootView.addView(flavourView)

        return screenRootView
    }

    private fun configureEnvironment(container: View, tvEnvironment: TextView) {
        when (BuildConfig.FLAVOR) {
            "dev" -> {
                container.visibility = View.VISIBLE
                tvEnvironment.text = "dev"
            }
            "hom" -> {
                container.visibility = View.VISIBLE
                tvEnvironment.text = "hom"
            }
            "prod" -> {
                container.visibility = View.GONE
                tvEnvironment.text = ""
            }
        }
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

    fun replaceFragment(fragment: Fragment) {
        mainActivity.replaceFragment(fragment)
    }

    private fun registerBackPressedAction() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity?.finish()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
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