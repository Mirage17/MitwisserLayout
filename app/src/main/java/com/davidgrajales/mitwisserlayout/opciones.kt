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
import androidx.fragment.app.Fragment
import com.davidgrajales.mitwisserlayout.chatStuff.chat
import com.davidgrajales.mitwisserlayout.model.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_opciones.*
import java.io.ByteArrayOutputStream

class opciones : Fragment() {

    private val ALL_PERMISSIONS_RESULT = 107
    private val IMAGE_RESULT = 200
    private val REQUEST_IMAGE_CAPTURE = 12345

    companion object {
        //image pick code
        private val IMAGE_PICK_CODE = 1000

        //Permission code
        private val PERMISSION_CODE = 1001
        var usuarioActual: Usuario? = null
        val TIME2LIVE = "TIME_TO_LIVE"

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        obtenerUsuarioActual()
        return inflater.inflate(R.layout.fragment_opciones, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        b_ChangeImagePic.setOnClickListener {
            originalImageDatabase(usuarioActual?.id)
            pickImageFromGallery()
        }

        b_logOut.setOnClickListener {
            Firebase.auth.signOut()
            activity?.finish()
        }

        b_SaveTimeToLive.setOnClickListener {
            var timeToLive = s_time2live.selectedItem.toString()
            val intent = Intent(
                context,
                chat::class.java
            )
            intent.putExtra(TIME2LIVE, timeToLive)
        }


    }

    private fun originalImageDatabase(userId: String?) {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("usuarios")
        var userThereIs = false
        val postListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {

                for (datasnapshot: DataSnapshot in snapshot.children) {
                    val user = datasnapshot.getValue(Usuario::class.java)
                    if (user?.id == userId) {
                        userThereIs = true
                        //mostrarurl(user!!.urlPicture)

                    }

                }
                if (!userThereIs) {
                    // Toast.makeText(requireContext(),user?.id, Toast.LENGTH_LONG).show()
                }
            }
        }
        myRef.addValueEventListener(postListener)
    }


    private fun permissionInManifest(context: Context, permissionImage: String): Boolean {
        val packageName = context.packageName
        try {
            val packageInfo = context.packageManager
                .getPackageInfo(packageName, PackageManager.GET_PERMISSIONS)
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

    private fun obtenerUsuarioActual() {
        val id = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("usuario/$id")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                usuarioActual = snapshot.getValue(Usuario::class.java)
                if (usuarioActual?.urlPicture != null) {
                    Picasso.get().load(usuarioActual?.urlPicture).into(iv_userPicture)

                }

            }

        })
    }

    var imageUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == REQUEST_IMAGE_CAPTURE || requestCode == IMAGE_PICK_CODE) {
                // Get image URI From intent
                imageUri = data.data
                // do something with the image URI
                iv_userPicture.setImageURI(imageUri)
                guardarImagenEnFirebaseStorage()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
    private fun guardarImagenEnFirebaseStorage(){
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val myRef: DatabaseReference = database.getReference("usuario")
        val userId = FirebaseAuth.getInstance().uid
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

            }
        }



    }



}