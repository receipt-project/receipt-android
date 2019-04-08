package shiverawe.github.com.receipt.ui

import android.app.Application
import android.content.Context
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import shiverawe.github.com.receipt.data.network.Api
import java.util.concurrent.TimeUnit


class App: Application() {
    companion object {
        lateinit var appContext: Context
        lateinit var api: Api
    }

    override fun onCreate() {
        super.onCreate()
        val okHttpClient = OkHttpClient.Builder()
                .build()
        val retrofit = Retrofit.Builder()
                .baseUrl("http://3.16.244.144/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build()
        api = retrofit.create(Api::class.java)
        appContext = applicationContext
    }
}