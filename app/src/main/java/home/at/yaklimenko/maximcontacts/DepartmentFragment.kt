package home.at.yaklimenko.maximcontacts

import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE
import kotlinx.android.synthetic.main.fragment_department.*
import kotlinx.coroutines.*
import org.json.JSONObject
import kotlin.coroutines.CoroutineContext

class DepartmentFragment : Fragment(R.layout.fragment_department), CoroutineScope {

    private lateinit var job: Job

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job


    private var department: Department? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        arguments?.getString(ARG_DEPARTMENT)?.let {
            department = Department(jsonObject = JSONObject(it))
        }
        setHasOptionsMenu(true)
        job = Job()
        super.onCreate(savedInstanceState)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.menu_exit).setVisible(true)
        super.onPrepareOptionsMenu(menu)
    }

    override fun onResume() {
        super.onResume()
        if (department == null) {
            launch {
                val network = MaximContactsApplication.networkService
                val dep: Department
                withContext(Dispatchers.IO) {
                    dep = Department(network.sendGetAllRequest())
                    department = dep
                }
                hideLoading()
                showCurrentDep()
                showList(dep)
            }
        } else {
            hideLoading()
            showCurrentDep()
            showList(department!!)
        }
    }

    private fun showCurrentDep() {
        department_current_name.text = department?.name
    }

    private fun showList(department: Department) {
        val clickListener: View.OnClickListener = View.OnClickListener {
            val tag = it.tag
            when (tag) {
                is Department -> {
                    if (tag.contacts.isEmpty()) {
                        Toast.makeText(activity, "Этот департамент пуст", Toast.LENGTH_SHORT).show()
                    } else {

                        activity?.supportFragmentManager?.beginTransaction()
                            ?.addToBackStack(tag.id.toString())
                            ?.setTransition(TRANSIT_FRAGMENT_FADE)
                            ?.replace(R.id.frame_box, newInstance(tag))
                            ?.commit()
                    }
                }
                is Employee -> {
                    activity?.supportFragmentManager?.beginTransaction()
                        ?.addToBackStack("Employee")
                        ?.setTransition(TRANSIT_FRAGMENT_FADE)
                        ?.replace(R.id.frame_box, EmployeeFragment.newInstance(tag))
                        ?.commit()
                }
            }
        }
        departmen_list.adapter =
            ContactListAdapter(department.contacts, layoutInflater, clickListener)
    }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }

    private fun hideLoading() {
        progressbar?.visibility = View.GONE
        progressbar_hint?.visibility = View.GONE
    }

    companion object {
        val TAG = DepartmentFragment::class.simpleName
        private const val ARG_DEPARTMENT = "department"

        @JvmStatic
        fun newInstance(department: Department) =
            DepartmentFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_DEPARTMENT, department.toString())
                }
            }
    }
}
