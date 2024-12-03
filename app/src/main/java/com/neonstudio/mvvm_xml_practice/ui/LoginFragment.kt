package com.neonstudio.mvvm_xml_practice.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.neonstudio.mvvm_xml_practice.R
import com.neonstudio.mvvm_xml_practice.databinding.FragmentLoginBinding
import com.neonstudio.mvvm_xml_practice.models.UserRequest
import com.neonstudio.mvvm_xml_practice.utils.NetworkResult
import com.neonstudio.mvvm_xml_practice.utils.TokenManager
import com.neonstudio.mvvm_xml_practice.viewmodels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val authViewModel by viewModels<AuthViewModel>()

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)


//        binding.btnLogin.setOnClickListener {
//            findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
//        }

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener{
            val validationResult = validateUserInput()
            if(validationResult.first){
                authViewModel.loginUser(getUserRequest())
            }
            else{
                binding.txtError.text = validationResult.second
            }
        }

        binding.btnSignUp.setOnClickListener{
            findNavController().popBackStack()
            //findNavController().navigate(R.id.action_loginFragment_to_mainFragment)

        }

        bindObservers()

    }

    private fun bindObservers() {
        authViewModel.userResponseLiveData.observe(viewLifecycleOwner, Observer {
            binding.progressBar.isVisible = false
            when (it) {
                is NetworkResult.Success -> {
                    //also token code here
                    tokenManager.saveToken(it.data!!.token)
                    findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
                }

                is NetworkResult.Error -> {
                    binding.txtError.text = it.message
                }

                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true
                }

            }
        })
    }

    private fun getUserRequest(): UserRequest {
        val emailAddress = binding.txtEmail.text.toString()
        val password = binding.txtPassword.text.toString()
        return UserRequest(emailAddress,password,"")
    }

    private fun validateUserInput(): Pair<Boolean, String> {
        val userRequest = getUserRequest()
        return authViewModel.validateCredentials(userRequest.username,userRequest.email, userRequest.password, true)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}