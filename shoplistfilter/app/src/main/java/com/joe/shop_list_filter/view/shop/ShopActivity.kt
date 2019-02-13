package com.joe.shop_list_filter.view.shop

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.joe.shop_list_filter.R
import com.joe.shop_list_filter.databinding.ActivityShopBinding
import com.joe.shop_list_filter.model.ShopRepositoryImpl
import com.joe.shop_list_filter.view.base.BaseActivity
import com.joe.shop_list_filter.view.filter.FilterActivity
import com.joe.shop_list_filter.view.shop.list.ShopsAdapter
import com.joe.shop_list_filter.viewmodel.shop.ShopViewModel
import com.joe.shop_list_filter.viewmodel.shop.ShopViewModelFactory
import kotlinx.android.synthetic.main.activity_shop.*

class ShopActivity : BaseActivity<ActivityShopBinding>() {
    companion object {
        private const val FILTER_REQUEST_CODE = 0x01
        const val FILTER_RESULT_CODE = 0x02
    }

    override val resourceId: Int = R.layout.activity_shop
    private val viewModel: ShopViewModel by lazy {
        ViewModelProviders.of(this, ShopViewModelFactory(ShopRepositoryImpl())).get(ShopViewModel::class.java)
    }
    private val listAdapter = ShopsAdapter {
        startActivityForResult(Intent(this, FilterActivity::class.java), FILTER_REQUEST_CODE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        shopsObserve()
        errorObserve()
        loadShops(listOf(0, 0, 0, 0, 0, 0, 0), setOf())
        viewDataBinding.viewModel = viewModel
        viewDataBinding.setLifecycleOwner(this)
    }

    private fun initView() {
        list.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = listAdapter
        }
    }

    private fun shopsObserve() {
        viewModel.shops.observe(this, Observer {
            listAdapter.setItem(it)
        })
    }

    private fun errorObserve() {
        viewModel.error.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })
    }
    private fun loadShops(ageFilter: List<Int>, styleFilter: Set<String>) {
        viewModel.loadShops(assets.open("shop_list.json"), ageFilter, styleFilter)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FILTER_REQUEST_CODE && resultCode == FILTER_RESULT_CODE) {
            data?.let { it ->
                val ageFilter = it.getIntegerArrayListExtra("ageFilter").toList()
                val styleFiler = it.getStringArrayListExtra("styleFilter").toSet()
                loadShops(ageFilter, styleFiler)
            }
        }
    }
}