package com.jmzd.ghazal.recipeappmvvm.ui.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.chip.ChipGroup
import com.jmzd.ghazal.recipeappmvvm.R
import com.jmzd.ghazal.recipeappmvvm.databinding.FragmentMenuBinding
import com.jmzd.ghazal.recipeappmvvm.models.menu.MenuStoredModel
import com.jmzd.ghazal.recipeappmvvm.utils.onceObserve
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
    private var selectedMealTitle = ""
    private var selectedMealId = 0
    private var selectedDietTitle = ""
    private var selectedDietId = 0

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
            //Read from menu stored data
            viewModel.menuStoredItems.asLiveData().onceObserve(viewLifecycleOwner) {storedData : MenuStoredModel ->
                selectedMealTitle = storedData.meal
                selectedDietTitle = storedData.diet
                updateChip(storedData.mealId, mealChipGroup)
                updateChip(storedData.dietId, dietChipGroup)
            }
            //Meal chips - click
            mealChipGroup.setOnCheckedStateChangeListener { group : ChipGroup, checkedIds : MutableList<Int>->
                var chip: Chip
                checkedIds.forEach { checkedId : Int ->
                    chip = group.findViewById(checkedId)
                    selectedMealTitle = chip.text.toString().lowercase()
                    selectedMealId = checkedId
                }
            }
            //Diet chips - click
            dietChipGroup.setOnCheckedStateChangeListener { group : ChipGroup, checkedIds : MutableList<Int> ->
                var chip: Chip
                checkedIds.forEach { checkedId : Int ->
                    chip = group.findViewById(checkedId)
                    selectedDietTitle = chip.text.toString().lowercase()
                    selectedDietId = checkedId
                }
            }
            //Submit
            submitBtn.setOnClickListener {
                viewModel.saveToStore(
                    meal = selectedMealTitle ,
                    mealId = selectedMealId ,
                    diet = selectedDietTitle ,
                    dietId = selectedDietId
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

    private fun updateChip(id: Int, view: ChipGroup) {
        if (id != 0) {
            view.findViewById<Chip>(id).isChecked = true
        }
    }

}