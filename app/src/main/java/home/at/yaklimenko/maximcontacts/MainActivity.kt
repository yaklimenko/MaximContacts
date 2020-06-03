package home.at.yaklimenko.maximcontacts

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE


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

    fun init() {
        if (!MaximContactsApplication.prefs.hasAuth) {
            loadAuthFragment()
        } else {
            loadRootDepartmentFragment()
        }
        needInit = false;
    }

    private fun loadAuthFragment() {
        val loginFragment = LoginFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame_box, loginFragment, LoginFragment.TAG)
            .commit()
    }

    private fun loadRootDepartmentFragment() {
        val departmentFragment = DepartmentFragment()
        supportFragmentManager.popBackStack(null, POP_BACK_STACK_INCLUSIVE)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame_box, departmentFragment, DepartmentFragment.TAG)
            .commit()
    }

}
