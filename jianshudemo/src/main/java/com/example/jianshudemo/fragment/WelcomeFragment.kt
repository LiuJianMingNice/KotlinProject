package com.example.jianshudemo.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.example.jianshudemo.R
import com.example.jianshudemo.common.BaseConstant
import com.example.jianshudemo.utils.AppPrefsUtils

class WelcomeFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_welcome, container, false)
    }

    lateinit var btnLogin: Button
    lateinit var btnRegister: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnLogin = view.findViewById(R.id.btn_login)
        btnRegister = view.findViewById(R.id.btn_register)

        btnLogin.setOnClickListener {
            val navOption = navOptions {
                anim {
                    enter = R.anim.common_slide_in_right
                    exit = R.anim.common_slide_out_left
                    popEnter = R.anim.common_slide_in_left
                    popExit = R.anim.common_slide_out_right
                }
            }

            val name = AppPrefsUtils.getString(BaseConstant.SP_USER_NAME)
            //Navigation传递参数
            val bundle = Bundle()
            bundle.putString(BaseConstant.ARGS_NAME, name)
            findNavController().navigate(R.id.login, bundle, navOption)
        }

        btnRegister.setOnClickListener {
            //利用SafeArgs传递参数
            val action = WelcomeFragmentDirections
                .actionWelcomeToRegister()
                .setEMAIL("TeaOf1995@Gamil.com")
            findNavController().navigate(action)
        }
    }

}