package com.example.rssreader

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rssreader.Adapter.NewsAdapter
import com.example.rssreader.Common.HTTPDataHandler
import com.example.rssreader.models.Json4Kotlin_Base
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolBar.title = "NEWS"
        setSupportActionBar(toolBar)
        val linearLayoutManager = LinearLayoutManager(baseContext, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = linearLayoutManager
        loadNews()

    }

    @SuppressLint("SetTextI18n")
    private fun loadNews() {
        val emptyView: TextView = findViewById(R.id.empty)
        val connMgr: ConnectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connMgr.activeNetworkInfo
        val loadNewsAsync = @SuppressLint("StaticFieldLeak")
        object:AsyncTask<String, String, String>(){
            val mDialog = ProgressDialog(this@MainActivity)
            override fun doInBackground(vararg params: String?): String {
                val result:String
                val http = HTTPDataHandler()
                result = http.GetHTTPDataHandler(params[0])
                return result
            }

            override fun onPreExecute() {
                mDialog.setMessage("Please wait...")
                mDialog.show()
            }

            override fun onPostExecute(result: String?) {
                mDialog.dismiss()
                val newsObject: Json4Kotlin_Base = Gson().fromJson(result, Json4Kotlin_Base::class.java)
                val adapter = NewsAdapter(newsObject, baseContext)
                recyclerView.adapter = adapter
                adapter.notifyDataSetChanged()
            }

        }
        if (networkInfo!= null && networkInfo.isConnected){
            loadNewsAsync.execute(uriBuilder())
            emptyView.visibility = View.GONE
        } else {
            emptyView.text = getString(R.string.no_connection)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.refresh)
            loadNews()

        return true
    }
    private fun uriBuilder():String{
        val urlBuilder = Uri.Builder()
        urlBuilder.scheme("https")
                .authority("content.guardianapis.com")
                .appendPath("search")
                .appendQueryParameter("show-tags", "contributor")
                .appendQueryParameter("api-key", "test")
        return urlBuilder.build().toString()

    }
}