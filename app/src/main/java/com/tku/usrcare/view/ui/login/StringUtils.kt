package com.tku.usrcare.view.ui.login


fun String.containsWhitespace(): Boolean {
    return this.contains(" ")
}

fun String.isEmail(): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String.isPasswordLongEnough(): Boolean {
    return this.length >= 8
}

fun String.engNumOnly(): Boolean {
    return this.matches(Regex("^[a-zA-Z0-9]*$"))
}