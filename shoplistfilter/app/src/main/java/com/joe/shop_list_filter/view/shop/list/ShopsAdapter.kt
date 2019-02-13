package com.joe.shop_list_filter.view.shop.list

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.joe.shop_list_filter.R
import com.joe.shop_list_filter.data.*
import com.joe.shop_list_filter.databinding.ListShopHeaderBinding
import com.joe.shop_list_filter.databinding.ListShopItemBinding
import java.lang.StringBuilder
import java.net.URL

const val shopImageUrl = "https://cf.shop.s.zigzag.kr/images/"

class ShopsAdapter(private val filterClick: () -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        private const val HEADER_VIEW_TYPE = 0x01
        private const val SHOP_VIEW_TYPE = 0x002
    }

    private val shops = mutableListOf<ShopView>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            HEADER_VIEW_TYPE -> HeaderViewHolder(
                ListShopHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false), filterClick
            )
            else -> ShopsViewHolder(
                ListShopItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            HEADER_VIEW_TYPE -> (holder as HeaderViewHolder).bind(shops[position] as ShopHeader)
            else -> (holder as ShopsViewHolder).bind(shops[position] as Shop)
        }
    }

    override fun getItemCount(): Int = shops.size

    override fun getItemViewType(position: Int): Int =
        when (shops[position]) {
            is ShopHeader -> HEADER_VIEW_TYPE
            is Shop -> SHOP_VIEW_TYPE
        }

    fun setItem(items: Pair<String, List<Shop>>) {
        this.shops.clear()
        this.shops.add(ShopHeader(items.first))
        this.shops.addAll(items.second)
        notifyDataSetChanged()
    }
}

class HeaderViewHolder(private val binding: ListShopHeaderBinding, private val filterClick: () -> Unit) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(header: ShopHeader) {
        binding.header = header
        binding.filter.setOnClickListener {
            filterClick()
        }
    }
}

class ShopsViewHolder(private val binding: ListShopItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(shop: Shop) {
        binding.apply {
            this.shop = shop
            index.text = "$adapterPosition"
            ages.text = getAgeNames(shop.ages)
            styles.removeAllViews()
            shop.style.split(",").forEach {
                styles.addView(drawStyleTextView(it))
            }
        }

        Glide.with(itemView.context)
            .load("$shopImageUrl${hostParse(shop.url)}.jpg")
            .apply(
                RequestOptions
                    .placeholderOf(R.drawable.ic_image)
                    .error(R.drawable.ic_image)
                    .circleCrop()
            )
            .into(binding.image)
    }

    private fun drawStyleTextView(text: String): TextView =
        TextView(itemView.context).apply {
            this.text = text
            textSize = 10f
            setPadding(10, 2, 10, 2)
            background = ContextCompat.getDrawable(itemView.context, R.drawable.style_border)
            Style.values().find { it.style.first == text }?.let {
                background.setTint(ContextCompat.getColor(itemView.context, it.style.second))
            }
        }


    private fun hostParse(url: String): String {
        val host = URL(url).host
        return when (host.contains("www.")) {
            true -> host.split('.')[1]
            false -> host.split('.')[0]
        }
    }

    private fun getAgeNames(ages: List<Int>): String {
        val text = StringBuilder()
        ages.forEachIndexed { index, age ->
            when {
                age == AGE_EXIST && index == 0 -> {
                    text.append(Age.values()[index].age)
                }
                age == AGE_EXIST && index in 1..3 && !text.contains("20대") -> {
                    text.append(Age.values()[index].age.substring(0, 3))
                }
                age == AGE_EXIST && index in 4..6 && !text.contains("30대") -> {
                    text.append(Age.values()[index].age.substring(0, 3))
                }
            }
        }
        return text.toString()
    }
}