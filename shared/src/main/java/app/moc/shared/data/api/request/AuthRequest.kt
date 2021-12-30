package app.moc.shared.data.api.request

import androidx.annotation.Keep
import app.moc.model.SignIn
import app.moc.model.SignUp

@Keep
data class SignInRequest(
    val email: String,
    val pwd: String
)

fun SignIn.toRequest() = SignInRequest(email, pwd)

@Keep
data class SignUpRequest(
    val business: Int,
    val email: String,
    val keyWords: String,
    val leaveDate: Long,
    val nickName: String,
    val pwd: String
)

fun SignUp.toRequest() = SignUpRequest(business, email, keyWords, leaveDate, nickName, pwd)