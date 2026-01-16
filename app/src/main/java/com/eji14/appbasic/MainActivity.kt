package com.eji14.appbasic

import ShowCaseScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.eji14.appbasic.ui.screen.DialogExampleScreen
import com.eji14.appbasic.ui.screen.TextEditorScreen
import com.eji14.appbasic.ui.screen.TextShowCase
import com.eji14.appbasic.ui.theme.AppBasicTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppBasicTheme {
//                TextEditorScreen {  }
//                ExampleDialogScreen()
                DialogExampleScreen()
            }
        }
    }
}