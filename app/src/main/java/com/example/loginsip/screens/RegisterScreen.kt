package com.example.loginsip.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource      // ✅ THIS LINE FIXES painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.loginsip.R
import com.google.firebase.auth.FirebaseAuth

@Composable
fun RegisterScreen(navController: NavHostController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.bg_login),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5E4D7)),
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .wrapContentHeight()
                .align(Alignment.Center)
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Image(painter = painterResource(id = R.drawable.logo), contentDescription = "Logo", modifier = Modifier.size(120.dp))
                Spacer(Modifier.height(16.dp))
                Text("Sign Up", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color(0xFF6F4E37))
                Spacer(Modifier.height(16.dp))

                OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Password") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(value = confirmPassword, onValueChange = { confirmPassword = it }, label = { Text("Confirm Password") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())

                Spacer(Modifier.height(16.dp))
                val isRegisterEnabled = email.isNotBlank() && password.isNotBlank() && confirmPassword.isNotBlank()
                Button(
                    onClick = {
                        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    navController.navigate("login") { popUpTo("register") { inclusive = true } }
                                } else {
                                    Toast.makeText(navController.context, task.exception?.message ?: "Registration failed", Toast.LENGTH_SHORT).show()
                                }
                            }
                    },
                    enabled = isRegisterEnabled,
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Register") }

                Spacer(Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                    Text("Already a user? ")
                    Text("LOGIN", color = Color(0xFFFFC085), fontWeight = FontWeight.Bold, modifier = Modifier.clickable { navController.navigate("login") })
                }
            }
        }
    }
}