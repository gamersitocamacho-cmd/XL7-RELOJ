package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.ui.screens.ClockFacade
import com.example.ui.screens.MainSystemShell
import com.example.ui.screens.UnlockSequence
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    private val viewModel: TerminalViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                val currentScreen by viewModel.currentScreen.collectAsState()

                Box(modifier = Modifier.fillMaxSize()) {
                    when (currentScreen) {
                        is Screen.Clock -> ClockFacade(viewModel = viewModel)
                        is Screen.Unlocking -> UnlockSequence(viewModel = viewModel)
                        is Screen.MainTerminal -> MainSystemShell(viewModel = viewModel)
                    }
                }
            }
        }
    }
}
