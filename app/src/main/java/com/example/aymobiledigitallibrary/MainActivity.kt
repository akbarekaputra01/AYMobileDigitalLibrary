package com.example.aymobiledigitallibrary
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.aymobiledigitallibrary.navigation.AppNavigation
import com.example.aymobiledigitallibrary.storage.ResultStorage
import com.example.aymobiledigitallibrary.storage.SessionStorage
import com.example.aymobiledigitallibrary.ui.theme.AppTheme
class MainActivity : ComponentActivity(){ override fun onCreate(savedInstanceState: Bundle?){ super.onCreate(savedInstanceState); enableEdgeToEdge(); setContent { AppTheme { AppNavigation(SessionStorage(this), ResultStorage(this)) } } } }
