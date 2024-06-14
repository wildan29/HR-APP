package com.example.hrmaster.app.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.hrmaster.R
import com.example.hrmaster.app.di.SignUpViewModel
import com.example.hrmaster.app.utils.Status
import com.example.hrmaster.app.utils.Utils
import com.example.hrmaster.data.models.request.UserRegisterRequest
import com.example.hrmaster.databinding.FragmentSignUpBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpFragment : Fragment() {
    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    private val viewModelRegister by viewModels<SignUpViewModel>()
    private var password = ""
    private var username = ""
    private var email = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSignUpBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            btnRegister.setOnClickListener {
                password = edtPassword.text.toString()
                username = edtNama.text.toString()
                email = edtEmail.text.toString()

                when {
                    edtNama.text.toString().isEmpty() -> {
                        tilNama.error = "Required *"
                        edtNama.requestFocus()
                    }

                    edtEmail.text.toString().isEmpty() -> {
                        tilEmail.error = "Required *"
                        edtEmail.requestFocus()
                    }

                    edtPassword.text.toString().isEmpty() -> {
                        tilPassword.error = "Required *"
                        edtPassword.requestFocus()
                    }

                }

                if (username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                    alertDialogSave()
                } else {
                    Snackbar.make(requireView(), "Data harus diisi!", Snackbar.LENGTH_SHORT)
                        .show()
                }
            }

            mtvSignin.setOnClickListener {
                findNavController().navigate(R.id.action_signUpFragment_to_enterFragment)
            }
        }
    }

    private fun alertDialogSave() {
        MaterialAlertDialogBuilder(
            requireContext(), R.style.ThemeOverlay_App_MaterialAlertDialog
        ).setTitle("Buat akun")
            .setMessage(
                "Apakah Anda yakin dengan data ini ?"
            ).setPositiveButton("Simpan") { _, _ ->
                addUser()
            }.setNeutralButton("Batal") { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    private fun addUser() {
        binding.apply {
            viewModelRegister.add(
                UserRegisterRequest(password, email, username)
            )

            viewLifecycleOwner.lifecycleScope.launch {
                viewModelRegister.userSignUp.flowWithLifecycle(
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
                            viewModelRegister.message.collect { msg ->
                                Snackbar.make(requireView(), msg, Snackbar.LENGTH_SHORT)
                                    .show()
                            }
                        }

                        Status.LOADING -> {
                            delay(2000L)
                        }

                        Status.SUCCESS -> {
                            setViewAndChildrenEnabled(content, true)
                            if (it.data?.data?.message != null) {
                                Snackbar.make(requireView(), it.data.data.message, Snackbar.LENGTH_SHORT)
                                    .show()
                                findNavController().navigate(R.id.action_signUpFragment_to_enterFragment)
                            }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}