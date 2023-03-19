package com.example.imagelrucache.adapter

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.imagelrucache.databinding.ItemDogBinding

class RecentDogAdapter: RecyclerView.Adapter<RecentDogAdapter.DogViewHolder>() {

    private val dogs = mutableListOf<Bitmap>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)=
        DogViewHolder(ItemDogBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(holder: DogViewHolder, position: Int) {
        holder.onBind(dogs[position])
    }

    override fun getItemCount() = dogs.size

    fun addDogs(dogs:List<Bitmap>){
        this.dogs.clear()
        this.dogs.addAll(dogs)
        notifyDataSetChanged()
    }

    class DogViewHolder(private val binding:ItemDogBinding):RecyclerView.ViewHolder(binding.root) {
        fun onBind(dogBitmap: Bitmap) {
            binding.ivDog.setImageBitmap(dogBitmap)
        }

    }

}