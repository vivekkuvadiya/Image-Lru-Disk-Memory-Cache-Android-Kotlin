package com.example.imagelrucache.ui.recentdogimage

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.imagelrucache.R
import com.example.imagelrucache.adapter.RecentDogAdapter
import com.example.imagelrucache.cache.lrudisk.LruDiskCache
import com.example.imagelrucache.databinding.FragmentRecentDogImagesBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RecentDogImagesFragment:Fragment(R.layout.fragment_recent_dog_images) {

    lateinit var binding:FragmentRecentDogImagesBinding
    private val viewModel:RecentDogViewModel by viewModels()
    private lateinit var recentDogAdapter: RecentDogAdapter

    @Inject
    lateinit var imageCache: LruDiskCache

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRecentDogImagesBinding.bind(view)

        setDogsRecyclerview()

        binding.btnClearDogs.setOnClickListener {
            viewModel.clearDogs()
        }

        setObserver()
    }

    private fun setObserver() {
        viewModel.allDogs.observe(viewLifecycleOwner){
            it?.let {
                recentDogAdapter.addDogs(it)
            }
        }
    }

    private fun setDogsRecyclerview() = binding.rvRecentDogs.apply {
        recentDogAdapter = RecentDogAdapter()
        adapter = recentDogAdapter
        layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
    }
}