package com.example.hrmaster.app.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hrmaster.R
import com.example.hrmaster.app.di.HomeViewModel
import com.example.hrmaster.app.ui.adapter.EmployeeAdapter
import com.example.hrmaster.app.utils.Status
import com.example.hrmaster.data.models.response.DataUser
import com.example.hrmaster.databinding.FragmentHomeBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import koleton.api.hideSkeleton
import koleton.api.loadSkeleton
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Calendar


@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModelHome by viewModels<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            rvListStory.layoutManager = LinearLayoutManager(context)
            rvListStory.adapter = EmployeeAdapter(
                listOf(
                    DataUser(null, null, null),
                    DataUser(null, null, null),
                    DataUser(null, null, null),
                    DataUser(null, null, null),
                    DataUser(null, null, null),
                    DataUser(null, null, null),
                    DataUser(null, null, null),
                    DataUser(null, null, null),
                    DataUser(null, null, null),
                    DataUser(null, null, null)
                )
            )

            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    launch {
                        viewModelHome.isLaunch.collect {
                            if (!it) {
                                // use skeleton loading
                                username.loadSkeleton()
                                shapeableImageView.loadSkeleton()
                                card.loadSkeleton()
                                card1.loadSkeleton()
                                card2.loadSkeleton()
                                rvListStory.loadSkeleton(R.layout.item_employee) {
                                    itemCount(10)
                                }
                            }
                        }
                    }

                    launch {
                        viewModelHome.getUser.collect {
                            when (it.status) {
                                Status.ERROR -> {
                                    viewModelHome.message.collectLatest { msg ->
                                        Snackbar.make(requireView(), msg, Snackbar.LENGTH_SHORT)
                                            .show()
                                    }
                                }

                                Status.LOADING -> {
                                    delay(2000L)
                                }

                                Status.SUCCESS -> {
                                    username.hideSkeleton()
                                    shapeableImageView.hideSkeleton()
                                    card.hideSkeleton()
                                    card1.hideSkeleton()
                                    card2.hideSkeleton()
                                    rvListStory.hideSkeleton()
                                    viewModelHome.launch()
                                    if (it.data?.data?.foto != null) {
                                        // assign photo
                                    }
                                }
                            }
                        }
                    }

                    launch {
                        viewModelHome.getUser3.collect {
                            if (it.status == Status.SUCCESS) {
                                username.text = "${greeting()}, ${it.data?.data?.username}"
                            }
                        }
                    }
                }
            }

            // get current user
            viewModelHome.getUser()
        }
    }

    private fun greeting(): String {
        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)

        return when {
            currentHour in 0..11 -> "Good morning"
            currentHour in 12..18 -> "Good afternoon"
            currentHour in 19..21 -> "Good evening"
            else -> "good night"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}