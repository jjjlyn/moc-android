package app.moc.shared.di

import app.moc.core.BuildConfig
import app.moc.shared.data.api.AuthService
import app.moc.shared.data.api.BusinessService
import app.moc.shared.data.prefs.PreferencesStorage
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

// 지라 테스트
@InstallIn(SingletonComponent::class)
@Module(includes = [ApiModule.Providers::class])
object ApiModule {

    @Provides
    @Singleton
    fun provideAuthService(
        retrofit: Retrofit
    ): AuthService {
        return retrofit.create(AuthService::class.java)
    }

    @Provides
    @Singleton
    fun provideBusinessService(
        retrofit: Retrofit
    ): BusinessService {
        return retrofit.create(BusinessService::class.java)
    }

    @InstallIn(SingletonComponent::class)
    @Module
    internal object Providers {
        @Provides
        @Singleton
        fun provideOkHttpClient(
            preferencesStorage: PreferencesStorage
        ): OkHttpClient {
            val userToken = runBlocking {
                preferencesStorage.userToken.first()
            }
            return OkHttpClient.Builder()
                .addInterceptor(Interceptor { chain ->
                    val request = chain.request().newBuilder().addHeader("Authorization", userToken).build()
                    chain.proceed(request)
                })
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
                    else HttpLoggingInterceptor.Level.NONE
                })
                .build()
        }
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_END_POINT)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(okHttpClient)
            .build()
    }
}


