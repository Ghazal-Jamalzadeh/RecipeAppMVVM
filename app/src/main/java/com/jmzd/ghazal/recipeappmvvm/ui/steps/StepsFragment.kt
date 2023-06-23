package com.jmzd.ghazal.recipeappmvvm.ui.steps

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jmzd.ghazal.recipeappmvvm.R
import com.jmzd.ghazal.recipeappmvvm.adapter.StepsAdapter
import com.jmzd.ghazal.recipeappmvvm.databinding.FragmentMenuBinding
import com.jmzd.ghazal.recipeappmvvm.databinding.FragmentSplashBinding
import com.jmzd.ghazal.recipeappmvvm.databinding.FragmentStepsBinding
import com.jmzd.ghazal.recipeappmvvm.models.detail.ResponseDetail
import com.jmzd.ghazal.recipeappmvvm.models.detail.ResponseDetail.AnalyzedInstruction.Step
import com.jmzd.ghazal.recipeappmvvm.utils.Constants
import com.jmzd.ghazal.recipeappmvvm.utils.setupRecyclerview
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StepsFragment : BottomSheetDialogFragment() {

    //Binding
    private var _binding: FragmentStepsBinding? = null
    private val binding get() = _binding!!
    //Adapter
    @Inject lateinit var stepsAdapter: StepsAdapter
    //Args
    private val args: StepsFragmentArgs by navArgs()
    //Other
    private lateinit var steps: MutableList<Step>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentStepsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            //Args
            args.let {
                steps = it.data.steps!!.toMutableList()
                if (steps.isNotEmpty()) {
                    Constants.STEPS_COUNT = steps.size
                    stepsAdapter.setData(steps)
                    stepsList.setupRecyclerview(LinearLayoutManager(requireContext()), stepsAdapter)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}