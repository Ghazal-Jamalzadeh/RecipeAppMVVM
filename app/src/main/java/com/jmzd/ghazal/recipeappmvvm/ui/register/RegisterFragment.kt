package com.jmzd.ghazal.recipeappmvvm.ui.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import coil.load
import com.jmzd.ghazal.recipeappmvvm.R
import com.jmzd.ghazal.recipeappmvvm.data.repository.RegisterRepository
import com.jmzd.ghazal.recipeappmvvm.databinding.FragmentRegisterBinding
import com.jmzd.ghazal.recipeappmvvm.models.register.BodyRegister
import com.jmzd.ghazal.recipeappmvvm.models.register.ResponseRegister
import com.jmzd.ghazal.recipeappmvvm.utils.Constants
import com.jmzd.ghazal.recipeappmvvm.utils.NetworkChecker
import com.jmzd.ghazal.recipeappmvvm.utils.NetworkRequest
import com.jmzd.ghazal.recipeappmvvm.utils.showSnackBar
import com.jmzd.ghazal.recipeappmvvm.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    //Binding
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    //other
    private val viewModel : RegisterViewModel by viewModels()
    private var email : String = ""

    @Inject
    lateinit var body : BodyRegister

    @Inject
    lateinit var networkChecker: NetworkChecker

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRegisterBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //init views
        binding.apply {
            //cover image
            coverImg.load(R.drawable.register_logo)
            //email
            emailEdt.addTextChangedListener {
                if (it.toString().contains("@")) {
                    email = it.toString()
                    emailTxtLay.error = ""
                } else {
                    emailTxtLay.error = getString(R.string.emailNotValid)
                }
            }
            //Click
            submitBtn.setOnClickListener {
                val firstname = nameEdt.text.toString()
                val lastName = lastNameEdt.text.toString()
                val username = usernameEdt.text.toString()
                //Body
                body.email = email
                body.firstName = firstname
                body.lastName = lastName
                body.username = username
                //Check network
                lifecycleScope.launchWhenStarted {
                    networkChecker.checkNetworkAvailability().collect { state : Boolean ->
                        if (state) {
                            //Call api
                            viewModel.callRegisterApi(Constants.MY_API_KEY, body)
                        } else {
                            /*روت به کانسترینت لایوت اشاره دارد!*/
                            root.showSnackBar(getString(R.string.checkConnection))
                        }
                    }
                }
                loadRegisterData()

            }


        }
    }

    private fun loadRegisterData() {
        viewModel.registerLiveData.observe(viewLifecycleOwner) { response : NetworkRequest<ResponseRegister> ->
            when (response) {
                is NetworkRequest.Loading -> {

                }
                is NetworkRequest.Success -> {
//
                }
                is NetworkRequest.Error -> {
                    binding.root.showSnackBar(response.message!!)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}