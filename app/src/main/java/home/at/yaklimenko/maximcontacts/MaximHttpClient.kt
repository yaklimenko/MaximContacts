package home.at.yaklimenko.maximcontacts

import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class MaximHttpClient(
    val login: String,
    val password: String
) {
    val helloPath = "Contacts.svc/Hello"

    fun sendGetRequest(path: String) {

        var auth = URLEncoder.encode("login", "UTF-8") + "=" + URLEncoder.encode(login, "UTF-8")
        auth += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(
            password,
            "UTF-8"
        )

        val mURL = URL("https://contact.taxsee.com/$helloPath/?$auth")

        with(mURL.openConnection() as HttpURLConnection) {
            // optional default is GET
            requestMethod = "GET"

            Log.d("HttpClient", "URL : $url")
            Log.d("HttpClient", "Response Code : $responseCode")

            BufferedReader(InputStreamReader(inputStream)).use {
                val response = StringBuffer()

                var inputLine = it.readLine()
                while (inputLine != null) {
                    response.append(inputLine)
                    inputLine = it.readLine()
                }
                it.close()
                Log.d("HttpClient", "Response : $response")
            }
        }
    }
}