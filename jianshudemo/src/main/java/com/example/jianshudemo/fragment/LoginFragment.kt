package com.example.jianshudemo.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.jianshudemo.MainActivity
import com.example.jianshudemo.R
import com.example.jianshudemo.common.BaseConstant
import com.example.jianshudemo.databinding.FragmentLoginBinding
import com.example.jianshudemo.utils.AppPrefsUtils
import com.example.jianshudemo.viewmodel.CustomViewModelProvider
import com.example.jianshudemo.viewmodel.LoginModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * 登录的Fragment
 *
 * A simple [Fragment] subclass.
 */
class LoginFragment : Fragment() {

    private val loginModel: LoginModel by viewModels {
        CustomViewModelProvider.providerLoginModel(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val binding: FragmentLoginBinding = DataBindingUtil.inflate(
            inflater
            , R.layout.fragment_login
            , container
            , false
        )

        onSubscribeUi(binding)

        //liu  调试用，先注释
//        val isFirstLaunch = AppPrefsUtils.getBoolean(BaseConstant.IS_FIRST_LAUNCH)
//        if (isFirstLaunch) {
//            onFirstLaunch()
//        }

        return binding.root
    }

    private fun onSubscribeUi(binding: FragmentLoginBinding) {
        binding.model = loginModel
        binding.activity = activity

        //如果使用LiveData下面这句必须加上
        binding.lifecycleOwner = this

        binding.btnLogin.setOnClickListener {
            loginModel.login()?.observe(viewLifecycleOwner, { user ->
                user?.let {
                    AppPrefsUtils.putLong(BaseConstant.SP_USER_ID, it.id)
                    AppPrefsUtils.putString(BaseConstant.SP_USER_NAME, it.account)
                    val intent = Intent(context, MainActivity::class.java)
                    requireContext().startActivity(intent)
                    Toast.makeText(context, "登陆成功!", Toast.LENGTH_SHORT).show()
                }
            })
        }
        arguments?.getString(BaseConstant.ARGS_NAME)?.apply {
            loginModel.n.value = this
        }
    }

    //第一次启动的时候调用
    private fun onFirstLaunch() {
        lifecycleScope.launch(Dispatchers.Main) {
            val str = withContext(Dispatchers.IO) {
                loginModel.onFirstLaunch()
            }
            Toast.makeText(requireContext(), str, Toast.LENGTH_SHORT).show()
            AppPrefsUtils.putBoolean(BaseConstant.IS_FIRST_LAUNCH, false)
        }
    }

}