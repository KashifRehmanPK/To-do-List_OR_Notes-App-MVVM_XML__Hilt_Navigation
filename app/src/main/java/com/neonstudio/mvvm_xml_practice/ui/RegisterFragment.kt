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
import com.neonstudio.mvvm_xml_practice.databinding.FragmentRegisterBinding
import com.neonstudio.mvvm_xml_practice.models.UserRequest
import com.neonstudio.mvvm_xml_practice.utils.NetworkResult
import com.neonstudio.mvvm_xml_practice.utils.TokenManager
import com.neonstudio.mvvm_xml_practice.viewmodels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val authViewModel: AuthViewModel by viewModels()

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

//        val view = inflater.inflate(R.layout.fragment_register, container, false)
//        val txtRedirect = view. findViewById<TextView>(R.id.txtRedirect)

        _binding = FragmentRegisterBinding.inflate(inflater, container, false)


        if(tokenManager.getToken() !=null){
            findNavController().navigate(R.id.action_registerFreagment_to_mainFragment)
        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSignUp.setOnClickListener {
            // findNavController().navigate(R.id.action_registerFreagment_to_mainFragment)
            val validationResult = validateUserInput()
            if(validationResult.first){
               // authViewModel.registerUser(UserRequest("testing000@gmail.com", "111", "testing"))
                authViewModel.registerUser(getUserRequest())
            }
            else{
                binding.txtError.text = validationResult.second
            }




        }
        binding.btnLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFreagment_to_loginFragment)

            //authViewModel.loginUser(UserRequest("testing000@gmail.com", "111", "testing"))

        }

        bindObserver()
    }

    private fun getUserRequest(): UserRequest {
        val emailAddress = binding.txtEmail.text.toString()
        val password = binding.txtPassword.text.toString()
        val username = binding.txtUsername.id.toString()
        return UserRequest(emailAddress,password,username)
    }

    private fun validateUserInput(): Pair<Boolean, String> {
        val userRequest = getUserRequest()
        return authViewModel.validateCredentials(userRequest.username,userRequest.email, userRequest.password,false)
    }



    private fun bindObserver() {
        authViewModel.userResponseLiveData.observe(viewLifecycleOwner, Observer {
            binding.progressBar.isVisible = false
            when (it) {
                is NetworkResult.Success -> {
                    //also token code here
                    tokenManager.saveToken(it.data!!.token)
                    findNavController().navigate(R.id.action_registerFreagment_to_mainFragment)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}