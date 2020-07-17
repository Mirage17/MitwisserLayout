package com.davidgrajales.mitwisserlayout

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import com.davidgrajales.mitwisserlayout.model.ChatMessage
import com.davidgrajales.mitwisserlayout.model.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.chat_row1.view.text
import kotlinx.android.synthetic.main.fragment_opciones.*


class chat : AppCompatActivity() {
    //val otherOne= intent.getParcelableExtra<Usuario>(ChatList.USER_KEY)

    private val ALL_PERMISSIONS_RESULT = 107
    private val IMAGE_RESULT = 200
    private val REQUEST_IMAGE_CAPTURE = 12345

    val adapter = GroupAdapter<ViewHolder>()

    var mBitmap: Bitmap? = null
    val permission = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        rv_chat.adapter=adapter

        ListeningMesages()

        bt_sendMessage.setOnClickListener{

            SendMessage()

        }

    }

    private  fun SendMessage(){
            val otherOne=intent.getParcelableExtra<Usuario>(ChatList.USER_KEY)
            val database:FirebaseDatabase= FirebaseDatabase.getInstance()
            val date= System.currentTimeMillis()/1000
            val fromId=FirebaseAuth.getInstance().uid
            val myRef: DatabaseReference =database.getReference("messages").push()
            val toId:String? = otherOne?.id
            val messageId:String?=myRef.push().key
            val message=et_message.text.toString()
            val chatMessage= ChatMessage(
                messageId,
                fromId,
                toId,
                date,
                message)
            myRef.setValue(chatMessage).addOnSuccessListener {

            }

    }
    private fun ListeningMesages(){
        val database= FirebaseDatabase.getInstance()
        val ref=database.getReference("messages")
        ref.addChildEventListener(object : ChildEventListener{
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage=snapshot.getValue(ChatMessage::class.java)
                if(et_message!=null){
                    if(chatMessage!!.fromID==FirebaseAuth.getInstance().uid){
                        adapter.add(ChatItemFrom(chatMessage!!.text))
                    }else{
                        adapter.add(ChatItemTo(chatMessage!!.text))
                    }


                }


            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
            }

        })
    }




    private fun ShowDummyChatExample() {
        val adapter = GroupAdapter<ViewHolder>()
        adapter.add(ChatItemFrom("este es el texto que pone \n la persona que envia el\n mensaje desde "))
        adapter.add(ChatItemTo("y aquí aparece el \ntexto de la persona al otro\n lado de la linea desde el" +
                "\npunto de vista del"))




        rv_chat.adapter = adapter
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
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }
    companion object {
        //image pick code
        private val IMAGE_PICK_CODE = 1000;
        //Permission code
        private val PERMISSION_CODE = 1001;
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





class ChatItemFrom(val words:String): Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
    viewHolder.itemView.text.text=words
    }

    override fun getLayout(): Int {
        return R.layout.chat_row1
    }
}
class ChatItemTo(val words: String) : Item<ViewHolder>() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
    viewHolder.itemView.text.text=words
    }

    override fun getLayout(): Int {
        return R.layout.chat_row2
    }

}