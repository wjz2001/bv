package dev.aaa1115910.bv

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import de.schnettler.datastore.manager.DataStoreManager
import dev.aaa1115910.bv.repository.UserRepository
import dev.aaa1115910.bv.viewmodel.HomeViewModel
import dev.aaa1115910.bv.viewmodel.LoginViewModel
import dev.aaa1115910.bv.viewmodel.PlayerViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module

class BVApp : Application() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        lateinit var dataStoreManager: DataStoreManager
        lateinit var koinApplication: KoinApplication
        lateinit var firebaseAnalytics: FirebaseAnalytics
    }

    override fun onCreate() {
        super.onCreate()
        context = this.applicationContext
        dataStoreManager = DataStoreManager(applicationContext.dataStore)
        koinApplication = startKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@BVApp)
            modules(appModule)
        }
        firebaseAnalytics = Firebase.analytics
    }
}

val appModule = module {
    single { UserRepository() }
    viewModel { HomeViewModel() }
    viewModel { LoginViewModel(get()) }
    viewModel { PlayerViewModel() }
}

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "Settings")