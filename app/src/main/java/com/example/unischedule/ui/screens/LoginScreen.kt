package com.example.unischedule.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.unischedule.ui.theme.Typography
import android.content.Context
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.unischedule.R
import com.example.unischedule.ui.theme.Black
import com.example.unischedule.ui.theme.Grey
import com.example.unischedule.ui.theme.White
import com.example.unischedule.data.UserDatabaseHelper


@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onBack: () -> Unit
) {
    BackHandler {
        onBack()
    }

    val context = LocalContext.current
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    val dbHelper = UserDatabaseHelper(context)

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.login_and_registration_background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Авторизация", style = Typography.displayMedium)

            Spacer(Modifier.height(32.dp))

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                textStyle = MaterialTheme.typography.labelMedium.copy(
                    color = Black
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = White,
                    unfocusedContainerColor = White,
                    focusedBorderColor = White,
                    unfocusedBorderColor = White,
                    focusedTextColor = Black,
                    unfocusedTextColor = Black
                ),
                shape = RoundedCornerShape(15.dp),
                placeholder = {
                    Text(
                        text = "Логин",
                        style = MaterialTheme.typography.labelMedium,
                        color = Grey
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                textStyle = MaterialTheme.typography.labelMedium.copy(
                    color = Black
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = White,
                    unfocusedContainerColor = White,
                    focusedBorderColor = White,
                    unfocusedBorderColor = White,
                    focusedTextColor = Black,
                    unfocusedTextColor = Black
                ),
                shape = RoundedCornerShape(15.dp),
                placeholder = {
                    Text(
                        text = "Пароль",
                        style = MaterialTheme.typography.labelMedium,
                        color = Grey
                    )
                },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            if (errorMessage.isNotEmpty()) {
                Text(errorMessage, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelSmall)
            }

            TextButton(onClick = onNavigateToRegister) {
                Text(
                    text = "Создать аккаунт",
                    style = MaterialTheme.typography.labelMedium,
                    color = Black
                )
            }
        }

        Button(
            onClick = {
                val user = dbHelper.getUser(username)
                if (user != null && user.second == password) {
                    Toast.makeText(context, "Вход выполнен", Toast.LENGTH_SHORT).show()
                    onLoginSuccess()
                } else {
                    errorMessage = "Неверный логин или пароль"
                }

            },
            shape = RoundedCornerShape(15.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = White,
                contentColor = Black
            ),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(start = 25.dp, bottom = 85.dp, end = 25.dp)
                .fillMaxWidth()
                .height(55.dp)
        ) {
            Text(
                text = "Войти",
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}
