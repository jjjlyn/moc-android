package app.moc.model

import androidx.annotation.Keep

@Keep
data class SignIn(
    val email: String,
    val pwd: String
)

@Keep
data class SignUp(
    val business: Int,
    val email: String,
    val keyWords: String,
    val leaveDate: Long,
    val nickName: String,
    val pwd: String
)

@Keep
data class User(
    val email: String,
    val nickName: String,
    val business: Int,
    val keyWords: String,
    val leaveDate: Long
)