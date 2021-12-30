package app.moc.shared.data.api

import app.moc.shared.data.api.request.SignInRequest
import app.moc.shared.data.api.request.SignUpRequest
import app.moc.shared.data.api.response.SignInResponse
import app.moc.shared.data.api.response.SignUpResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthService {

    @POST("login")
    suspend fun signIn(@Body signInRequest: SignInRequest): SignInResponse

    @POST("register")
    suspend fun signUp(@Body signUpRequest: SignUpRequest): SignUpResponse

    @GET("checkEmail")
    suspend fun checkEmailDuplicate(@Query("checkEmail") email: String)

    @GET("checkNickName")
    suspend fun checkNickNameDuplicate(@Query("checkNickName") nickName: String)
}