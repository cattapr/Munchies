package com.example.myapplication.feature.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.myapplication.R

val InterFont = FontFamily(
    Font(R.font.inter_24pt_regular, weight = FontWeight.W400),
    Font(R.font.inter_24pt_medium, weight = FontWeight.W500),
    Font(R.font.inter_24pt_bold, weight = FontWeight.W700),
)

val PoppinsFont = FontFamily(
    Font(R.font.poppins_medium, weight = FontWeight.W500),
)

val Typography = Typography(
    // title1 - Inter Regular 18sp
    titleLarge = TextStyle(
        fontFamily = InterFont,
        fontWeight = FontWeight.W400,
        fontSize = 18.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.sp
    ),
    // title2 - Poppins Medium 14sp
    titleMedium = TextStyle(
        fontFamily = PoppinsFont,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.sp
    ),
    // subtitle
    labelLarge = TextStyle(
        fontFamily = InterFont,
        fontWeight = FontWeight.W700,
        fontSize = 16.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.sp
    ),

    labelMedium = TextStyle(
        fontFamily = InterFont,
        fontWeight = FontWeight.W700,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.sp
    ),
    // footer1 - Inter Medium 10sp
    labelSmall = TextStyle(
        fontFamily = InterFont,
        fontWeight = FontWeight.W500,
        fontSize = 10.sp,
        lineHeight = 10.sp,
        letterSpacing = 0.sp
    ),
    // headline1 - Inter Regular 24sp
    headlineLarge = TextStyle(
        fontFamily = InterFont,
        fontWeight = FontWeight.W400,
        fontSize = 24.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.sp
    ),
    // headline2 - Inter Regular 16sp
    headlineMedium = TextStyle(
        fontFamily = InterFont,
        fontWeight = FontWeight.W400,
        fontSize = 16.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.sp
    )
)