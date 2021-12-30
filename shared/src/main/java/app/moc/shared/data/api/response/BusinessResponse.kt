package app.moc.shared.data.api.response

import androidx.annotation.Keep
import app.moc.model.Business
import com.squareup.moshi.Json

@Keep
data class BusinessResponse(
    @Json(name = "job_no")
   val jobNo: Int,
   val depth1: String,
   val depth2: String
)