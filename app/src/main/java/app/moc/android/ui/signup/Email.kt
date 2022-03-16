package app.moc.android.ui.signup

import androidx.annotation.Keep

@Keep
data class EmailValidCheck(
    val checkedEmail: String,
    val isValid: Boolean
)