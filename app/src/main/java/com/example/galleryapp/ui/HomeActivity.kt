package com.example.galleryapp.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.galleryapp.databinding.ActivityHomeBinding
import com.example.galleryapp.models.Image
import com.example.galleryapp.utils.Global.VOLLEY_TIMEOUT
import com.example.galleryapp.Adapters.GridViewAdapter
import com.example.galleryapp.R
import com.example.galleryapp.Web.VolleySingleton
import com.example.galleryapp.Web.WebServices
import java.net.HttpURLConnection
import java.net.URL


private const val MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1

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

        actionViews()


    }


    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        super.dispatchTouchEvent(event)
        val x = event.x.toInt()
        val y = event.y.toInt()
        binding.pullToRefresh.isEnabled = y <= 1200
        Log.e("yy", "" + y)
        when (event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE, MotionEvent.ACTION_UP -> {
            }
        }
        return false
    }


    private fun actionViews() {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val width = displayMetrics.widthPixels

        getImagesApi()
        getImagesApi()

        binding.pullToRefresh.setOnRefreshListener {
            actionViews()
            binding.pullToRefresh.isRefreshing = false
        }
        binding.gridView.columnWidth = width / 5

        binding.delete.setOnClickListener {
            val temp = ArrayList<Image>()
            for (i in 0 until positions.size) {
                temp.add(imagesList[positions[i]])
            }
            imagesList.removeAll(temp)
            positions.clear()
            gridViewAdapter.notifyDataSetChanged()
            binding.constraint.visibility = View.GONE
            if (imagesList.size == 0) {
                binding.image.visibility = View.VISIBLE
                binding.text.visibility = View.VISIBLE
            }

        }

        binding.share.setOnClickListener {
            positions.clear()
            var ifAllSelected = false
            for (i in 0 until imagesList.size) {
                if (!imagesList[i].isSelected) {
                    ifAllSelected = true
                }
                positions.add(i)
                imagesList[i].isSelected = true
            }

            if (!ifAllSelected)
                for (i in 0 until imagesList.size) {
                    imagesList[i].isSelected = false
                    positions.clear()
                    Log.d(TAG, "actionViewsa: x")
                }

            gridViewAdapter.notifyDataSetChanged()

        }


    }

    private fun getImagesApi() {

        Log.d(
            TAG,
            "getImagesApi: URL " + WebServices.BASE_URL + resources.getString(R.string.PIXABAY_KEY)
        )
        val jsonObjectRequest = object : JsonObjectRequest(
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

                        imagesList.add(0, Image(urls, id.toString()))
                    }

                    binding.image.visibility = View.GONE
                    binding.text.visibility = View.GONE

                    gridViewAdapter =
                        GridViewAdapter(
                            this,
                            imagesList,
                            this
                        )
                    binding.gridView.adapter = gridViewAdapter

                } catch (ex: Exception) {
                    Log.e(TAG, "getImagesApi: ", ex)
                }
            },
            Response.ErrorListener { error ->

                try {
//                    binding.emptyState.cl.visibility = View.VISIBLE
                    binding.image.visibility = View.VISIBLE
                    binding.text.visibility = View.VISIBLE
                    Log.e(TAG, "getImagesApi: error ", error)
                } catch (ex: Exception) {
                    Log.e(TAG, "getStoresCategories: ", ex)
                }

            }
        ) {

        }


        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            VOLLEY_TIMEOUT,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        VolleySingleton.getInstance(applicationContext).addToRequestQueue(jsonObjectRequest)
    }

    private fun init(): Bitmap? {

        var urlConnection: HttpURLConnection? = null
        try {
            val uri = URL(imagesList[0].imageUri)
            urlConnection = uri.openConnection() as HttpURLConnection
            val statusCode = urlConnection.responseCode
//            if (statusCode !=
//                HttpStatus.SC_OK) {
//
//            }
            val inputStream = urlConnection.inputStream
            if (inputStream != null) {
                return BitmapFactory.decodeStream(inputStream)
            }
        } catch (e: java.lang.Exception) {
            urlConnection!!.disconnect()
            Log.w("ImageDownloader", "Error downloading image from")
        } finally {
            urlConnection?.disconnect()
        }
        return null


    }

    override fun onPicClick(position: Int, imgUri : String) {
        Log.d(TAG, "onPicClick: $position")
        Log.d(TAG, "onPicClick: path $imgUri")

        val intent = Intent(this, ImageActivity::class.java).apply {
            putExtra("uri", imgUri)
        }
        startActivity(intent)
    }

    override fun onPicClick(position: Int) {
        Log.d(TAG, "onPicClick: $position")
    }

    override fun onPicLongPress(position: Int, isSelected: Boolean) {
        if (positions.contains(position)) {
            positions.remove(position)
        } else {
            positions.add(position)
        }

        if (positions.size > 0) {
            binding.constraint.visibility = View.VISIBLE
        } else
            binding.constraint.visibility = View.GONE
        Log.d(TAG, "onPicLongPress: $positions.size")
    }


}