package com.example.imagelrucache.ui.genratedogimage

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.imagelrucache.R
import com.example.imagelrucache.cache.lrudisk.LruDiskCache
import com.example.imagelrucache.databinding.FragmentGenerateDogImageBinding
import com.example.imagelrucache.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class GenerateDogImageFragment: Fragment(R.layout.fragment_generate_dog_image) {

    lateinit var binding:FragmentGenerateDogImageBinding
    private val viewModel:GenerateDogViewModel by viewModels()
    @Inject
    lateinit var imageCache: LruDiskCache

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentGenerateDogImageBinding.bind(view)

        binding.btnGenerate.setOnClickListener {
            binding.btnGenerate.isEnabled = false
            viewModel.generateDogImage()
        }

        setObserver()
    }

    private fun setObserver() {
        viewModel.dogImage.observe(viewLifecycleOwner){
            it?.let {
                when(it){
                    is Resource.Success -> {
                        it.data?.let {
                            lifecycleScope.launch(Dispatchers.Main) {
                                binding.ivDog.setImageBitmap(it)
                                binding.btnGenerate.isEnabled = true
                            }
                        }
                    }
                    is Resource.Error -> {
                        binding.btnGenerate.isEnabled = true
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Loading -> {}
                }
            }
        }
    }

}