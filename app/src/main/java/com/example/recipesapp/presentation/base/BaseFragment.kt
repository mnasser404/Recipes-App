package com.example.recipesapp.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import com.example.reciepsapp.R


abstract class BaseFragment : Fragment() {

    private lateinit var progressBar : ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val constraintLayout =  inflater.inflate(R.layout.fragment_base, container, false)
        progressBar = constraintLayout.findViewById(R.id.progressBar)
        loadLayoutFromResIdToViewStub(constraintLayout, container)
        return constraintLayout
    }

    abstract fun loadLayoutFromResIdToViewStub(rootView: View?, container: ViewGroup?)

    fun showHideProgressBar(isVisible: Boolean){
        if(isVisible){
            progressBar.visibility = View.VISIBLE
        }else{
            progressBar.visibility = View.GONE
        }
    }

}