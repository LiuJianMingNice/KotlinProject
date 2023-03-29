package com.example.jianshudemo.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.jianshudemo.R
import com.example.jianshudemo.common.BaseConstant
import com.example.jianshudemo.databinding.FragmentRegisterBinding
import com.example.jianshudemo.viewmodel.CustomViewModelProvider
import com.example.jianshudemo.viewmodel.RegisterModel


class RegisterFragment : Fragment() {

    private val registerModel: RegisterModel by viewModels {
        CustomViewModelProvider.providerRegisterModel(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentRegisterBinding = DataBindingUtil.inflate(
            inflater
        , R.layout.fragment_register
        , container
        , false
        )


        initData(binding)
        onSubscribeUi(binding)
        return binding.root
    }

    private fun initData(binding: FragmentRegisterBinding) {
        val safeArgs: RegisterFragmentArgs by navArgs()
        val email = safeArgs.email
        binding.model?.mail?.value = email

        binding.lifecycleOwner = this
        binding.model = registerModel
        binding.activity = activity
    }

    private fun onSubscribeUi(binding: FragmentRegisterBinding) {
        binding.btnRegister.setOnClickListener {
            registerModel.register()
            val bundle = Bundle()
            bundle.putString(BaseConstant.ARGS_NAME, registerModel.n.value)
            findNavController().navigate(R.id.login, bundle, null)
        }
    }

}