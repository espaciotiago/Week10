package tech.yeswecode.week10

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import tech.yeswecode.week10.databinding.ActivityMainBinding
import tech.yeswecode.week10.models.User
import tech.yeswecode.week10.models.Notes
import tech.yeswecode.week10.utils.Constants
import tech.yeswecode.week10.utils.HTTPSWebUtilDomi
import tech.yeswecode.week10.utils.UsersServiceProvider
import tech.yeswecode.week10.utils.VolleyServiceProvider
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.mockUserBtn.setOnClickListener {
            mockUser("Alfa")
        }

        binding.mockNotesBtn.setOnClickListener {
            mockNotes("Beta", "Lorem ipsum")
        }

        binding.getNotesBtn.setOnClickListener {
            getNotesOf("Beta")
        }

        binding.getUsersBtn.setOnClickListener {
            //getUsers()
            //getUsersWithVolley()
            getUsersWithProvider()
        }
    }

    private fun mockUser(userName: String) {
        val user = User(UUID.randomUUID().toString(), userName, Date().time)
        val json = Gson().toJson(user)

        lifecycleScope.launch(Dispatchers.IO) {
            val url = "${Constants.BASE_URL}/users/${user.name}.json"
            HTTPSWebUtilDomi().PUTRequest(url, json)
        }
    }

    private fun mockNotes(userName: String, note: String) {
        val notes = Notes(UUID.randomUUID().toString(),
            note,
            Date().time,
            userName)
        val json = Gson().toJson(notes)
        lifecycleScope.launch(Dispatchers.IO) {
            val url = "${Constants.BASE_URL}/notes/${userName}.json"
            HTTPSWebUtilDomi().POSTRequest(url, json)
        }
    }

    private fun getNotesOf(userName: String) {
        binding.textView.text = ""
        lifecycleScope.launch(Dispatchers.IO) {
            val url = "${Constants.BASE_URL}/notes/${userName}.json"
            val response = HTTPSWebUtilDomi().GETRequest(url)
            val jsonObject = JSONObject(response)

            for(key in jsonObject.keys()){
                val itemStr = jsonObject.get(key).toString()
                val notes = Gson().fromJson(itemStr, Notes::class.java)
                withContext(Dispatchers.Main) {
                    binding.textView.append(notes.toString())
                }
            }
        }
    }

    private fun getUsers() {
        binding.textView.text = ""
        lifecycleScope.launch(Dispatchers.IO) {
            val url = "${Constants.BASE_URL}/users.json"
            val response = HTTPSWebUtilDomi().GETRequest(url)
            val jsonObject = JSONObject(response)

            for(key in jsonObject.keys()){
                val itemStr = jsonObject.get(key).toString()
                val user = Gson().fromJson(itemStr, User::class.java)
                withContext(Dispatchers.Main) {
                    binding.textView.append(user.toString())
                }
            }
        }
    }

    private fun getUsersWithVolley() {
        binding.textView.text = ""
        val url = "${Constants.BASE_URL}/users.json"
        VolleyServiceProvider().get(
            this,
            url,
            success = {
                val jsonObject = JSONObject(it)
                for(key in jsonObject.keys()){
                    val itemStr = jsonObject.get(key).toString()
                    val user = Gson().fromJson(itemStr, User::class.java)
                    binding.textView.append(user.toString())
                }
            }
        ) {
            binding.textView.append(it)
        }
    }

    private fun getUsersWithProvider() {

        val successCallback = { users: ArrayList<User> ->
            for(user in users) {
                binding.textView.append(user.toString())
            }
        }

        val errorCallback = { error: String ->
            binding.textView.append(error)
        }

        binding.textView.text = ""
        val url = "${Constants.BASE_URL}/users.json"
        UsersServiceProvider().getUsers(this, url, successCallback, errorCallback)
    }
}