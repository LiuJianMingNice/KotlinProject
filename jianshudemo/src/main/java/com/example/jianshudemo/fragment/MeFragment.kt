package com.example.jianshudemo.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.transition.Explode
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.jianshudemo.R
import com.example.jianshudemo.databinding.FragmentMeBinding
import com.example.jianshudemo.viewmodel.CustomViewModelProvider
import com.example.jianshudemo.viewmodel.MeModel

class MeFragment : Fragment() {
    private val TAG by lazy { MeFragment::class.java.simpleName }
    private val model: MeModel by viewModels {
        CustomViewModelProvider.providerMeModel(requireContext())
    }

    //选择图片的标识
    private val REQUEST_CODE_IMAGE = 100

    //加载框
    private val sweetAlertDialog: SweetAlertDialog by lazy {
        SweetAlertDialog(requireContext(), SweetAlertDialog.PROGRESS_TYPE)
            .setTitleText("头像")
            .setContentText("更新中")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enterTransition = Explode()
        exitTransition = Explode()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentMeBinding = FragmentMeBinding.inflate(inflater, container, false)
        initListener(binding)
//        onSubscribeUi(binding)
        return binding.root
    }

    /**
     * 初始化监听器
     */
    private fun initListener(binding: FragmentMeBinding) {
        binding.ivHead.setOnClickListener {

        }
    }

}