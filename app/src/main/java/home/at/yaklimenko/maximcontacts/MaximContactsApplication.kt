package home.at.yaklimenko.maximcontacts

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import home.at.yaklimenko.maximcontacts.MaximContactsApplication.Companion.TAG

class MaximContactsApplication : Application() {
    companion object {
        val TAG = MaximContactsApplication::class.simpleName
        lateinit var prefs: Prefs
        val networkService = NetworkService()
    }

    override fun onCreate() {
        prefs = Prefs(applicationContext)
        super.onCreate()
    }
}

class Prefs(context: Context) {
    val PREFS_FILENAME = "home.at.yaklimenko.maximcontacts.prefs"
    val LOGIN_KEY = "login"
    val PASSWORD_KEY = "password"

    @Volatile
    private var loginMem: String? = null

    @Volatile
    private var passwordMem: String? = null
    val prefs: SharedPreferences

    init {
        prefs = context.getSharedPreferences(PREFS_FILENAME, 0)
        if (prefs.contains(LOGIN_KEY) && prefs.contains(PASSWORD_KEY)) {
            loginMem = prefs.getString(LOGIN_KEY, "") ?: ""
            passwordMem = prefs.getString(PASSWORD_KEY, "") ?: ""
        }
    }

    var hasAuth = loginMem != null && passwordMem != null

    var login: String
        get() = loginMem ?: ""
        set(value) {
            loginMem = value
            prefs.edit().putString(LOGIN_KEY, value).apply()
        }

    var password: String
        get() = passwordMem ?: ""
        set(value) {
            passwordMem = value
            prefs.edit().putString(PASSWORD_KEY, value).apply()
        }

    fun deleteAuth() {
        loginMem = null
        passwordMem = null
        Log.d(TAG, "cleared auth")
        prefs.edit().clear().apply()
    }
}