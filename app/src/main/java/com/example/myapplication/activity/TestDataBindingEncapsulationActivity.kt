package com.example.myapplication.activity

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.adapter.HomeAdapter
import com.example.myapplication.adapter.SecondAdapter
import com.example.myapplication.bean.Student
import com.example.myapplication.bean.Teacher
import com.example.myapplication.databinding.ActivityTestDataBindingEncapsulationBinding
import com.example.myapplication.interfaces.ItemClickListener
import com.example.myapplication.viewmodel.MainViewModel

class TestDataBindingEncapsulationActivity : BaseActivity<ActivityTestDataBindingEncapsulationBinding, MainViewModel>() {
//    lateinit var homeAdapter: HomeAdapter
//    override fun ActivityTestDataBindingEncapsulationBinding.initBinding() {
//        Log.d("TestDataBinding", "initBinding: ${btn.text}")
//        homeAdapter = HomeAdapter(itemClickListener)
//        with(recyclerView) {
//            layoutManager = LinearLayoutManager(this@TestDataBindingEncapsulationActivity).apply {
//                orientation = RecyclerView.VERTICAL
//            }
//            adapter = homeAdapter
//        }
//        homeAdapter.setData(listOf("a", "b", "c", "d", "e", "f"))
//        btn.setOnClickListener {
//            Log.d("TestDataBinding", "第二次setData")
//            homeAdapter.setData(listOf("c", "d", "e", "f"))
//        }
//    }
//
//    private val itemClickListener = object : ItemClickListener<String> {
//        override fun onItemClick(view: View, position: Int, data: String) {
//            Log.d("TestDataBinding", "data: $data, position: ${homeAdapter.getActualPosition(data)}")
//        }
//
//    }

    override fun ActivityTestDataBindingEncapsulationBinding.initBinding() {
        val secondAdapter = SecondAdapter()
        with(recyclerView){
            layoutManager = LinearLayoutManager(this@TestDataBindingEncapsulationActivity).apply {
                orientation = RecyclerView.VERTICAL
            }
            adapter = secondAdapter
        }
        secondAdapter.setData(
            listOf(
                Teacher(1,"Person","语文"),
                Student(2,"Person","一年级"),
                Teacher(3,"Person","数学"),
            ))
    }

    override fun initObserve() {

    }

}