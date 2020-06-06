package home.at.yaklimenko.maximcontacts

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_employee.*
import kotlinx.coroutines.*
import org.json.JSONObject
import kotlin.coroutines.CoroutineContext

class EmployeeFragment : Fragment(R.layout.fragment_employee), CoroutineScope {

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

        with(employee) {
            employee_name.text = name
            if (title.isNotEmpty()) {
                employee_title_value.text = title
                employee_title.visibility = View.VISIBLE
            }

            if (phone.isNotEmpty()) {
                employee_phone_value.text = phone
                employee_phone_value.setOnClickListener {
                    val intent = Intent(Intent.ACTION_DIAL)
                    intent.data = Uri.fromParts(URI_SCHEME_TEL, phone, null)
                    startActivity(intent)
                }
                employee_phone.visibility = View.VISIBLE
            }

            if (email.isNotEmpty()) {
                employee_email_value.text = email
                employee_email_value.setOnClickListener {
                    val intent = Intent(Intent.ACTION_SENDTO)
                    intent.data = Uri.fromParts(
                        URI_SCHEME_MAILTO, email, null)
                    startActivity(intent)
                }
                employee_email.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroyView() {
        job.cancel()
        super.onDestroyView()
    }

    companion object {
        private const val ARG_EMPLOYEE = "employee"
        private const val URI_SCHEME_TEL = "tel"
        private const val URI_SCHEME_MAILTO = "mailto"

        @JvmStatic
        fun newInstance(employee: Employee) =
            EmployeeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_EMPLOYEE, employee.toString())
                }
            }
    }
}
