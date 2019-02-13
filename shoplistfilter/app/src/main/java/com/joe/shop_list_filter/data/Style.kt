package com.joe.shop_list_filter.data

import com.joe.shop_list_filter.R

enum class Style(val style: Pair<String, Int>) {
    FEMININE(Pair("페미닌", R.color.colorGray)), MODERN_CHIC(Pair("모던시크", R.color.colorModernChic)),
    SIMPLE_BASIC(Pair("심플베이직", R.color.colorSimpleBasic)), LOVELY(Pair("러블리", R.color.colorLovely)),
    UNIQUE(Pair("유니크", R.color.colorUnique)), MISSY_STYLE(Pair("미시스타일", R.color.colorMissy)),
    CAMPUS_LOOK(Pair("캠퍼스룩", R.color.colorCampus)), VINTAGE(Pair("빈티지", R.color.colorVintage)),
    SEXY_GRAM(Pair("섹시글램", R.color.colorSexyGram)), SCHOOL_LOOK(Pair("스쿨룩", R.color.colorSchoolLook)),
    ROMANTIC(Pair("로맨틱", R.color.colorRomantic)), OFFICE_LOOK(Pair("오피스룩", R.color.colorOfficeLook)),
    LUXURY(Pair("럭셔리", R.color.colorLuxury)), HOLLYWOOD_STYLE(Pair("헐리웃스타일", R.color.colorHollywoodStyle))
}