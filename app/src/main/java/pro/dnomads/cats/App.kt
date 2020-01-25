package pro.dnomads.cats

import android.app.Application
import com.facebook.stetho.Stetho
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import pro.dnomads.cats.di.app.AppInjector
import timber.log.Timber
import javax.inject.Inject


val appContext: App by lazy {
    App.instance
}


class App : Application(), HasAndroidInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun androidInjector() = dispatchingAndroidInjector

    override fun onCreate() {
        super.onCreate()
        // Used Timber for logs
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        AppInjector.init(this)
        Stetho.initializeWithDefaults(this)
    }


    companion object {
        private var _instance: App? = null
        val instance: App
            get() = _instance ?: throw IllegalStateException("Application is not created")

    }
}