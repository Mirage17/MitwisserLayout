package com.davidgrajales.mitwisserlayout

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.davidgrajales.mitwisserlayout.model.Usuario
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.fragment_opciones.*
import java.io.ByteArrayOutputStream

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

        b_logOut.setOnClickListener{
            Firebase.auth.signOut()
            activity?.finish()

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
    private fun guardarImagenEnFirebaseStorage(){
        val database:FirebaseDatabase= FirebaseDatabase.getInstance()
        val myRef:DatabaseReference=database.getReference("usurio")
        val userId= FirebaseAuth.getInstance().uid
        var urlPicture=""
        val mStorage:FirebaseStorage= FirebaseStorage.getInstance()
        val pictureRef=mStorage.reference.child(userId!!)

        iv_userPicture.isDrawingCacheEnabled=true
        iv_userPicture.buildDrawingCache()
        val bitmap=(iv_userPicture.drawable as BitmapDrawable).bitmap
        val baos=ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG,100, baos)
        val data:ByteArray=baos.toByteArray()
        var uploadTask = pictureRef.putBytes(data)
        val urlTask = uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            pictureRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                urlPicture = task.result.toString()

                val childUpdate=HashMap<String,Any>()
                childUpdate["urlPicture"]=urlPicture

                myRef.child(userId).updateChildren(childUpdate)

            } else {
                // Handle failures
                // ...
            }
        }



    }





}