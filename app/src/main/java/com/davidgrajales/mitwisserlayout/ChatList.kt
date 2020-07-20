package com.davidgrajales.mitwisserlayout

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.davidgrajales.mitwisserlayout.model.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_chat_list.*
import kotlinx.android.synthetic.main.messages_list_unit.view.*


class ChatList : Fragment() {

    companion object {
        val USER_KEY = "USER_KEY"
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_chat_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {


                // in here you can do logic when backPress is clicked
            }
        })

        val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
        val user: FirebaseUser? = mAuth.currentUser
        val correo = user?.email

        //ShowMessage("bienvenido $correo")

        fetchUser()

        /*adapter.setOnItemClickListener{item, view ->
            val intent =Intent(this@ChatList.context,chat::class.java)
            startActivity(intent)*/
        }

    private fun fetchUser(){
        val database=FirebaseDatabase.getInstance()
        val ref=database.getReference("usuario")

        val postListener= object:ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val adapter=GroupAdapter<ViewHolder>()
                snapshot.children.forEach {
                    val user=it.getValue(Usuario::class.java)
                    if(user!=null) {
                        adapter.add(UserItem(user))
                    }

                }
                adapter.setOnItemClickListener { item, view ->
                    val userItem=item as UserItem
                    val intent=Intent(context,chat::class.java)
                    intent.putExtra(USER_KEY, userItem.user)
                    startActivity(intent)
                }
                rv_m_list.adapter=adapter
            }

        }


        ref.addListenerForSingleValueEvent(postListener)

    }


}

class UserItem(val user:Usuario):Item<ViewHolder>(){
    override fun getLayout(): Int {
    return R.layout.messages_list_unit
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.tv_userName.text=user.nickName
        Picasso.get().load(user.urlPicture).into(viewHolder.itemView.iv_userPicture)

    }


}


