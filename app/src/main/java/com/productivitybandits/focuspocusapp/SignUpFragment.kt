package com.productivitybandits.focuspocusapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar

class SignUpFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nameInput = view.findViewById<EditText>(R.id.nameInput)
        val emailInput = view.findViewById<EditText>(R.id.emailInput)
        val passwordInput = view.findViewById<EditText>(R.id.passwordInput)
        val confirmPasswordInput = view.findViewById<EditText>(R.id.confirmPasswordInput)
        val signUpButton = view.findViewById<Button>(R.id.signUpButton)
        val loginText = view.findViewById<TextView>(R.id.loginText)

        signUpButton.setOnClickListener {
            val name = nameInput.text.toString().trim()
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()
            val confirmPassword = confirmPasswordInput.text.toString().trim()

            val errorMessage = validateSignUp(name, email, password, confirmPassword)

            if (errorMessage == null) {
                Toast.makeText(requireContext(), "Sign-Up Successful!", Toast.LENGTH_SHORT).show()
                // Navigate to Home (Example)
                findNavController().navigate(R.id.action_signUpFragment_to_homeFragment)
            } else {
                Snackbar.make(view, errorMessage, Snackbar.LENGTH_LONG).show()
            }
        }

        loginText.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
        }
    }

    private fun validateSignUp(name: String, email: String, password: String, confirmPassword: String): String? {
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            return "All fields are required!"
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return "Invalid email format!"
        }
        if (password.length < 6) {
            return "Password must be at least 6 characters!"
        }
        if (password != confirmPassword) {
            return "Passwords do not match!"
        }
        return null
    }
}
