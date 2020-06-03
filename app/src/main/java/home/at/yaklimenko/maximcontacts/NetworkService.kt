package home.at.yaklimenko.maximcontacts

import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class NetworkService(

) {
    lateinit var auth: String
    val domain = "https://contact.taxsee.com/"

    fun sendCheckAuthRequest(login: String, password: String): Boolean {
        val path = "Contacts.svc/Hello"
        val checkAuth = "login=$login&password=$password"

        val url = URL("$domain$path?$checkAuth")
        val authRes: AuthCheckResponse
        with(url.openConnection() as HttpURLConnection) {
            // optional default is GET
            requestMethod = "GET"

            Log.d("HttpClient", "URL : $url")
            Log.d("HttpClient", "Response Code : $responseCode")

            val response = readResponse()

            authRes = AuthCheckResponse(String(response))
            Log.d("HttpClient", "authRes : ${authRes.success}")

        }
        if (authRes.success) auth = checkAuth
        return authRes.success
    }

    private fun HttpURLConnection.readResponse(): StringBuffer {
        val response = StringBuffer()
        BufferedReader(InputStreamReader(inputStream)).use {

            var inputLine = it.readLine()
            while (inputLine != null) {
                response.append(inputLine)
                inputLine = it.readLine()
            }
            it.close()
            Log.d("HttpClient", "Response : $response")
        }
        return response
    }


    fun sendGetAllRequest(): String {
        val path = "Contacts.svc/GetAll"
        val url = URL("$domain$path?$auth")
        val allRes: String
        with(url.openConnection() as HttpURLConnection) {
            // optional default is GET
            requestMethod = "GET"

            Log.d("HttpClient", "URL : $url")
            Log.d("HttpClient", "Response Code : $responseCode")

            val response = readResponse()

            allRes = String(response)

        }
        return allRes
    }


}

