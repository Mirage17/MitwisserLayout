package com.davidgrajales.mitwisserlayout.chatStuff

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.davidgrajales.mitwisserlayout.R
import com.davidgrajales.mitwisserlayout.model.ChatMessage
import com.davidgrajales.mitwisserlayout.model.Multimedia
import com.davidgrajales.mitwisserlayout.model.Usuario
import com.davidgrajales.mitwisserlayout.opciones
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.chat_row1.view.*
import kotlinx.android.synthetic.main.picture_row_from.view.*
import java.io.ByteArrayOutputStream


class chat : AppCompatActivity() {

    companion object {
        //image pick code
        private val IMAGE_PICK_CODE = 1000

        //Permission code
        private val PERMISSION_CODE = 1001
        var usuarioActual: Usuario? = null

    }

    //val otherOne= intent.getParcelableExtra<Usuario>(ChatList.USER_KEY)

    //private val ALL_PERMISSIONS_RESULT = 107
    //private val IMAGE_RESULT = 200
    private val REQUEST_IMAGE_CAPTURE = 12345

    val adapter = GroupAdapter<ViewHolder>()

    var mBitmap: Bitmap? = null
    val permission = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        //window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        obtenerUsuarioActual()
        rv_chat.adapter = adapter
        ListeningMesages()
        ListenigPictures()
        SendTime2Live2Firebase()

