package com.example.storyapp.utils.fonts

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import com.example.storyapp.R

val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val fontName = GoogleFont("Poppins")

val fontFamily = FontFamily(
    Font(googleFont = fontName, fontProvider = provider)
)

val MyTypography = Typography(
    // Judul besar atau headline utama (misalnya di layar beranda atau judul utama halaman)
    headlineLarge = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp // Ukuran untuk headline besar
    ),
    // Judul subheading atau secondary title
    headlineMedium = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp // Ukuran untuk headline atau subheading
    ),
    // Judul medium untuk elemen lebih kecil
    titleLarge = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp // Ukuran judul besar untuk    bagian penting
    ),
    // Ukuran judul untuk bagian atau elemen yang lebih kecil
    titleMedium = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp // Judul sedang
    ),
    // Ukuran judul kecil
    titleSmall = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp // Judul kecil
    ),
    // Ukuran teks paragraf utama atau teks konten biasa
    bodyLarge = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp // Teks utama yang digunakan di sebagian besar body text
    ),
    // Body text ukuran menengah
    bodyMedium = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp // Ukuran body text standar
    ),
    // Body text kecil, untuk keterangan tambahan atau teks di bawah
    bodySmall = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp // Ukuran body kecil
    ),
    // Untuk caption atau label kecil di bawah elemen UI
    labelSmall = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Light,
        fontSize = 10.sp // Ukuran teks sangat kecil, untuk keterangan atau caption
    ),
    // Teks yang digunakan pada tombol
    labelLarge = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp // Ukuran untuk teks tombol atau aksi
    )
)
