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
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.unischedule.R
import com.example.unischedule.ui.theme.Grey
import com.example.unischedule.ui.theme.Typography
import com.example.unischedule.data.UserDatabaseHelper


@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onBack: () -> Unit,
    darkTheme: Boolean
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

    val backgroundPainter = painterResource(
        if (darkTheme) R.drawable.login_and_registration_background_dark
        else R.drawable.login_and_registration_background
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = backgroundPainter,
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
            Text(
                text = stringResource(id = R.string.registration),
                style = Typography.displayMedium
            )

            Spacer(Modifier.height(32.dp))

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                textStyle = MaterialTheme.typography.labelMedium.copy(
                    color = MaterialTheme.colorScheme.onSecondary
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedContainerColor = MaterialTheme.colorScheme.onPrimary,
                    focusedBorderColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onPrimary,
                    focusedTextColor = MaterialTheme.colorScheme.onSecondary,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSecondary
                ),
                shape = RoundedCornerShape(15.dp),
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.login_input),
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
                    color = MaterialTheme.colorScheme.onSecondary
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedContainerColor = MaterialTheme.colorScheme.onPrimary,
                    focusedBorderColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onPrimary,
                    focusedTextColor = MaterialTheme.colorScheme.onSecondary,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSecondary
                ),
                shape = RoundedCornerShape(15.dp),
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.password),
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
                    color = MaterialTheme.colorScheme.onSecondary
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedContainerColor = MaterialTheme.colorScheme.onPrimary,
                    focusedBorderColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onPrimary,
                    focusedTextColor = MaterialTheme.colorScheme.onSecondary,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSecondary
                ),
                shape = RoundedCornerShape(15.dp),
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.password2),
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
                        errorMessage = context.getString(R.string.error_empty_fields)

                    password.length < 8 ->
                        errorMessage = context.getString(R.string.error_password_length)

                    !password.any { it.isDigit() } ->
                        errorMessage = context.getString(R.string.error_password_digit)

                    !password.any { it.isUpperCase() } ->
                        errorMessage = context.getString(R.string.error_password_upper)

                    !password.any { it.isLowerCase() } ->
                        errorMessage = context.getString(R.string.error_password_lower)

                    password != confirmPassword ->
                        errorMessage = context.getString(R.string.error_password_mismatch)

                    else -> {
                        val success = dbHelper.addUser(username, password)
                        if (success) {
                            val userId = dbHelper.getUserId(username)
                            if (userId != null) {
                                val prefs = context.getSharedPreferences("user_prefs", android.content.Context.MODE_PRIVATE)
                                prefs.edit()
                                    .putString("user_id", userId.toString())
                                    .putString("username", username)
                                    .apply()
                            }

                            Toast.makeText(context, context.getString(R.string.registration_success), Toast.LENGTH_SHORT).show()
                            onRegisterSuccess()
                        } else {
                            errorMessage = context.getString(R.string.error_user_exists)
                        }

                    }
                }
            },
            shape = RoundedCornerShape(15.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.onPrimary,
                contentColor = MaterialTheme.colorScheme.onSecondary
            ),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(start = 25.dp, bottom = 85.dp, end = 25.dp)
                .fillMaxWidth()
                .height(55.dp)
        ) {
            Text(
                text = stringResource(id = R.string.to_create),
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}