        bt_sendMessage.setOnClickListener {

            SendMessage()
        }
        ib_sendPictures.setOnClickListener {
            pickImageFromGallery()


        }

    }

    private fun SendTime2Live2Firebase() {

        var timeToLive = intent.getStringExtra(opciones.TIME2LIVE)
        Log.d("El tiempo de vida es", timeToLive.toString())

    }


    private fun obtenerUsuarioActual() {
        val id = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("usuario/$id")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                usuarioActual = snapshot.getValue(Usuario::class.java)
            }

        })
    }

    private fun SentPic2Firebase() {
        val otherOne = intent.getParcelableExtra<Usuario>(ChatList.USER_KEY)
        val fromId = FirebaseAuth.getInstance().uid
        val toId = otherOne?.id
        val databasepic: FirebaseDatabase = FirebaseDatabase.getInstance()
        val myRefpic: DatabaseReference = databasepic.getReference("user-pictures/$fromId/$toId")
        val yourRefpic: DatabaseReference = databasepic.getReference("user-pictures/$toId/$fromId")
        val idPic = myRefpic.push().key
        val date = System.currentTimeMillis() / 1000
        var urlPhoto = ""

        val mStorage: FirebaseStorage = FirebaseStorage.getInstance()
        val photoRef = mStorage.reference.child(idPic!!)
        // Get the data from an ImageView as bytes
        iv_storagepick.isDrawingCacheEnabled = true
        iv_storagepick.buildDrawingCache()
        val bitmap = (iv_storagepick.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        var uploadTask = photoRef.putBytes(data)
        val urlTask = uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            photoRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                urlPhoto = task.result.toString()

                val multimedia = Multimedia(
                    idPic,
                    fromId,
                    toId,
                    date,
                    urlPhoto
                )

                //Log.d("pic id",idPic.toString())
                myRefpic.child(idPic).setValue(multimedia)
                yourRefpic.child(idPic).setValue(multimedia)

            } else {
                Toast.makeText(this, "Foto no subida", Toast.LENGTH_SHORT).show()
            }
        }



    }

    private fun SendMessage() {
        val otherOne = intent.getParcelableExtra<Usuario>(ChatList.USER_KEY)
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val date = System.currentTimeMillis() / 1000
        val fromId = FirebaseAuth.getInstance().uid
        val toId: String? = otherOne?.id
        val myRef: DatabaseReference = database.getReference("/user-messages/$fromId/$toId").push()
        val youRef: DatabaseReference = database.getReference("/user-messages/$toId/$fromId").push()
        val messageId: String? = myRef.push().key
        val message = et_message.text.toString()
        val chatMessage = ChatMessage(
            messageId,
            fromId,
            toId,
            date,
            message
        )
        myRef.setValue(chatMessage).addOnSuccessListener {
            et_message.text.clear()
        }
        youRef.setValue(chatMessage)

    }

    private fun ListenigPictures() {

        val otherOne = intent.getParcelableExtra<Usuario>(ChatList.USER_KEY)
        val fromId = FirebaseAuth.getInstance().uid
        val toId: String? = otherOne?.id
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("user-pictures/$fromId/$toId")
        //Log.d("picture id", currentPicture?.id.toString())
        ref.addChildEventListener(object : ChildEventListener {

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatPicture = snapshot.getValue(Multimedia::class.java)
                if (iv_storagepick != null) {
                    if (chatPicture!!.fromID == FirebaseAuth.getInstance().uid) {

                        adapter.add(
                            ChatItemPickFrom(
                                chatPicture,
                                usuarioActual
                            )
                        )
                    } else {
                        adapter.add(
                            ChatItemPickTo(
                                chatPicture,
                                otherOne
                            )
                        )
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }


            override fun onChildRemoved(snapshot: DataSnapshot) {
            }

        })
    }

    private fun ListeningMesages() {
        val otherOne = intent.getParcelableExtra<Usuario>(ChatList.USER_KEY)
        val fromId = FirebaseAuth.getInstance().uid
        val toId: String? = otherOne?.id
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("/user-messages/$fromId/$toId")
        ref.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage=snapshot.getValue(ChatMessage::class.java)
                if(et_message!=null){
                    if(chatMessage!!.fromID==FirebaseAuth.getInstance().uid) {

                        adapter.add(
                            ChatItemFrom(
                                chatMessage.text,
                                usuarioActual
                            )
                        )
                    }else {
                        adapter.add(
                            ChatItemTo(
                                chatMessage.text,
                                otherOne
                            )
                        )
                    }
                }
            }
            override fun onChildRemoved(snapshot: DataSnapshot) {
            }
        })
    }

    private fun permissionInManifest(context: Context, permissionImage:String):Boolean{
        val packageName=context.packageName
        try {
            val packageInfo=context.packageManager
                .getPackageInfo(packageName, PackageManager.GET_PERMISSIONS)
            val declaredPermisisons = packageInfo.requestedPermissions
            if (declaredPermisisons != null && declaredPermisisons.size > 0) {
                for (p in declaredPermisisons) {
                    if (p == permissionImage) {
                        return true
                    }
                }
            }
        }catch (e: PackageManager.NameNotFoundException){

        }
        return false
    }


    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(
            intent,
            IMAGE_PICK_CODE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data !=null) {
            if (requestCode == REQUEST_IMAGE_CAPTURE || requestCode== IMAGE_PICK_CODE) {
                // Get image URI From intent
                var imageUri = data.data
                // do something with the image URI
                iv_storagepick.setImageURI(imageUri)
                SentPic2Firebase()


            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


}


class ChatItemFrom(val words: String, val usuario: Usuario?) : Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.text.text = words
        val uri = usuario?.urlPicture
        val imageSelf = viewHolder.itemView.iv_pic1
        Picasso.get().load(uri).fit().into(imageSelf)

    }

    override fun getLayout(): Int {
        return R.layout.chat_row1
    }
}

class ChatItemTo(val words: String, val usuario: Usuario?) : Item<ViewHolder>() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.text.text = words
        val uri = usuario?.urlPicture
        val imagePartner = viewHolder.itemView.iv_pic1
        Picasso.get().load(uri).fit().into(imagePartner)
    }

    override fun getLayout(): Int {
        return R.layout.chat_row2
    }

}

class ChatItemPickFrom(val multimedia: Multimedia?, val usuario: Usuario?) : Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        val uri = usuario?.urlPicture

        val imageSelf = viewHolder.itemView.iv_userPicture
        Picasso.get().load(uri).fit().into(imageSelf)
        if (multimedia?.url != null)
            Picasso.get().load(multimedia.url).fit().into(viewHolder.itemView.iv_PictureSent)

    }

    override fun getLayout(): Int {
        return R.layout.picture_row_from
    }
}

class ChatItemPickTo(val multimedia: Multimedia?, val usuario: Usuario?) : Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        val uri = usuario?.urlPicture
        val imageYours = viewHolder.itemView.iv_userPicture
        Picasso.get().load(uri).fit().into(imageYours)
        if (multimedia?.url != null)
            Picasso.get().load(multimedia.url).into(viewHolder.itemView.iv_PictureSent)

    }

    override fun getLayout(): Int {
        return R.layout.picture_row_to
    }
}
