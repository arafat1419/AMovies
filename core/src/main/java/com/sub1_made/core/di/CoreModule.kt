package com.sub1_made.core.di

import androidx.room.Room
import com.sub1_made.core.BuildConfig
import com.sub1_made.core.data.source.CatalogRepository
import com.sub1_made.core.data.source.local.LocalDataSource
import com.sub1_made.core.data.source.local.room.CatalogDb
import com.sub1_made.core.data.source.remote.RemoteDataSource
import com.sub1_made.core.data.source.remote.api.ApiService
import com.sub1_made.core.domain.repository.ICatalogRepository
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val databaseModule = module {
    factory { get<CatalogDb>().dao() }
    single {
        val passphrase: ByteArray = SQLiteDatabase.getBytes(BuildConfig.PASSPHARSE.toCharArray())
        val factory = SupportFactory(passphrase)
        Room.databaseBuilder(
            androidContext(),
            CatalogDb::class.java, "catalog_db"
        ).fallbackToDestructiveMigration()
            .openHelperFactory(factory)
            .build()
    }
}

val networkModule = module {
    single {
        val hostname = "api.themoviedb.org"
        val certificatePinner = CertificatePinner.Builder()
            /*.add(hostname, "sha256/AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=")*/
            .add(hostname, BuildConfig.PINNER1)
            .add(hostname, BuildConfig.PINNER2)
            .add(hostname, BuildConfig.PINNER3)
            .build()
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .certificatePinner(certificatePinner)
            .build()
    }
    single {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL_TMDB)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
        retrofit.create(ApiService::class.java)
    }
}

val repositoryModule = module {
    single { LocalDataSource(get()) }
    single { RemoteDataSource(get()) }
    single<ICatalogRepository> {
        CatalogRepository(
            get(),
            get()
        )
    }
}