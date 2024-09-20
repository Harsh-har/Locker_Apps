package com.example.LockerApp.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.LockerApp.Classes.ButtonClass
import com.example.LockerApp.WasteActivity
import com.example.LockerApp.databinding.ButtontextBinding

class ButtonAdapter (var dataItem:ArrayList<ButtonClass>,var context: Context): RecyclerView.Adapter< ButtonAdapter.MyViewHolder>() {
    inner class MyViewHolder(var binding: ButtontextBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var binding =ButtontextBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataItem.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.name.text = dataItem.get(position).name

        holder.itemView.setOnClickListener {
            val intent = Intent(context, WasteActivity::class.java)

            context?.startActivity(intent)
        }
    }
}