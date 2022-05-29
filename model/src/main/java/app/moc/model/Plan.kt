package app.moc.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import java.io.File

@Keep
@Parcelize
data class Plan(
    val id: Int? = null,
    val type: String,
    val title: String,
    val startDate: Long,
    val endDate: Long,
    val dayOfWeeks: String,
    val status: String? = null,
    val color: String?,
    val memo: String?,
): Parcelable {
    companion object {
        val EMPTY = Plan(null, "", "", 0L, 0L, "", null, null, null)
    }
}

@Keep
data class PlanCheck(
    val id: Int,
    val date: Long,
    val type: String,
    val satisfact: Int,
    val imageTags: String?
)

@Keep
data class PlanCheckQueryInfo(
    val planId: Int,
    val year: Int,
    val month: Int
)