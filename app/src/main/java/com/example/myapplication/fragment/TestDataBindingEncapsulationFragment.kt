package com.example.myapplication.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentTestDataBindingEncapsulationBinding

/**
 * A simple [Fragment] subclass.
 * Use the [TestDataBindingEncapsulationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TestDataBindingEncapsulationFragment : BaseFragment<FragmentTestDataBindingEncapsulationBinding>() {
    override fun FragmentTestDataBindingEncapsulationBinding.initBinding() {
        Log.d("ljm", "initBinding: ${btn.text}")
    }

}