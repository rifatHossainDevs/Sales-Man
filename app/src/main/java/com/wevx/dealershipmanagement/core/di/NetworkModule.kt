package com.wevx.dealershipmanagement.core.di

import com.wevx.dealershipmanagement.data.remote.auth.AuthApiService
import com.wevx.dealershipmanagement.data.remote.home.HomeApiService
import com.wevx.dealershipmanagement.data.repository_impl.auth.AuthRepositoryImpl
import com.wevx.dealershipmanagement.data.repository_impl.home.HomeRepositoryImpl
import com.wevx.dealershipmanagement.domain.repository.auth.AuthRepository
import com.wevx.dealershipmanagement.domain.repository.home.HomeRepository
import com.wevx.dealershipmanagement.domain.use_case.home_usecase.GetDistrictUseCase
import com.wevx.dealershipmanagement.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Auth
    @Provides
    @Singleton
    fun provideAuthApiService(retrofit: Retrofit): AuthApiService {
        return retrofit.create(AuthApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(authApiService: AuthApiService): AuthRepository {
        return AuthRepositoryImpl(authApiService)
    }

    //Home
    @Provides
    @Singleton
    fun provideHomeApiService(retrofit: Retrofit): HomeApiService {
        return retrofit.create(HomeApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideHomeRepository(homeApiService: HomeApiService): HomeRepository {
        return HomeRepositoryImpl(homeApiService)
    }

    @Provides
    @Singleton
    fun provideDistrictUseCase(
        homeRepository: HomeRepository
    ): GetDistrictUseCase {
        return GetDistrictUseCase(homeRepository)
    }

    // Profile
    /*@Provides
    @Singleton
    fun provideProfileApiService(retrofit: Retrofit): ProfileApiService{
        return retrofit.create(ProfileApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideProfileRepository(profileApiService: ProfileApiService): ProfileRepository{
        return ProfileRepositoryImpl(profileApiService)
    }*/

}

