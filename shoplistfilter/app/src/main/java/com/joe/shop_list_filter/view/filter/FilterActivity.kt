package com.joe.shop_list_filter.view.filter

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Gravity
import android.widget.CheckBox
import android.widget.GridLayout
import androidx.core.content.ContextCompat
import com.joe.shop_list_filter.R
import com.joe.shop_list_filter.data.Age
import com.joe.shop_list_filter.data.Style
import com.joe.shop_list_filter.databinding.ActivityFilterBinding
import com.joe.shop_list_filter.view.base.BaseActivity
import com.joe.shop_list_filter.view.shop.ShopActivity
import kotlinx.android.synthetic.main.activity_filter.*

class FilterActivity : BaseActivity<ActivityFilterBinding>() {
    companion object {
        private const val AGE_MAX_COL = 4
        private const val STYLE_MAX_COL = 3
    }

    override val resourceId: Int = R.layout.activity_filter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val ageViews = drawAgeViews()
        val styleViews = drawStyleViews()
        ageViews.forEach { setAgeOnClickListener(it) }
        styleViews.forEach { setStyleOnClickListener(it) }
        init.setOnClickListener { _ ->
            ageViews.forEach { it.background = getBackground(R.drawable.filter_age_unclick) }
            styleViews.forEach { it.background = getBackground(R.drawable.filter_style_unclick) }
        }
        filter.setOnClickListener { filter(ageViews, styleViews) }
        close.setOnClickListener { finish() }
    }

    private fun filter(ageViews: List<CheckBox>, styleViews: List<CheckBox>) {
        val ageFilter = mutableListOf<Int>()
        val styleFilter = mutableSetOf<String>()
        ageViews.forEach {
            when (it.isChecked) {
                true -> ageFilter.add(1)
                false -> ageFilter.add(0)
            }
        }
        styleViews.filter { it.isChecked }.forEach {
            styleFilter.add(it.text.toString())
        }
        val intent = Intent()
        intent.putIntegerArrayListExtra("ageFilter", ArrayList(ageFilter))
        intent.putStringArrayListExtra("styleFilter", ArrayList(styleFilter))
        setResult(ShopActivity.FILTER_RESULT_CODE, intent)
        finish()
    }

    private fun drawAgeViews(): List<CheckBox> {
        val views = mutableListOf<CheckBox>()
        Age.values().forEachIndexed { index, age ->
            val textView = makeGridCheckView(
                text = age.age,
                textColor = ContextCompat.getColor(this, R.color.colorPrimary),
                background = getBackground(R.drawable.filter_age_unclick),
                layoutParams = getGridParams(index / AGE_MAX_COL, index % AGE_MAX_COL)
            )
            ageGrid.addView(textView)
            views.add(textView)
        }
        return views
    }

    private fun drawStyleViews(): List<CheckBox> {
        val views = mutableListOf<CheckBox>()
        Style.values().forEachIndexed { index, style ->
            val textView = makeGridCheckView(
                text = style.style.first,
                textColor = ContextCompat.getColor(this, R.color.colorAccent),
                background = getBackground(R.drawable.filter_style_unclick),
                layoutParams = getGridParams(index / STYLE_MAX_COL, index % STYLE_MAX_COL)
            )
            styleGrid.addView(textView)
            views.add(textView)
        }
        return views
    }

    private fun setAgeOnClickListener(view: CheckBox) {
        view.setOnCheckedChangeListener { ageView, isChecked ->
            when (isChecked) {
                true -> {
                    ageView.setTextColor(ContextCompat.getColor(this, R.color.colorWhite))
                    ageView.background = getBackground(R.drawable.filter_age_click)
                }
                false -> {
                    ageView.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
                    ageView.background = getBackground(R.drawable.filter_age_unclick)
                }
            }
        }
    }

    private fun setStyleOnClickListener(view: CheckBox) {
        view.setOnCheckedChangeListener { styleView, isChecked ->
            when (isChecked) {
                true -> {
                    styleView.setTextColor(ContextCompat.getColor(this, R.color.colorWhite))
                    styleView.background = getBackground(R.drawable.filter_style_click)
                }
                false -> {
                    styleView.setTextColor(ContextCompat.getColor(this, R.color.colorAccent))
                    styleView.background = getBackground(R.drawable.filter_style_unclick)
                }
            }
        }
    }

    private fun makeGridCheckView(
        text: String,
        layoutParams: GridLayout.LayoutParams,
        textColor: Int? = null,
        background: Drawable? = null,
        gravity: Int = Gravity.CENTER
    ): CheckBox =
        CheckBox(this).apply {
            this.text = text
            this.layoutParams = layoutParams
            this.buttonDrawable = null
            this.gravity = gravity
            textColor?.let { setTextColor(it) }
            background?.let { this.background = it }
        }

    private fun getBackground(resourceId: Int) =
        ContextCompat.getDrawable(this, resourceId)

    private fun getGridParams(row: Int, col: Int): GridLayout.LayoutParams =
        GridLayout.LayoutParams(GridLayout.spec(row, 1f), GridLayout.spec(col, 1f)).apply {
            setMargins(5, 5, 5, 5)
        }
}