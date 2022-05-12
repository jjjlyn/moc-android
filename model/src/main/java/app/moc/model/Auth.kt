package app.moc.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

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
@AndroidParcelize
data class User(
    val result: Boolean,
    val email: String?,
    val nickName: String?,
    val business: Int,
    val keyWords: String?,
    val leaveDate: Long,
    val userId: String?,
    val userToken: String?,
    val userTokenExpiredDate: Long,
    val deviceToken: String?,
    val notiFlag: Int
): AndroidParcel