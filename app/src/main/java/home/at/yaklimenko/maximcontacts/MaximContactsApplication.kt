package home.at.yaklimenko.maximcontacts

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

class MaximContactsApplication : Application() {
    companion object {
        lateinit var prefs: Prefs
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
    val prefs: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, 0);

    var hasAuth = prefs.contains(LOGIN_KEY)

    var login: String
        get() {
            if (!prefs.contains(LOGIN_KEY)) throw IllegalStateException("no login stored")
            return prefs.getString(LOGIN_KEY, "") ?: "";
        }
        set(value) = prefs.edit().putString(LOGIN_KEY, value).apply()

    var password: String
        get() {
            if (!prefs.contains(PASSWORD_KEY)) throw IllegalStateException("no password stored")
            return prefs.getString(PASSWORD_KEY, "") ?: "";
        }
        set(value) = prefs.edit().putString(PASSWORD_KEY, value).apply()


}