package com.jmzd.ghazal.recipeappmvvm.ui.recipe

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.jmzd.ghazal.recipeappmvvm.R
import com.jmzd.ghazal.recipeappmvvm.databinding.FragmentRecipeBinding
import com.jmzd.ghazal.recipeappmvvm.databinding.FragmentSplashBinding
import com.jmzd.ghazal.recipeappmvvm.models.register.RegisterStoredModel
import com.jmzd.ghazal.recipeappmvvm.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecipeFragment : Fragment() {

    //Binding
    private var _binding: FragmentRecipeBinding? = null
    private val binding get() = _binding!!

    //viewModel
    private val registerViewModel: RegisterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRecipeBinding.inflate(inflater , container , false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //show username
        lifecycleScope.launch {
            showUsername()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    @SuppressLint("SetTextI18n")
    suspend fun showUsername() {
        registerViewModel.readData.collect { storedData: RegisterStoredModel ->
            binding.usernameTxt.text = "${getString(R.string.hello)}, ${storedData.username} ${getEmojiByUnicode()}"
        }
    }

    private fun getEmojiByUnicode(): String {
        return String(Character.toChars(0x1f44b))
    }

}