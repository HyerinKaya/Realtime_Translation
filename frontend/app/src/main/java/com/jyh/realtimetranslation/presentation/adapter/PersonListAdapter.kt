package com.jyh.realtimetranslation.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jyh.realtimetranslation.data.entity.person.PersonEntity
import com.jyh.realtimetranslation.databinding.PersonItemBinding

class PersonListAdapter: ListAdapter<PersonEntity, PersonListAdapter.ViewHolder>(diffUtil) {
    inner class ViewHolder(binding:PersonItemBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(person:PersonEntity){

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(PersonItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }


    companion object{
        val diffUtil = object : DiffUtil.ItemCallback<PersonEntity>() {
            override fun areItemsTheSame(oldItem: PersonEntity, newItem: PersonEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: PersonEntity, newItem: PersonEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}