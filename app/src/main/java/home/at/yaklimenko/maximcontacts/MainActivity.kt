package home.at.yaklimenko.maximcontacts

import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import kotlin.system.exitProcess


class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private var needInit = true

    override fun onResume() {
        super.onResume()
        if (needInit) loadFragment()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d(TAG, "onOptionsItemSelected")
        if (item.itemId == R.id.menu_exit) {
            MaximContactsApplication.prefs.deleteAuth()
            loadAuthFragment()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun loadFragment() {
        if (!MaximContactsApplication.prefs.hasAuth) {
            Log.d(TAG, "has no auth. LoginFrgment")
            loadAuthFragment()
        } else {
            Log.d(TAG, "has auth. DepartmentFragment")
            MaximContactsApplication.networkService.useAuth(
                MaximContactsApplication.prefs.login, MaximContactsApplication.prefs.password
            )
            loadRootDepartmentFragment()
        }
        needInit = false
    }

    private fun loadAuthFragment() {
        val loginFragment = LoginFragment()
        clearBackStack()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame_box, loginFragment, LoginFragment.TAG)
            .commit()

    }

    private fun loadRootDepartmentFragment() {
        val departmentFragment = DepartmentFragment()
        clearBackStack()
        supportFragmentManager
            .beginTransaction()
            .addToBackStack("0")
            .replace(R.id.frame_box, departmentFragment, DepartmentFragment.TAG)
            .commit()
    }

    private fun clearBackStack() {
        for (i in 0 until supportFragmentManager.backStackEntryCount) {
            supportFragmentManager.popBackStack()
        }
    }

    override fun onBackPressed() {
        Log.d(TAG, "onBackPressed. BackStack: ${supportFragmentManager.backStackEntryCount}")
        super.onBackPressed()
        if (supportFragmentManager.backStackEntryCount == 0) {
            exitProcess(0)
        }

    }

    companion object {
        val TAG = MainActivity::class.simpleName
    }
}
