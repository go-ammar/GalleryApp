package com.example.galleryapp

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.example.galleryapp.databinding.ActivityHomeBinding
import com.example.galleryapp.models.Image
import com.example.galleryapp.utils.Global.VOLLEY_TIMEOUT
import com.example.galleryapp.utils.GridViewAdapter
import com.example.galleryapp.utils.VolleySingleton
import com.example.galleryapp.utils.WebServices
import java.net.HttpURLConnection
import java.net.URL


private const val MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1
private const val MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 2


class HomeActivity : AppCompatActivity(), GridViewAdapter.OnClick {


    private lateinit var binding: ActivityHomeBinding
    private var imagesList = ArrayList<Image>()
    private val TAG = "HomeActivity"
    private lateinit var gridViewAdapter: GridViewAdapter
    private var positions = ArrayList<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val width = displayMetrics.widthPixels

        binding.gridView.columnWidth = width / 5
        //init()

        for (i in imagesList) {
            Log.d(TAG, "onCreate: " + i.imageUri)
        }
        Log.d(TAG, "onCreate: " + imagesList.size)
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ),
            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
        )


        binding.delete.setOnClickListener {
            for (i in 0 until positions.size) {
                imagesList[positions[i]].isSelected = false
                imagesList.removeAt(positions[i])
            }
            positions.clear()
            gridViewAdapter.notifyDataSetChanged()
            binding.constraint.visibility = View.GONE
        }

        binding.share.setOnClickListener{

            Log.d(TAG, "onCreate: " + imagesList[positions[0]].imageUri)

        }

        getImagesApi()
        getImagesApi()

    }

    private fun getImagesApi() {

        Log.d(
            TAG,
            "getImagesApi: URL " + WebServices.BASE_URL + resources.getString(R.string.PIXABAY_KEY)
        )
        val JsonObjectRequest = object : JsonObjectRequest(
            Method.GET,
            WebServices.BASE_URL + resources.getString(R.string.PIXABAY_KEY),
            null,
            Response.Listener { response ->

                try {

                    val hitsArray = response.getJSONArray("hits")
                    for (i in 0 until hitsArray.length()) {


                        val ob = hitsArray.getJSONObject(i)
                        val urls = ob.getString("webformatURL")
                        val id = ob.getInt("id")


                        imagesList.add(Image(urls, id.toString()))
                    }
//                    Log.d(TAG, "getImagesApi: $response")


                    for (i in 0 until imagesList.size) {
                        Log.d(TAG, "getImagesApi list is: " + imagesList[i].imageUri)
                    }

                    gridViewAdapter = GridViewAdapter(this, imagesList, this)
                    binding.gridView.adapter = gridViewAdapter

                } catch (ex: Exception) {
                    Log.e(TAG, "getImagesApi: ", ex)
                }
            },
            Response.ErrorListener { error ->

                try {
//                    val data = String(error.networkResponse.data)
                    Log.e(TAG, "getImagesApi: error ", error)
                } catch (ex: Exception) {
                    Log.e(TAG, "getStoresCategories: ", ex)
                }

            }
        ) {

        }


        JsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            VOLLEY_TIMEOUT,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        VolleySingleton.getInstance(applicationContext).addToRequestQueue(JsonObjectRequest)
    }

    private fun init() : Bitmap?{

        var urlConnection: HttpURLConnection? = null
        try {
            val uri = URL(imagesList[0].imageUri)
            urlConnection = uri.openConnection() as HttpURLConnection
            val statusCode = urlConnection.responseCode
//            if (statusCode !=
//                HttpStatus.SC_OK) {
//
//            }
            val inputStream = urlConnection!!.inputStream
            if (inputStream != null) {
                return BitmapFactory.decodeStream(inputStream)
            }
        } catch (e: java.lang.Exception) {
            urlConnection!!.disconnect()
            Log.w("ImageDownloader", "Error downloading image from")
        } finally {
            urlConnection?.disconnect()
        }
        return null;


    }

    override fun onPicClick(position: Int) {
        Log.d(TAG, "onPicClick: $position")
    }

    override fun onPicLongPress(position: Int, isSelected: Boolean) {
//        imagesList.removeAt(position)
//        gridViewAdapter.notifyDataSetChanged()
        if (positions.contains(position)) {
            positions.remove(position)
        } else {
            positions.add(position)
        }

        if (isSelected) {
            binding.constraint.visibility = View.VISIBLE

        } else
            binding.constraint.visibility = View.GONE
        Log.d(TAG, "onPicLongPress: $position")
    }


}