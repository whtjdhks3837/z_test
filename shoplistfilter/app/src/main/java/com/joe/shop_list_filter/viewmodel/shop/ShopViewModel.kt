package com.joe.shop_list_filter.viewmodel.shop

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.joe.shop_list_filter.data.Shop
import com.joe.shop_list_filter.model.ShopRepository
import com.joe.shop_list_filter.viewmodel.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import java.io.InputStream

class ShopViewModel(private val shopRepository: ShopRepository) : BaseViewModel() {
    private val _shops = MutableLiveData<Pair<String, List<Shop>>>()
    val shops: LiveData<Pair<String, List<Shop>>> = _shops

    fun loadShops(shops: InputStream, ageFilter: List<Int>, stylesFilter: Set<String>) {
        setProgress(true)
        compositeDisposable.add(
            shopRepository.loadShops(shops, ageFilter, stylesFilter)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess { setProgress(false) }
                .doOnError { setProgress(false) }
                .subscribe({
                    if (it.second.isEmpty())
                        error("쇼핑몰 목록이 없습니다.")
                    _shops.value = it
                }, {
                    it.printStackTrace()
                    error("error")
                })
        )
    }
}

class ShopViewModelFactory(private val shopRepository: ShopRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ShopViewModel(shopRepository) as T
    }
}
