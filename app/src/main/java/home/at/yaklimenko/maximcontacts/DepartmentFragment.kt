package home.at.yaklimenko.maximcontacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE
import kotlinx.android.synthetic.main.fragment_department.*
import kotlinx.coroutines.*
import org.json.JSONObject
import kotlin.coroutines.CoroutineContext


private const val ARG_DEPARTMENT = "department"

class DepartmentFragment : Fragment(), CoroutineScope {

    private lateinit var job: Job

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job


    private var department: Department? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        arguments?.getString(ARG_DEPARTMENT)?.let {
            department = Department(jsonObject = JSONObject(it))
        }
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        job = Job()
        return inflater.inflate(R.layout.fragment_department, container, false)
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
            if (it.tag is Department) {
                val dep = it.tag as Department

                if (dep.contacts.isEmpty()) {
                    Toast.makeText(activity, "Этот департамент пуст", Toast.LENGTH_SHORT).show()
                } else {
                    activity?.supportFragmentManager?.beginTransaction()
                        ?.addToBackStack(dep.id.toString())
                        ?.setTransition(TRANSIT_FRAGMENT_FADE)
                        ?.replace(R.id.frame_box, newInstance(dep))
                        ?.commit()
                }
            } else if (it.tag is Employee) {
                val emp = it.tag as Employee
                activity?.supportFragmentManager?.beginTransaction()
                    ?.addToBackStack(emp.id.toString())
                    ?.setTransition(TRANSIT_FRAGMENT_FADE)
                    ?.replace(R.id.frame_box, EmployeeFragment.newInstance(emp))
                    ?.commit()
            }
        }
        departmen_list.adapter =
            ContactListAdapter(department.contacts, layoutInflater, clickListener)
    }

    private fun hideLoading() {
        progressbar?.visibility = View.GONE
        progressbar_hint?.visibility = View.GONE
    }

    override fun onDestroyView() {
        job.cancel()
        super.onDestroyView()
    }

    companion object {
        val TAG = DepartmentFragment::class.simpleName

        @JvmStatic
        fun newInstance(department: Department) =
            DepartmentFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_DEPARTMENT, department.toString())
                }
            }
    }
}
