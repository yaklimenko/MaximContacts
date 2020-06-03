package home.at.yaklimenko.maximcontacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


private const val ARG_DEPARTMENT = "department"

class DepartmentFragment : Fragment(), CoroutineScope {

    private lateinit var job: Job

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job


    private var department: Department? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            department = Department(it.getString(ARG_DEPARTMENT))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        job = Job()

        if (department == null) {
            launch {
                val network = NetworkService()
                withContext(Dispatchers.IO) {
                    network.sendCheckAuthRequest("test_user", "test_pass")
                    department = Department(network.sendGetAllRequest())
                }
                val zu = ""
            }
        }

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_department, container, false)
    }

    override fun onDestroyView() {
        job.cancel()
        super.onDestroyView()
    }

    companion object {
        val TAG = DepartmentFragment::class.simpleName

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.

         * @return A new instance of fragment ContactListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(department: Department) =
            DepartmentFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_DEPARTMENT, department.toString())
                }
            }
    }
}
