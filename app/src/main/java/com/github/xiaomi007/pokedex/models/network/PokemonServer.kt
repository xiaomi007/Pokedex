package com.github.xiaomi007.pokedex.models.network

import android.util.Log
import com.github.xiaomi007.pokedex.BuildConfig
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.Flowable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

/**
 * Server object and api interfaces
 *
 * @author Damien
 */
interface PokemonServer {

    companion object {
        private val flickrServer: PokemonServer by lazy {
            Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(getHttpClient())
                    .baseUrl("https://pokeapi.co/api/v2/")
                    .build()
                    .create(PokemonServer::class.java)
        }

        internal fun get(): PokemonServer {
            return flickrServer
        }

        private fun getHttpClient(): OkHttpClient {

            return if (BuildConfig.DEBUG) {
                val logger = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
                    Log.d("API", it)
                })
                logger.level = HttpLoggingInterceptor.Level.BASIC
                OkHttpClient.Builder()
                        .addInterceptor(logger)
                        .connectTimeout(30, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .writeTimeout(30, TimeUnit.SECONDS)
                        .build()
            } else {
                OkHttpClient.Builder()
                        .connectTimeout(30, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .writeTimeout(30, TimeUnit.SECONDS)
                        .build()
            }

        }

    }

    @GET("pokemon/")
    fun getAllPokemon(@Query(value = "limit") limit: Int): Flowable<CallWrapper>

}

