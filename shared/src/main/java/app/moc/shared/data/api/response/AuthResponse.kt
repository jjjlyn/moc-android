package app.moc.shared.data.api.response

import androidx.annotation.Keep
import app.moc.model.User

@Keep
data class SignInResponse(
    val result: Boolean,
    val email: String,
    val userId: Int,
    val pwd: String?,
    val nickName: String,
    val business: Int,
    val keyWords: String,
    val leaveDate: Long,
    val createDate: Long,
    val modifyDate: Long,
    val deviceToken: String,
    val notiFlag: Int
)

fun SignInResponse.toModel() = User(email, nickName, business, keyWords, leaveDate)

@Keep
data class SignUpResponse(
    val result: Boolean,
    val email: String,
    val userId: Int,
    val pwd: String,
    val nickName: String,
    val business: Int,
    val keyWords: String,
    val leaveDate: Long,
    val createDate: Long,
    val modifyDate: Long,
    val deviceToken: String?,
    val notiFlag: Int
)

fun SignUpResponse.toModel() = User(email, nickName, business, keyWords, leaveDate)