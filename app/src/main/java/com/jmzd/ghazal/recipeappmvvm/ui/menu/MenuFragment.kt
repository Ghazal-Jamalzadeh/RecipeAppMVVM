package com.jmzd.ghazal.recipeappmvvm.ui.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
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
    //selected values
    private var selectedChipMealTitle = ""
    private var selectedChipMealId = 0
    private var selectedCipDietTitle = ""
    private var selectedCipDietId = 0

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
            setupChip(viewModel.getMealsList(), mealChipGroup)
            setupChip(viewModel.getDietsList(), dietChipGroup)
            //Meal chips - click
            mealChipGroup.setOnCheckedStateChangeListener { group : ChipGroup, checkedIds : MutableList<Int>->
                var chip: Chip
                checkedIds.forEach { checkedId : Int ->
                    chip = group.findViewById(checkedId)
                    selectedChipMealTitle = chip.text.toString().lowercase()
                    selectedChipMealId = checkedId
                }
            }
            //Diet chips - click
            dietChipGroup.setOnCheckedStateChangeListener { group : ChipGroup, checkedIds : MutableList<Int> ->
                var chip: Chip
                checkedIds.forEach { checkedId : Int ->
                    chip = group.findViewById(checkedId)
                    selectedCipDietTitle = chip.text.toString().lowercase()
                    selectedCipDietId = checkedId
                }
            }
            //Submit
            submitBtn.setOnClickListener {
                viewModel.saveToStore(
                    meal = selectedChipMealTitle ,
                    mealId = selectedChipMealId ,
                    diet = selectedCipDietTitle ,
                    dietId = selectedCipDietId
                )
                findNavController().navigate(MenuFragmentDirections.actionMenuFragmentToRecipeFragment().setIsUpdated(true))
            }

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