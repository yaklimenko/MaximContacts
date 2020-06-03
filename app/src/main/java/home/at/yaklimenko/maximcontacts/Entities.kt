package home.at.yaklimenko.maximcontacts

import org.json.JSONObject

class AuthCheckResponse(json: String) : JSONObject(json) {
    val message = optString("Message")
    val success = getBoolean("Success")
}

class Department(json: String? = null, jsonObject: JSONObject? = null) {
    var jsonObj: JSONObject

    init {
        jsonObj = json?.let { JSONObject(it) } ?: jsonObject!!
    }

    val id = jsonObj.getInt("ID")
    val name = jsonObj.getString("Name")

    val departments = jsonObj.optJSONArray("Departments")
        ?.let { 0.until(it.length()).map { i -> it.optJSONObject(i) } }
        ?.map { Department(jsonObject = it) }

    val empoyees = jsonObj.optJSONArray("Employees")
        ?.let { 0.until(it.length()).map { i -> it.optJSONObject(i) } }
        ?.map { Employee(jsonObject = it) }

}

class Employee(jsonObject: JSONObject) {
    val id = jsonObject.getInt("ID")
    val name = jsonObject.getString("Name")
    val title = jsonObject.optString("Title")
    val email = jsonObject.optString("Email")
    val phone = jsonObject.optString("Phone")
}