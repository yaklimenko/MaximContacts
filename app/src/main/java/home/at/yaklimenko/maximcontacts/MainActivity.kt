package home.at.yaklimenko.maximcontacts



import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    var needInit = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        if (needInit) init()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d(TAG,"onOptionsItemSelected")
        if (item.itemId == R.id.menu_exit) {
            MaximContactsApplication.prefs.deleteAuth()
            init()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun init() {
        if (!MaximContactsApplication.prefs.hasAuth) {
            loadAuthFragment()
        } else {
            MaximContactsApplication.networkService.useAuth(
                MaximContactsApplication.prefs.login, MaximContactsApplication.prefs.password
            )
            loadRootDepartmentFragment()
        }
        needInit = false
    }

    private fun loadAuthFragment() {
        val loginFragment = LoginFragment()
        for (i in 0 until supportFragmentManager.getBackStackEntryCount()) {
            supportFragmentManager.popBackStack()
        }
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame_box, loginFragment, LoginFragment.TAG)
            .commit()

    }

    private fun loadRootDepartmentFragment() {
        val departmentFragment = DepartmentFragment()
        for (i in 0 until supportFragmentManager.getBackStackEntryCount()) {
            supportFragmentManager.popBackStack()
        }
        supportFragmentManager
            .beginTransaction()
            .addToBackStack(getString(R.string.root_department_name))
            .replace(R.id.frame_box, departmentFragment, DepartmentFragment.TAG)
            .commit()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Log.d(TAG, "onBackPressed. BackStack: ${supportFragmentManager.backStackEntryCount}")
        if (supportFragmentManager.backStackEntryCount == 0) {
           System.exit(0)
        }

    }

    companion object {
        val TAG = MainActivity::class.simpleName
    }
}
