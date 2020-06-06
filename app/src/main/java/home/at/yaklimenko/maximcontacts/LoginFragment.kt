package home.at.yaklimenko.maximcontacts

import android.os.Bundle
import android.text.Editable
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


class LoginFragment : Fragment(R.layout.fragment_login), CoroutineScope {
    private lateinit var job: Job

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        job = Job()
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth_button.setOnClickListener {
            if (login_edittext.text.isEmpty() || password_edittext.text.isEmpty()) {
                Toast.makeText(context, getString(R.string.toast_empty_auth), Toast.LENGTH_SHORT)
                    .show()
            } else {
                performAuthCheck(login_edittext.text, password_edittext.text)
            }
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.menu_exit).isVisible = false
        super.onPrepareOptionsMenu(menu)
    }

    private fun performAuthCheck(login: Editable, password: Editable) {
        launch {
            val httpClient = MaximContactsApplication.networkService
            val res = withContext(Dispatchers.IO) {
                httpClient.sendCheckAuthRequest(
                    login.toString(),
                    password.toString()
                )
            }
            if (res.success) {
                MaximContactsApplication.prefs.login = login.toString()
                MaximContactsApplication.prefs.password = password.toString()
                Toast.makeText(activity, getString(R.string.toast_auth_success), Toast.LENGTH_SHORT)
                    .show()
                httpClient.useAuth(login.toString(), password.toString())
                loadRootDepartmentFragment()
            } else {
                Toast.makeText(activity, res.message, Toast.LENGTH_SHORT)
                    .show()

            }
        }
    }

    private fun loadRootDepartmentFragment() {
        val departmentFragment = DepartmentFragment()
        activity?.supportFragmentManager?.popBackStack(
            null,
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
        activity?.supportFragmentManager
            ?.beginTransaction()
            ?.addToBackStack("0")
            ?.replace(R.id.frame_box, departmentFragment, DepartmentFragment.TAG)
            ?.commit()
    }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }


    companion object {
        val TAG = LoginFragment::class.simpleName
    }
}
