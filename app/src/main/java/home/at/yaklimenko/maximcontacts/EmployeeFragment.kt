package home.at.yaklimenko.maximcontacts

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_employee.*
import kotlinx.coroutines.*
import org.json.JSONObject
import kotlin.coroutines.CoroutineContext

private const val ARG_EMPLOYEE = "employee"

class EmployeeFragment : Fragment(), CoroutineScope {

    private lateinit var job: Job
    private lateinit var employee: Employee

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job()
        arguments?.getString(ARG_EMPLOYEE)?.let {
            employee = Employee(JSONObject(it))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_employee, container, false)
    }

    override fun onResume() {
        super.onResume()
        loadEmployeeInfo()
    }

    private fun loadEmployeeInfo() {
        launch {
            val bitmap: Bitmap
            withContext(Dispatchers.IO) {
                bitmap = MaximContactsApplication.networkService.getEmpoyeePhoto(employee.id)
            }
            employee_photo.setImageBitmap(bitmap)
        }

        employee_name.text = employee.name
        employee.title?.let {
            employee_title_value.text = it
            employee_title.visibility = View.VISIBLE
        }

        employee.phone?.let {
            employee_phone_value.text = it
            employee_phone.visibility = View.VISIBLE
        }

        employee.email?.let {
            employee_email_value.text = it
            employee_email.visibility = View.VISIBLE
        }

    }

    override fun onDestroyView() {
        job.cancel()
        super.onDestroyView()
    }

    companion object {
        @JvmStatic
        fun newInstance(employee: Employee) =
            EmployeeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_EMPLOYEE, employee.toString())
                }
            }
    }
}
