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
import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.unischedule.R
import com.example.unischedule.ui.theme.Black
import com.example.unischedule.ui.theme.Grey
import com.example.unischedule.ui.theme.Typography
import com.example.unischedule.ui.theme.White
import com.example.unischedule.data.UserDatabaseHelper


@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onBack: () -> Unit
) {
    BackHandler {
        onBack()
    }

    val context = LocalContext.current
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
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
            Text("Регистрация",  style = Typography.displayMedium)

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

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
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
                        text = "Повторите пароль",
                        style = MaterialTheme.typography.labelMedium,
                        color = Grey
                    )
                },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
        Button(
            onClick = {
                when {
                    username.isBlank() || password.isBlank() || confirmPassword.isBlank() ->
                        errorMessage = "Все поля должны быть заполнены"

                    password.length < 8 ->
                        errorMessage = "Пароль должен содержать минимум 8 символов"

                    !password.any { it.isDigit() } ->
                        errorMessage = "Пароль должен содержать хотя бы одну цифру"

                    !password.any { it.isUpperCase() } ->
                        errorMessage = "Пароль должен содержать хотя бы одну заглавную букву"

                    !password.any { it.isLowerCase() } ->
                        errorMessage = "Пароль должен содержать хотя бы одну строчную букву"

                    password != confirmPassword ->
                        errorMessage = "Пароли не совпадают"

                    else -> {
                        val success = dbHelper.addUser(username, password)
                        if (success) {
                            Toast.makeText(context, "Регистрация успешна", Toast.LENGTH_SHORT).show()
                            onRegisterSuccess()
                        } else {
                            errorMessage = "Пользователь с таким логином уже существует"
                        }
                    }
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
                text = "Создать аккаунт",
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}
