package com.example.passwordmanager.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.passwordmanager.Adapter.AccountAdapter
import com.example.passwordmanager.R
import com.google.android.material.appbar.MaterialToolbar


class Website : DialogFragment(){
    val TAG="WebSite_Dialog"
    private lateinit var websiteAdapter: AccountAdapter
    private lateinit var toolbar:MaterialToolbar
//    private lateinit var recyclerView: RecyclerView
    fun display(fragmentManager: FragmentManager):Website?{
        val web=Website()
        if(fragmentManager!=null){
            web.show(fragmentManager,TAG)
        }
        return web
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL,com.example.passwordmanager.R.style.AppTheme_FullScreenDialog)
    }
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view:View=inflater.inflate(R.layout.fragment_website, container, false)
        toolbar=view.findViewById(R.id.appBarLayout)
//        recyclerView=view.findViewById(R.id.account_adding_layout)
//        recyclerView.layoutManager = LinearLayoutManager(requireContext())
//        websiteAdapter = AccountAdapter(requireContext())
//        recyclerView.adapter = websiteAdapter
        return view
    }

    override fun onStart() {
        super.onStart()
        val dialog: Dialog?=dialog
        dialog?.let {
            val width=ViewGroup.LayoutParams.FILL_PARENT
            val height=ViewGroup.LayoutParams.FILL_PARENT
            it.window?.setLayout(width,height)
            it.window?.attributes?.windowAnimations= com.example.passwordmanager.R.style.AppTheme_Slide
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.setNavigationOnClickListener { dismiss() }
        toolbar.title = "Website"
        toolbar.setOnMenuItemClickListener {
            dismiss()
            true
        }
    }
}