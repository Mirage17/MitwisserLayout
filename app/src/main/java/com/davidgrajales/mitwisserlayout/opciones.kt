package com.davidgrajales.mitwisserlayout

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Picture
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.PermissionChecker.checkSelfPermission

import kotlinx.android.synthetic.main.fragment_opciones.*

class opciones : Fragment() {

    private val ALL_PERMISSIONS_RESULT = 107
    private val IMAGE_RESULT = 200
    private val REQUEST_IMAGE_CAPTURE = 12345



    var mBitmap: Bitmap? = null
    val permission = 1


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_opciones, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        b_ChangeImagePic.setOnClickListener {

            pickImageFromGallery()


        }
    }

    private fun permissionInManifest(context: Context,permissionImage:String):Boolean{
        val packageName=context.packageName
        try {
            val packageInfo=context.packageManager
                .getPackageInfo(packageName,PackageManager.GET_PERMISSIONS)
            val declaredPermisisons = packageInfo.requestedPermissions
            if (declaredPermisisons != null && declaredPermisisons.size > 0) {
                for (p in declaredPermisisons) {
                    if (p == permissionImage) {
                        return true
                    }
                }
            }
        }catch (e:PackageManager.NameNotFoundException){

        }
        return false
    }

    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }
    companion object {
        //image pick code
        private val IMAGE_PICK_CODE = 1000;
        //Permission code
        private val PERMISSION_CODE = 1001;
    }

    private fun backFragment() {
        val manager = (context as AppCompatActivity).supportFragmentManager
        manager.popBackStackImmediate()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data !=null) {
            if (requestCode == REQUEST_IMAGE_CAPTURE || requestCode== IMAGE_PICK_CODE) {
                // Get image URI From intent
                var imageUri = data.data
                // do something with the image URI
                iv_userPicture.setImageURI(imageUri)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }





}