package com.joe.shop_list_filter.model

import com.google.gson.Gson
import com.joe.shop_list_filter.data.AGE_EXIST
import com.joe.shop_list_filter.data.AGE_NOT_EXIST
import com.joe.shop_list_filter.data.Shop
import com.joe.shop_list_filter.data.Shops
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.io.InputStream

interface ShopRepository : Repository {
    fun loadShops(
        shops: InputStream,
        ageFilter: List<Int>,
        stylesFilter: Set<String>
    ): Single<Pair<String, List<Shop>>>
}

class ShopRepositoryImpl : ShopRepository {
    override fun loadShops(
        shops: InputStream,
        ageFilter: List<Int>,
        stylesFilter: Set<String>
    ): Single<Pair<String, List<Shop>>> {
        return when {
            ageFilter.any { age -> age == AGE_EXIST } && stylesFilter.isNotEmpty() -> {
                shopsObserve(shops)
                    .filter(ageFilter)
                    .filter(stylesFilter)
                    .sortStyleMatchCountWithAndScore()
            }
            ageFilter.any { age -> age == AGE_EXIST } && stylesFilter.isEmpty() -> {
                shopsObserve(shops)
                    .filter(ageFilter)
                    .sortStyleMatchCountWithAndScore()
            }
            ageFilter.all { age -> age == AGE_NOT_EXIST } && stylesFilter.isNotEmpty() -> {
                shopsObserve(shops)
                    .filter(stylesFilter)
                    .sortStyleMatchCountWithAndScore()
            }
            else -> { shopsObserve(shops).sortStyleMatchCountWithAndScore() }
        }
    }

    private fun shopsObserve(shopsInputStream: InputStream) =
        shopsInputSteamObserve(shopsInputStream)
            .inputStreamToShops()

    private fun shopsInputSteamObserve(shopsInputStream: InputStream) =
        Single.just(shopsInputStream)
            .subscribeOn(Schedulers.newThread())

    private fun Single<InputStream>.inputStreamToShops() =
        this.map { inputStream ->
            val json = inputStream.bufferedReader().use { it.readLine() }
            val shops = Gson().fromJson(json, Shops::class.java)
            Pair(shops.week, shops.shops)
        }

    private fun Single<Pair<String, List<Shop>>>.filter(ageFilter: List<Int>) =
        this.map {
            Pair(it.first, it.second.filter { shop ->
                var isFind = false
                shop.ages.forEachIndexed { index, age ->
                    if (age == 1 && age == ageFilter[index]) {
                        isFind = true
                        return@forEachIndexed
                    }
                }
                isFind
            })
        }

    private fun Single<Pair<String, List<Shop>>>.filter(stylesFilter: Set<String>) =
        this.map {
            Pair(it.first, it.second
                .filter { shop ->
                    val styles = shop.style.split(",").toSet()
                    val isStyleExist = stylesFilter.any { style -> style in styles }
                    if (isStyleExist) shop.styleMatchCount =
                            stylesFilter.count { style -> styles.contains(style) }
                    isStyleExist
                })
        }

    private fun Single<Pair<String, List<Shop>>>.sortStyleMatchCountWithAndScore() =
        this.map { shops ->
            Pair(
                shops.first,
                shops.second.sortedWith(compareByDescending<Shop> { it.styleMatchCount }.thenByDescending { it.score })
            )
        }
}