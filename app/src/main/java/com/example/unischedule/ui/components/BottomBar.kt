package com.example.unischedule.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.unischedule.R
import com.example.unischedule.ui.theme.LightTiffany


@Composable
fun BottomBar(
    navController: NavController,
    onAddClick: () -> Unit
) {
    var selectedItem by remember { mutableStateOf("calendar") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(vertical = 8.dp)
            .size(44.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BottomBarItem(
            icon = R.drawable.ic_calendar,
            label = "Календарь",
            isSelected = selectedItem == "calendar",
            onClick = {
                selectedItem = "calendar"
                navController.navigate("calendar")
            }
        )

        BottomBarItem(
            icon = R.drawable.ic_clock,
            label = "Сегодня",
            isSelected = selectedItem == "today",
            onClick = {
                selectedItem = "today"
                navController.navigate("today")
            }
        )

        BottomBarItem(
            icon = R.drawable.ic_settings,
            label = "Настройки",
            isSelected = selectedItem == "settings",
            onClick = {
                selectedItem = "settings"
                navController.navigate("settings")
            }
        )


        BottomBarItem(
            icon = R.drawable.ic_adding,
            label = "Добавить",
            isSelected = selectedItem == "add",
            onClick = {
                selectedItem = "add"
                onAddClick()
            }
        )
    }
}

@Composable
fun BottomBarItem(
    icon: Int,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = rememberRipple()
        ) { onClick() }

    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSecondary,
            modifier = Modifier.size(28.dp)
        )
    }
}
