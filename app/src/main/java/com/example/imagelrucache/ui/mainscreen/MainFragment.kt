package com.example.imagelrucache.ui.mainscreen

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.imagelrucache.R
import com.example.imagelrucache.databinding.FragmentMainBinding

class MainFragment : Fragment(R.layout.fragment_main) {

    lateinit var binding: FragmentMainBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainBinding.bind(view)

        binding.btnGenerateDog.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_generateDogImageFragment)
        }

        binding.btnRecentDogImage.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_recentDogImagesFragment)
        }
    }

}