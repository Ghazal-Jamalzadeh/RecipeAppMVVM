package com.jmzd.ghazal.recipeappmvvm.ui.menu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.chip.ChipGroup
import com.jmzd.ghazal.recipeappmvvm.R
import com.jmzd.ghazal.recipeappmvvm.databinding.FragmentMenuBinding
import com.jmzd.ghazal.recipeappmvvm.viewmodel.MenuViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MenuFragment : BottomSheetDialogFragment() {

    //Binding
    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!

    //ViewModel
    private lateinit var viewModel: MenuViewModel

    //other
    private var chipCounter = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[MenuViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMenuBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //init views
        binding.apply {
            //Generate chips
            setupChip(viewModel.getMalsList(), mealChipGroup)
            setupChip(viewModel.getDietsList(), dietChipGroup)

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setupChip(list: MutableList<String>, view: ChipGroup) {
        list.forEach {
            val chip = Chip(requireContext())
            val drawable =
                ChipDrawable.createFromAttributes(requireContext(), null, 0, R.style.DarkChip)
            chip.setChipDrawable(drawable)
            chip.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            chip.id = chipCounter++
            chip.text = it
            view.addView(chip)
        }
    }

}