package com.davidgrajales.mitwisserlayout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.os.Parcel
import android.os.Parcelable
import com.davidgrajales.mitwisserlayout.model.ChatMessage
import com.davidgrajales.mitwisserlayout.model.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.chat_row1.view.*
import kotlinx.android.synthetic.main.chat_row1.view.text
import kotlinx.android.synthetic.main.chat_row2.view.*
import kotlinx.android.synthetic.main.messages_list_unit.view.*


class chat : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        //val user=intent.getParcelableArrayExtra<Usuario>[ChatList,USER_KEY]

        ShowDummyChatExample()

        bt_sendMessage.setOnClickListener{

            SendMessage()

        }

    }

    private  fun SendMessage(){


            val fromId=FirebaseAuth.getInstance().uid
            //val toId=
            val messageReference=FirebaseDatabase
                .getInstance().getReference("messages").push()
            val message=et_message.text.toString()
            val chatMessage= ChatMessage(message)
            messageReference.setValue(chatMessage).addOnSuccessListener {  }


    }

    private fun ShowDummyChatExample() {
        val adapter = GroupAdapter<ViewHolder>()
        adapter.add(ChatItemFrom("este es el texto que pone \n la persona que envia el\n mensaje desde "))
        adapter.add(ChatItemTo("y aquí aparece el \ntexto de la persona al otro\n lado de la linea desde el" +
                "\npunto de vista del"))




        rv_chat.adapter = adapter
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