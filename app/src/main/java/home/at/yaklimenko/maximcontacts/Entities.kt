package home.at.yaklimenko.maximcontacts

import org.json.JSONObject

class AuthCheckResponse(json: String) : JSONObject(json) {
    val message = optString("Message")
    val success = getBoolean("Success")
}

abstract class Contact {
    abstract val id: Int
    abstract val name: String
}

class Department(json: String? = null, jsonObject: JSONObject? = null) : Contact() {
    var jsonObj: JSONObject = json?.let { JSONObject(it) } ?: jsonObject!!
    override val id = jsonObj.getString("ID").toInt()
    override val name = jsonObj.getString("Name")

    private val departments = jsonObj.optJSONArray("Departments")
        ?.let { 0.until(it.length()).map { i -> it.optJSONObject(i) } }
        ?.map {
            Department(jsonObject = it)
        }

    private val empoyees = jsonObj.optJSONArray("Employees")
        ?.let { 0.until(it.length()).map { i -> it.optJSONObject(i) } }
        ?.map { Employee(jsonObject = it) }

    val contacts: Array<Contact>
        get() {
            return (departments?.toList().orEmpty() + empoyees?.toList().orEmpty())
                .toTypedArray()
        }

    override fun toString(): String {
        return jsonObj.toString()
    }
}

class Employee(val jsonObject: JSONObject) : Contact() {
    override val id = jsonObject.getString("ID").toInt()
    override val name = jsonObject.getString("Name")
    val title = jsonObject.optString("Title")
    val email = jsonObject.optString("Email")
    val phone = jsonObject.optString("Phone")

    override fun toString(): String {
        return jsonObject.toString()
    }
}