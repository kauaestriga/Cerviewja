package com.example.cerviewja.ui.about

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cerviewja.BuildConfig
import com.example.cerviewja.R
import com.example.cerviewja.domain.entity.Developer
import com.example.cerviewja.ui.base.BaseFragment
import com.example.cerviewja.utils.Constants

class AboutFragment : BaseFragment() {

    override val layout: Int = R.layout.fragment_about

    lateinit var rvDevs: RecyclerView
    lateinit var tvVersion: TextView

    lateinit var developers: ArrayList<Developer>
    lateinit var aboutAdapter: AboutAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpView(view)
        loadDevs()
    }

    private fun setUpView(view: View) {
        rvDevs = view.findViewById(R.id.rv_devs_list)
        tvVersion = view.findViewById(R.id.tv_version_number)

        developers = ArrayList()
        tvVersion.text = getString(R.string.version, BuildConfig.VERSION_NAME)
    }

    private fun loadDevs() {
//        developers.add(Developer("Kauã Estriga", "338984"))
//        developers.add(Developer("Bárbara Perretti", "337512"))
//        developers.add(Developer("Pedro Henrique", "338043"))
        showLoading()

        aboutAdapter = AboutAdapter(developers)
        rvDevs.adapter = aboutAdapter

        mFirestore
            .collection(Constants.DEVELOPERS)
            .get()
            .addOnSuccessListener { result ->
                for (document in result)
                    aboutAdapter.addDev(document.toObject(Developer::class.java))
                hideLoading()
            }
            .addOnFailureListener { e ->
                showErrorMessage(e.message.toString())
                hideLoading()
            }
    }
}