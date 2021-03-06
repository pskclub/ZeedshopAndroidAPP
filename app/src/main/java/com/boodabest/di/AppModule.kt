package com.boodabest.di

import android.app.Application
import androidx.room.Room
import com.boodabest.database.*
import com.boodabest.services.AuthService
import com.boodabest.services.BannerService
import com.boodabest.services.BrandService
import com.boodabest.services.ProductService
import com.boodabest.utils.LiveDataCallAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import javax.inject.Singleton

var baseAPI = "https://boodabest-ecom.pams.ai/api/"


@Module(includes = [ViewModelModule::class])
class AppModule {
    private fun baseAPIOptions(): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor { chain ->
            val original = chain.request()
            // Request customization: add request headers
            val requestBuilder = original.newBuilder()
                .addHeader("language", "en")
                .addHeader("x-device", "android")
                .addHeader("x-timestamp", "2019-08-15 18:24:00")


            val request = requestBuilder.build()
            val tagName = "Network: "
            Timber.tag(tagName).i("*****************")
            Timber.tag(tagName).i("*****************")
            Timber.tag(tagName + "URL").i(request.url().toString())
            Timber.tag(tagName + "Method").i(request.method().toString())
            Timber.tag(tagName + "Headers").i(request.headers().toString())
            Timber.tag(tagName + "Body").i(request.body().toString())
            chain.proceed(request)
        }

        return httpClient.build()
    }

    @Singleton
    @Provides
    fun provideProductService(): ProductService {

        return Retrofit.Builder()
            .baseUrl(baseAPI)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .client(baseAPIOptions())
            .build()
            .create(ProductService::class.java)
    }


    @Singleton
    @Provides
    fun provideProductDb(app: Application): ProductDb {
        return Room
            .inMemoryDatabaseBuilder(app, ProductDb::class.java)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideProductDao(db: ProductDb): ProductDao {
        return db.productDao()
    }


    @Singleton
    @Provides
    fun provideBannerService(): BannerService {
        return Retrofit.Builder()
            .baseUrl(baseAPI)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .client(baseAPIOptions())
            .build()
            .create(BannerService::class.java)
    }


    @Singleton
    @Provides
    fun provideBannerDb(app: Application): BannerDb {
        return Room
            .inMemoryDatabaseBuilder(app, BannerDb::class.java)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideBannerDao(db: BannerDb): BannerDao {
        return db.bannerDao()
    }

    @Singleton
    @Provides
    fun provideBrandService(): BrandService {
        return Retrofit.Builder()
            .baseUrl(baseAPI)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .client(baseAPIOptions())
            .build()
            .create(BrandService::class.java)
    }

    @Singleton
    @Provides
    fun provideBrandDb(app: Application): BrandDb {
        return Room
            .inMemoryDatabaseBuilder(app, BrandDb::class.java)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideBrandDao(db: BrandDb): BrandDao {
        return db.brandDao()
    }


    @Singleton
    @Provides
    fun provideUserDb(app: Application): UserDb {
        return Room
            .databaseBuilder(app, UserDb::class.java, "user.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideUserDao(db: UserDb): UserDao {
        return db.userDao()
    }

    @Singleton
    @Provides
    fun provideAuthService(): AuthService {
        return Retrofit.Builder()
            .baseUrl(baseAPI)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .client(baseAPIOptions())
            .build()
            .create(AuthService::class.java)
    }
}
