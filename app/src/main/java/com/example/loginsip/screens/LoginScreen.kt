package com.example.loginsip.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.loginsip.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun LoginScreen(navController: NavHostController) {

    val context = LocalContext.current
    val prefs = context.getSharedPreferences("login_prefs", 0)

    var email by remember { mutableStateOf(prefs.getString("email", "") ?: "") }
    var password by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(prefs.getBoolean("remember", false)) }
    var passwordVisible by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

        // -------- BACKGROUND --------
        Image(
            painter = painterResource(id = R.drawable.bg_login),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // -------- CARD --------
        Card(
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5E4D7)),
            modifier = Modifier
                .fillMaxWidth(0.88f)
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo",
                    modifier = Modifier.size(120.dp)
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    text = "Sign In",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF6F4E37),
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(20.dp))

                // -------- EMAIL --------
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                )

                Spacer(Modifier.height(12.dp))

                // -------- PASSWORD --------
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    visualTransformation =
                        if (passwordVisible) VisualTransformation.None
                        else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector =
                                    if (passwordVisible) Icons.Default.Visibility
                                    else Icons.Default.VisibilityOff,
                                contentDescription = "Toggle password"
                            )
                        }
                    }
                )

                Spacer(Modifier.height(12.dp))

                // -------- REMEMBER + FORGOT --------
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    // LEFT SIDE — Remember Me
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Checkbox(
                            checked = rememberMe,
                            onCheckedChange = { rememberMe = it },
                            colors = CheckboxDefaults.colors(
                                checkedColor = Color(0xFF6F4E37)
                            )
                        )

                        Text(
                            text = "Remember me",
                            fontSize = 13.sp,
                            maxLines = 1,
                            softWrap = false
                        )
                    }

                    // RIGHT SIDE — Forgot Password
                    Text(
                        text = "Forgot password?",
                        color = Color(0xFF6F4E37),
                        fontSize = 12.sp,
                        maxLines = 1,
                        softWrap = false,
                        modifier = Modifier
                            .padding(start = 16.dp) // 👈 guaranteed visual gap
                            .clickable {
                                if (email.isBlank()) {
                                    Toast.makeText(
                                        context,
                                        "Enter your email first",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    FirebaseAuth.getInstance()
                                        .sendPasswordResetEmail(email)
                                        .addOnSuccessListener {
                                            Toast.makeText(
                                                context,
                                                "Reset email sent",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                        .addOnFailureListener {
                                            Toast.makeText(
                                                context,
                                                "Failed to send reset email",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                }
                            }
                    )
                }

                // -------- LOGIN BUTTON --------
                Button(
                    onClick = {
                        val auth = FirebaseAuth.getInstance()
                        val db = FirebaseFirestore.getInstance()

                        auth.signInWithEmailAndPassword(email, password)
                            .addOnSuccessListener {

                                if (rememberMe) {
                                    prefs.edit()
                                        .putString("email", email)
                                        .putBoolean("remember", true)
                                        .apply()
                                } else {
                                    prefs.edit().clear().apply()
                                }

                                val uid = auth.currentUser?.uid ?: return@addOnSuccessListener

                                db.collection("users").document(uid).get()
                                    .addOnSuccessListener { document ->
                                        val role = document.getString("role") ?: "customer"
                                        val name = auth.currentUser?.displayName
                                            ?: email.substringBefore("@")

                                        if (role == "staff") {
                                            navController.navigate("staff_dashboard") {
                                                popUpTo("login") { inclusive = true }
                                            }
                                        } else {
                                            navController.navigate("dashboard/$name") {
                                                popUpTo("login") { inclusive = true }
                                            }
                                        }
                                    }
                            }
                            .addOnFailureListener {
                                Toast.makeText(context, "Login failed", Toast.LENGTH_SHORT).show()
                            }
                    },
                    enabled = email.isNotBlank() && password.isNotBlank(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF6F4E37)
                    )
                ) {
                    Text("Login", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(Modifier.height(16.dp))

                // -------- SIGN UP --------
                Row {
                    Text("Need an account? ")
                    Text(
                        text = "SIGN UP",
                        color = Color(0xFF6F4E37),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable {
                            navController.navigate("register")
                        }
                    )
                }
            }
        }
    }
}
