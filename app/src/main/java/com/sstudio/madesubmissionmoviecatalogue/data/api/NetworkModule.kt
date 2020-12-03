package com.sstudio.madesubmissionmoviecatalogue.data.api

import android.content.Context
import android.os.Build
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton
import javax.net.ssl.SSLContext


@Module
class NetworkModule {
    @Provides
    @Singleton
    fun provideRetrofit(
        @Named("baseUrl") baseUrl: String, gson: Gson,
        okHttpClient: OkHttpClient
    ): Retrofit {
        var retrofit: Retrofit? = null

        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .build()
        }
        return retrofit as Retrofit
    }

    @Provides
    @Singleton
    fun provideOkhttpClient(
        cache: Cache,
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        val okHttp = OkHttpClient
            .Builder()
            .followRedirects(true)
            .followSslRedirects(true)
            .retryOnConnectionFailure(true)
            .cache(null).connectTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .addInterceptor(httpLoggingInterceptor)
            .cache(cache)
//            .connectionSpecs(Collections.singletonList(spec))
        return enableTls12OnPreLollipop(okHttp)?.build() as OkHttpClient
    }

    @Provides
    @Singleton
    fun provideInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return httpLoggingInterceptor
    }

    @Provides
    @Singleton
    fun provideOkHttpCache(context: Context): Cache {
        val cacheSize = 10 * 1024 * 1024 // 10 MB
        return Cache(context.cacheDir, cacheSize.toLong())
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().create()
    }

    private val spec = ConnectionSpec.Builder(ConnectionSpec.COMPATIBLE_TLS)
        .supportsTlsExtensions(true)
        .tlsVersions(TlsVersion.TLS_1_2, TlsVersion.TLS_1_1, TlsVersion.TLS_1_0)
        .cipherSuites(
            CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
            CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
            CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256,
            CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA,
            CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA,
            CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA,
            CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA,
            CipherSuite.TLS_ECDHE_ECDSA_WITH_RC4_128_SHA,
            CipherSuite.TLS_ECDHE_RSA_WITH_RC4_128_SHA,
            CipherSuite.TLS_DHE_RSA_WITH_AES_128_CBC_SHA,
            CipherSuite.TLS_DHE_DSS_WITH_AES_128_CBC_SHA,
            CipherSuite.TLS_DHE_RSA_WITH_AES_256_CBC_SHA
        )
        .build()

    private fun enableTls12OnPreLollipop(client: OkHttpClient.Builder): OkHttpClient.Builder? {
        if (Build.VERSION.SDK_INT in 16..21) {
            try {
                val sc: SSLContext = SSLContext.getInstance("TLSv1.2")
                sc.init(null, null, null)
                client.sslSocketFactory(Tls12SocketFactory(sc.socketFactory))
                val cs = ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                    .tlsVersions(TlsVersion.TLS_1_2).build()
                val specs: MutableList<ConnectionSpec> = ArrayList()
                specs.add(cs)
                specs.add(ConnectionSpec.COMPATIBLE_TLS)
                specs.add(ConnectionSpec.CLEARTEXT)
                client.connectionSpecs(specs)
            } catch (exc: Exception) {
                Log.e("OkHttpTLSCompat", "Error while setting TLS 1.2", exc)
            }
        }
        return client
    }
}