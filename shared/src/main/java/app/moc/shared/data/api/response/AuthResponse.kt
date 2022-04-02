package app.moc.shared.data.api.response

import androidx.annotation.Keep
import app.moc.model.User

@Keep
data class AuthResponse(
    val result: Boolean,
    val email: String?,
    val userId: Int,
    val pwd: String?,
    val nickName: String?,
    val business: Int,
    val keyWords: String?,
    val leaveDate: Long,
    val createDate: Long,
    val modifyDate: Long,
    val userToken: String?,
    val userTokenExpiredDate: Long,
    val deviceToken: String?,
    val notiFlag: Int
)

fun AuthResponse.toModel() = User(result, email, nickName, business, keyWords, leaveDate, userId.toString(), userToken, userTokenExpiredDate, deviceToken, notiFlag)