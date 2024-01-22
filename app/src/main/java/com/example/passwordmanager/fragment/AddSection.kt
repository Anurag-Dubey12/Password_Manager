package com.example.passwordmanager.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.example.passwordmanager.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddSection : DialogFragment() {
    val TAG = "example_dialog"

    private lateinit var toolbar: Toolbar

    fun display(fragmentManager: FragmentManager?): AddSection? {
        val exampleDialog = AddSection()
        if (fragmentManager != null) {
            exampleDialog.show(fragmentManager, TAG)
        }
        return exampleDialog
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view:View=inflater.inflate(R.layout.fragment_add_section, container, false)
        toolbar = view.findViewById(R.id.toolbar);
        return view
    }

    override fun onStart() {
        super.onStart()
        val dialog: Dialog? = dialog
        dialog?.let {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            it.window?.setLayout(width, height)
//            it.window?.attributes?.windowAnimations = R.style.AppTheme_Slide
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar!!.setNavigationOnClickListener { dismiss() }
        toolbar!!.title = "Some Title"
        toolbar!!.inflateMenu(R.menu.menuoption)
        toolbar!!.setOnMenuItemClickListener {
            dismiss()
            true
        }
    }}