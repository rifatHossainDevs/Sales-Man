package com.wevx.dealershipmanagement.core.di

import com.wevx.dealershipmanagement.data.remote.auth.AuthApiService
import com.wevx.dealershipmanagement.data.remote.home.AreaApiService
import com.wevx.dealershipmanagement.data.remote.order.OrderApiService
import com.wevx.dealershipmanagement.data.remote.product.ProductApiService
import com.wevx.dealershipmanagement.data.remote.storeOwner.StoreOwnerApiService
import com.wevx.dealershipmanagement.data.repository_impl.auth.AuthRepositoryImpl
import com.wevx.dealershipmanagement.data.repository_impl.home.AreaRepositoryImpl
import com.wevx.dealershipmanagement.data.repository_impl.order.OrderRepositoryImpl
import com.wevx.dealershipmanagement.data.repository_impl.product.ProductRepositoryImpl
import com.wevx.dealershipmanagement.data.repository_impl.storeOwner.StoreOwnerRepositoryImpl
import com.wevx.dealershipmanagement.domain.repository.auth.AuthRepository
import com.wevx.dealershipmanagement.domain.repository.home.AreaRepository
import com.wevx.dealershipmanagement.domain.repository.order.OrderRepository
import com.wevx.dealershipmanagement.domain.repository.product.ProductRepository
import com.wevx.dealershipmanagement.domain.repository.storeOwner.StoreOwnerRepository
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
    fun provideAreaApiService(retrofit: Retrofit): AreaApiService {
        return retrofit.create(AreaApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideAreaRepository(homeApiService: AreaApiService): AreaRepository {
        return AreaRepositoryImpl(homeApiService)
    }

    //Product
    @Provides
    @Singleton
    fun provideProductApiService(retrofit: Retrofit): ProductApiService {
        return retrofit.create(ProductApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideProductRepository(productApiService: ProductApiService): ProductRepository {
        return ProductRepositoryImpl(productApiService)
    }

    //StoreOwner
    @Provides
    @Singleton
    fun provideStoreOwnerApiService(retrofit: Retrofit): StoreOwnerApiService{
        return retrofit.create(StoreOwnerApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideStoreOwnerRepository(storeOwnerApiService: StoreOwnerApiService): StoreOwnerRepository {
        return StoreOwnerRepositoryImpl(storeOwnerApiService)
    }

    //Order
    @Provides
    @Singleton
    fun provideOrderApiService(retrofit: Retrofit): OrderApiService {
        return retrofit.create(OrderApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideOrderRepository(orderApiService: OrderApiService): OrderRepository {
        return OrderRepositoryImpl(orderApiService)
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