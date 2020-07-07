package com.davidgrajales.mitwisserlayout

import android.app.Application
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.room.Room
import com.davidgrajales.mitwisserlayout.model.UsuarioDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item

import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_chat_list.*


class ChatList : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter=GroupAdapter<ViewHolder>()
        adapter.add(UserItem())
        adapter.add(UserItem())
        adapter.add(UserItem())
        adapter.add(UserItem())
        adapter.add(UserItem())




        rv_m_list.adapter=adapter

        adapter.setOnItemClickListener{item, view ->
            val intent =Intent(this@ChatList.context,chat::class.java)
            startActivity(intent)
        }


    }


}

class UserItem:Item<ViewHolder>(){
    override fun getLayout(): Int {
    return R.layout.messages_list_unit
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {

    }

}
