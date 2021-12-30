package app.moc.shared.data.api

import app.moc.shared.data.api.response.BusinessResponse
import retrofit2.http.GET

interface BusinessService {
    @GET("jobs")
    suspend fun fetchBusiness(): List<BusinessResponse>
}