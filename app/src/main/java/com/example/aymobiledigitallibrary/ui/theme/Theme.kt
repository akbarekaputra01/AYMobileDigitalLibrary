package com.example.aymobiledigitallibrary.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val colors = lightColorScheme(
    background = Color(0xFFF9F9FF), surface = Color.White, primary = Color(0xFF0E3B69),
    primaryContainer = Color(0xFF2C5282), onBackground = Color(0xFF121C2C), onSurfaceVariant = Color(0xFF43474F), outline = Color(0xFFC3C6D0), error = Color(0xFFBA1A1A)
)

private val appTypography = Typography(
    headlineLarge = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.SemiBold),
    headlineMedium = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.SemiBold),
    headlineSmall = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.SemiBold),
    bodyLarge = TextStyle(fontSize = 16.sp),
    bodyMedium = TextStyle(fontSize = 14.sp),
    labelSmall = TextStyle(fontSize = 11.sp, letterSpacing = 0.8.sp, fontWeight = FontWeight.Bold)
)

@Composable fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = colors, typography = appTypography, shapes = Shapes(medium = RoundedCornerShape(16.dp)), content = content)
}
