package home.at.yaklimenko.maximcontacts

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

class MaximContactsApplication : Application() {
    companion object {
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

    private var loginMem : String? = null
    private var passwordMem : String? = null
    val prefs : SharedPreferences

    init {
        prefs = context.getSharedPreferences(PREFS_FILENAME, 0)
        if (prefs.contains(LOGIN_KEY) && prefs.contains(PASSWORD_KEY) ) {
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
        prefs.edit().clear().apply()
    }
}