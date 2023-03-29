package com.example.myapplication.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.ComponentActivity
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.example.myapplication.R
import com.example.myapplication.base.BaseBinding
import com.example.myapplication.util.ViewModelUtils
import java.lang.reflect.ParameterizedType

abstract class BaseActivity<VB : ViewDataBinding, VM: ViewModel>(private val factory: ViewModelProvider.Factory? = null) : AppCompatActivity(), BaseBinding<VB> {
//    internal val mBinding: VB by lazy(mode = LazyThreadSafetyMode.NONE) {
//        getViewBinding(layoutInflater)
//    }

    protected val mBinding: VB by lazy(mode = LazyThreadSafetyMode.NONE) {
        getViewBinding(layoutInflater)
    }
    protected lateinit var viewModel: VM

//    @LayoutRes abstract fun getLayoutId(): Int
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        viewModel = ViewModelUtils.createViewModel(this, factory, 1)
        mBinding.initBinding()
        initObserve()
    }
    abstract fun initObserve()

    inline fun <VB:ViewBinding> Any.getViewBinding(inflater: LayoutInflater,position:Int = 0):VB{
        val vbClass =  (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments.filterIsInstance<Class<VB>>()
        val inflate = vbClass[position].getDeclaredMethod("inflate", LayoutInflater::class.java)
        return  inflate.invoke(null, inflater) as VB
    }

    inline fun <VM: ViewModel> ComponentActivity.createViewModel(position:Int): VM {
        val vbClass = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments.filterIsInstance<Class<*>>()
        val viewModel = vbClass[position] as Class<VM>
        return ViewModelProvider(this).get(viewModel)
    }
}