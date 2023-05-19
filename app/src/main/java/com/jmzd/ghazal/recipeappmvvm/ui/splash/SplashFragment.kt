package com.jmzd.ghazal.recipeappmvvm.ui.splash

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import com.jmzd.ghazal.recipeappmvvm.BuildConfig
import com.jmzd.ghazal.recipeappmvvm.R
import com.jmzd.ghazal.recipeappmvvm.databinding.FragmentSplashBinding
import com.jmzd.ghazal.recipeappmvvm.models.register.RegisterStoredModel
import com.jmzd.ghazal.recipeappmvvm.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class SplashFragment : Fragment() {

    //Binding
    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSplashBinding.inflate(inflater , container , false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //InitViews
        binding.apply {
//Dynamically background
            bgImg.load(R.drawable.bg_splash)
            //Application Version
            versionTxt.text = "${getString(R.string.version)} : ${BuildConfig.VERSION_NAME}"
            //Auto navigate
            lifecycleScope.launchWhenCreated {
                delay(2500)
                //Check user info
                viewModel.readData.asLiveData().observe(viewLifecycleOwner) { storedData : RegisterStoredModel->
                    findNavController().popBackStack(R.id.splashFragment, true)

                    if (storedData.username.isNotEmpty()) {
                        //navigate to home
                    } else {
                        /* کدهای زیر در واقع مشابه هستن */
//                        findNavController().navigate(R.id.registerFragment)
                        findNavController().navigate(R.id.action_to_registerFragment)
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}