package com.joe.shop_list_filter.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

sealed class ShopView

data class Shops(
    @SerializedName("week") @Expose val week: String,
    @SerializedName("list") @Expose val shops: List<Shop>
)

data class Shop(
    @SerializedName("0") @Expose val score: Int,
    @SerializedName("n") @Expose val name: String,
    @SerializedName("u") @Expose val url: String,
    @SerializedName("S") @Expose val style: String,
    @SerializedName("A") @Expose val ages: List<Int>,
    var styleMatchCount: Int = 0
) : ShopView()

data class ShopHeader(
    val week: String
) : ShopView()

const val AGE_EXIST = 1
const val AGE_NOT_EXIST = 0