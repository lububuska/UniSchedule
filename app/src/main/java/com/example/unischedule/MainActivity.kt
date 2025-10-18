package com.example.unischedule

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.unischedule.ui.components.BottomBar
import com.example.unischedule.ui.screens.*
import com.example.unischedule.ui.theme.UniScheduleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UniScheduleTheme {
                MainNavigation()
            }
        }
    }
}

@Composable
fun MainNavigation() {
    val navController = rememberNavController()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.primary,
        bottomBar = { BottomBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "calendar",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("calendar") { CalendarScreen(navController) }

            composable(
                route = "month/{monthName}",
                arguments = listOf(navArgument("monthName") { type = NavType.StringType })
            ) { backStackEntry ->
                val monthName = backStackEntry.arguments?.getString("monthName") ?: "Январь"
                // MonthDaysScreen(navController, monthName)  Здесь твой экран с календарём на месяц
            }
        }
    }
}
