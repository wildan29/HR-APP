package com.example.hrmaster.app.ui.fragment

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.hrmaster.R
import com.example.hrmaster.app.di.LoginViewModel
import com.example.hrmaster.app.di.SignUpViewModel
import com.example.hrmaster.app.ui.activity.DashboardActivity
import com.example.hrmaster.app.utils.Status
import com.example.hrmaster.app.utils.Utils
import com.example.hrmaster.data.models.request.LoginRequest
import com.example.hrmaster.data.models.request.UserRegisterRequest
import com.example.hrmaster.databinding.FragmentEnterBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EnterFragment : Fragment() {
    private var _binding: FragmentEnterBinding? = null
    private val binding get() = _binding!!
    private var password = ""
    private var username = ""
    private val viewModelLogin by viewModels<LoginViewModel>()
    private var cek = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEnterBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            mtvSignup.setOnClickListener {
                findNavController().navigate(R.id.action_enterFragment_to_signUpFragment)
            }
            btnLogin.setOnClickListener {
                password = edtPassword.text.toString()
                username = edtNama.text.toString()

                when {
                    edtNama.text.toString().isEmpty() -> {
                        tilNama.error = "Required *"
                        edtNama.requestFocus()
                    }

                    edtPassword.text.toString().isEmpty() -> {
                        tilPassword.error = "Required *"
                        edtPassword.requestFocus()
                    }

                }

                if (username.isNotEmpty() && password.isNotEmpty()) {
                    alertDialogLogin()
                } else {
                    Snackbar.make(requireView(), "Data harus diisi!", Snackbar.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun alertDialogLogin() {
        MaterialAlertDialogBuilder(
            requireContext(), R.style.ThemeOverlay_App_MaterialAlertDialog
        ).setTitle("Buat akun")
            .setMessage(
                "Apakah Anda yakin dengan data ini ?"
            ).setPositiveButton("Simpan") { _, _ ->
                loginUser()
            }.setNeutralButton("Batal") { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    private fun loginUser(){
        binding.apply {
            viewModelLogin.login(
                LoginRequest(password, username)
            )

            viewLifecycleOwner.lifecycleScope.launch {
                viewModelLogin.login.flowWithLifecycle(
                    viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED
                ).collect {
                    Utils.showLoading(
                        progressIndicator, it.status == Status.LOADING
                    )
                    Utils.showLoading(overlay, it.status == Status.LOADING)
                    setViewAndChildrenEnabled(content, false)
                    when (it.status) {
                        Status.ERROR -> {
                            setViewAndChildrenEnabled(content, true)
                            viewModelLogin.message.collect { msg ->
                                Snackbar.make(requireView(), msg, Snackbar.LENGTH_SHORT)
                                    .show()
                            }
                        }

                        Status.LOADING -> {
                            delay(2000L)
                        }

                        Status.SUCCESS -> {
                            setViewAndChildrenEnabled(content, true)
                                Snackbar.make(requireView(), "Login Sukses", Snackbar.LENGTH_SHORT)
                                    .show()

                            // add 1 second delay
                            delay(1000)

                            val bundle =
                                ActivityOptions.makeSceneTransitionAnimation(requireActivity()).toBundle()
                            startActivity(
                                Intent(requireActivity(), DashboardActivity::class.java),
                                bundle
                            )

                            cek = true
                        }
                    }
                }
            }
        }
    }


    private fun setViewAndChildrenEnabled(view: View, enabled: Boolean) {
        view.isEnabled = enabled
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val child = view.getChildAt(i)
                setViewAndChildrenEnabled(child, enabled)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        if (cek) {
            activity?.finishAfterTransition()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}