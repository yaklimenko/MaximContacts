package home.at.yaklimenko.maximcontacts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.list_item_department.view.*

class ContactListAdapter(
    val contacts: Array<Contact>,
    val inflater: LayoutInflater,
    val onClickListener: View.OnClickListener
) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: inflater.inflate(R.layout.list_item_department, parent, false)
        val contact = contacts[position]
        view.department_name.text = contact.name
        view.tag = contact
        view.setOnClickListener(onClickListener)
        return view
    }

    override fun getItem(position: Int): Any {
        return contacts[position]
    }

    override fun getItemId(position: Int): Long {
        return contacts[position].id.toLong()
    }

    override fun getCount(): Int {
        return contacts.size
    }

}