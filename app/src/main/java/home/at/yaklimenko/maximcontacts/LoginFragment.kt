package home.at.yaklimenko.maximcontacts

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


class LoginFragment : Fragment(), CoroutineScope {
    private lateinit var job: Job

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        job = Job()
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.menu_exit).setVisible(false)
        super.onPrepareOptionsMenu(menu)
    }

    override fun onResume() {
        super.onResume()
        auth_button.setOnClickListener {
            if (login_edittext.text.isEmpty() || password_edittext.text.isEmpty()) {
                Toast.makeText(context, "Логин и пароль не должны быть пустыми", Toast.LENGTH_LONG)
                    .show()
            } else {
                performAuthCheck(login_edittext.text, password_edittext.text)
            }
        }
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
            if (res) {
                MaximContactsApplication.prefs.login = login.toString()
                MaximContactsApplication.prefs.password = password.toString()
                Toast.makeText(activity, "Вход выполнен. Данные сохранены", Toast.LENGTH_LONG)
                    .show()
                httpClient.useAuth(login.toString(), password.toString())
                loadRootDepartmentFragment()
            } else {
                Toast.makeText(activity, "Неправильный логин или пароль", Toast.LENGTH_LONG).show()

            }
        }
    }

    private fun loadRootDepartmentFragment() {
        val departmentFragment = DepartmentFragment()
        activity!!.supportFragmentManager.popBackStack(
            null,
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
        activity!!.supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame_box, departmentFragment, DepartmentFragment.TAG)
            .commit()
    }

    override fun onDestroyView() {
        job.cancel()
        super.onDestroyView()
    }


    companion object {
        val TAG = LoginFragment::class.simpleName
    }
}
