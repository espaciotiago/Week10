package tech.yeswecode.week10.utils

import android.content.Context
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import org.json.JSONObject
import tech.yeswecode.week10.models.User

class VolleyServiceProvider {

    fun get(
        context: Context,
        url: String,
        success: (result: String) -> Unit,
        error: (result: String) -> Unit) {
        val queue = Volley.newRequestQueue(context)
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            {
                success(it)
            },
            {
                error(it.toString())
            })
        queue.add(stringRequest)
    }
}

class UsersServiceProvider {

    val serviceProvider: VolleyServiceProvider

    init {
        serviceProvider = VolleyServiceProvider()
    }

    fun getUsers(
        context: Context,
        url: String,
        success: (result: ArrayList<User>) -> Unit,
        error: (result: String) -> Unit) {

        serviceProvider.get(context, url, {
            var users = ArrayList<User>()
            val jsonObject = JSONObject(it)
            for(key in jsonObject.keys()){
                val itemStr = jsonObject.get(key).toString()
                val user = Gson().fromJson(itemStr, User::class.java)
                users.add(user)
                success(users)
            }
        }) {
            error(it)
        }
    }
}