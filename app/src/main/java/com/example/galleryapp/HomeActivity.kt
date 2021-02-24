package com.example.galleryapp

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.example.galleryapp.databinding.ActivityHomeBinding
import com.example.galleryapp.models.Image
import com.example.galleryapp.utils.Global.VOLLEY_TIMEOUT
import com.example.galleryapp.utils.GridViewAdapter
import com.example.galleryapp.utils.VolleySingleton
import com.example.galleryapp.utils.WebServices
import com.squareup.picasso.Picasso
import com.squareup.picasso.Picasso.LoadedFrom
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


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
        init()

        for (i in imagesList) {
            Log.d(TAG, "onCreate: " + i.imageUri)
        }
        Log.d(TAG, "onCreate: " + imagesList.size)
//        if (ContextCompat.checkSelfPermission(
//                this,
//                Manifest.permission.READ_EXTERNAL_STORAGE
//            )
//            != PackageManager.PERMISSION_GRANTED
//        ) ActivityCompat.requestPermissions(
//            this, arrayOf(
//                Manifest.permission.READ_EXTERNAL_STORAGE,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE
//            ),
//            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
//        )


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
            "getImagesApi: URL " + WebServices.BASE_URL + resources.getString(R.string.ACCESS_KEY)
        )
        val JsonArrayRequest = object : JsonArrayRequest(
            Method.GET,
            WebServices.BASE_URL + resources.getString(R.string.ACCESS_KEY),
            null,
            Response.Listener { response ->

                try {

                    for (i in 0 until response.length()) {
                        val obj = response.getJSONObject(i)
                        val urls = obj.getJSONObject("urls")
                        urls.getString("raw")
                        imagesList.add(Image(urls.getString("full")))
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


        JsonArrayRequest.retryPolicy = DefaultRetryPolicy(
            VOLLEY_TIMEOUT,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        VolleySingleton.getInstance(applicationContext).addToRequestQueue(JsonArrayRequest)
    }

    private fun init() {


//        val uri: Uri
//        val cursor: Cursor
//        val column_index_data: Int
//        val column_index_folder_name: Int
//        val listOfAllImages = ArrayList<String?>()
//        var absolutePathOfImage: String? = null
//        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
//
//        val projection = arrayOf(
//            MediaColumns.DATA,
//            MediaStore.Images.Media.BUCKET_DISPLAY_NAME
//        )
//
//        cursor = this.contentResolver.query(
//            uri, projection, null,
//            null, null
//        )!!
//
//        column_index_data = cursor.getColumnIndexOrThrow(MediaColumns.DATA)
//        column_index_folder_name = cursor
//            .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
//        while (cursor.moveToNext()) {
//            Log.d(TAG, "init: inside loop ")
//            absolutePathOfImage = cursor.getString(column_index_data)
//            imagesList.add(Image(absolutePathOfImage))
//        }
//
//        binding.gridView.adapter = GridViewAdapter(this, imagesList)
//        val projection = arrayOf(MediaColumns.DATA)
//
//        val cursor = contentResolver.query(
//            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//            projection,
//            null,
//            null,
//            null
//        )
//        while (cursor!!.moveToNext()) {
//            val absolutePathOfImage = cursor.getString(cursor.getColumnIndex(MediaColumns.DATA))
//            val ImageModel = ImageModel()
//            ImageModel.setImage(absolutePathOfImage)
//            imageList.add(ImageModel)
//            imagesList.add(Image(absolutePathOfImage))
//        }
//        cursor.close()
//        Log.d(TAG, "init: outside loop ")
//        binding.gridView.adapter = GridViewAdapter(this, imagesList)

//        imagesList.add(Image(R.drawable.ic_delete))
//        imagesList.add(Image(R.drawable.ic_delete))
//        imagesList.add(Image(R.drawable.ic_delete))
//        imagesList.add(Image(R.drawable.ic_delete))
//        imagesList.add(Image(R.drawable.ic_delete))
//        imagesList.add(Image(R.drawable.ic_delete))
//        imagesList.add(Image(R.drawable.ic_delete))
//        imagesList.add(Image(R.drawable.ic_delete))
//        imagesList.add(Image(R.drawable.ic_delete))
//        imagesList.add(Image(R.drawable.ic_delete))
//        imagesList.add(Image(R.drawable.ic_delete))
//        imagesList.add(Image(R.drawable.ic_delete))
//        imagesList.add(Image(R.drawable.ic_delete))
//        imagesList.add(Image(R.drawable.ic_delete))
//        imagesList.add(Image(R.drawable.ic_delete))
//        imagesList.add(Image(R.drawable.ic_delete))
//        imagesList.add(Image(R.drawable.ic_delete))
//        imagesList.add(Image(R.drawable.ic_delete))
//        imagesList.add(Image(R.drawable.ic_delete))
//        imagesList.add(Image(R.drawable.ic_delete))
//        imagesList.add(Image(R.drawable.ic_delete))
//        imagesList.add(Image(R.drawable.ic_delete))
//        imagesList.add(Image(R.drawable.ic_delete))
//        imagesList.add(Image(R.drawable.ic_delete))
//        imagesList.add(Image(R.drawable.ic_delete))
//        imagesList.add(Image(R.drawable.ic_delete))
//        imagesList.add(Image(R.drawable.ic_delete))
//        imagesList.add(Image(R.drawable.ic_delete))
//        imagesList.add(Image(R.drawable.ic_delete))
//        imagesList.add(Image(R.drawable.ic_delete))
//        imagesList.add(Image(R.drawable.ic_delete))
//        imagesList.add(Image(R.drawable.ic_delete))
//        imagesList.add(Image(R.drawable.ic_delete))
//        imagesList.add(Image(R.drawable.ic_delete))
//        imagesList.add(Image(R.drawable.ic_delete))
//        imagesList.add(Image(R.drawable.ic_delete))
//        imagesList.add(Image(R.drawable.ic_delete))
//        imagesList.add(Image(R.drawable.ic_delete))
//        imagesList.add(Image(R.drawable.ic_delete))
//        imagesList.add(Image(R.drawable.ic_delete))
//        imagesList.add(Image(R.drawable.ic_delete))
//        imagesList.add(Image(R.drawable.ic_delete))
//        imagesList.add(Image(R.drawable.ic_delete))
//        imagesList.add(Image(R.drawable.ic_delete))
//        imagesList.add(Image(R.drawable.ic_delete))
//        imagesList.add(Image(R.drawable.ic_delete))
//        imagesList.add(Image(R.drawable.ic_delete))
//        imagesList.add(Image(R.drawable.ic_delete))
//        imagesList.add(Image(R.drawable.ic_delete))
//        imagesList.add(Image(R.drawable.ic_delete))
//        imagesList.add(Image(R.drawable.ic_delete))
//        imagesList.add(Image(R.drawable.ic_delete))
//        imagesList.add(Image(R.drawable.ic_delete))
//        imagesList.add(Image(R.drawable.ic_delete))
//        imagesList.add(Image(R.drawable.ic_delete))
//        imagesList.add(Image(R.drawable.ic_delete))
//        imagesList.add(Image(R.drawable.ic_delete))
//        imagesList.add(Image(R.drawable.ic_delete))
//        imagesList.add(Image(R.drawable.ic_delete))
//        imagesList.add(Image(R.drawable.ic_delete))
//        imagesList.add(Image(R.drawable.ic_delete))
//        imagesList.add(Image(R.drawable.ic_delete))
//        imagesList.add(Image(R.drawable.ic_delete))
//        imagesList.add(Image(R.drawable.ic_delete))
//        imagesList.add(Image(R.drawable.ic_delete))
//        imagesList.add(Image(R.drawable.ic_delete))
//        imagesList.add(Image(R.drawable.ic_delete))
//        imagesList.add(Image(R.drawable.ic_delete))
//        imagesList.add(Image(R.drawable.ic_delete))
//        imagesList.add(Image(R.drawable.ic_delete))
//        imagesList.add(Image(R.drawable.ic_delete))
//        imagesList.add(Image(R.drawable.ic_delete))
//        imagesList.add(Image(R.drawable.ic_delete))
//        imagesList.add(Image(R.drawable.ic_delete))
//        imagesList.add(Image(R.drawable.ic_delete))
//        imagesList.add(Image(R.drawable.ic_delete))
//        imagesList.add(Image(R.drawable.ic_delete))
//        imagesList.add(Image(R.drawable.ic_delete))
//        imagesList.add(Image(R.drawable.ic_delete))
//        imagesList.add(Image(R.drawable.ic_delete))
//        imagesList.add(Image(R.drawable.ic_delete))
//        imagesList.add(Image(R.drawable.ic_delete))
//        imagesList.add(Image(R.drawable.ic_delete))
//        imagesList.add(Image(R.drawable.ic_delete))
//        imagesList.add(Image(R.drawable.ic_delete))
//        imagesList.add(Image(R.drawable.ic_delete))
//        imagesList.add(Image(R.drawable.ic_delete))
//        imagesList.add(Image(R.drawable.ic_delete))
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