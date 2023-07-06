package com.jmzd.ghazal.recipeappmvvm.ui.search

import android.graphics.Rect
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.jmzd.ghazal.recipeappmvvm.adapter.RecentAdapter
import com.jmzd.ghazal.recipeappmvvm.databinding.FragmentSearchBinding
import com.jmzd.ghazal.recipeappmvvm.ui.recipe.RecipeFragmentDirections
import com.jmzd.ghazal.recipeappmvvm.utils.NetworkChecker
import com.jmzd.ghazal.recipeappmvvm.utils.NetworkRequest
import com.jmzd.ghazal.recipeappmvvm.utils.setupRecyclerview
import com.jmzd.ghazal.recipeappmvvm.utils.showSnackBar
import com.jmzd.ghazal.recipeappmvvm.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.properties.Delegates


@AndroidEntryPoint
class SearchFragment : Fragment() {
    //Binding
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var recentAdapter: RecentAdapter

    @Inject
    lateinit var networkChecker: NetworkChecker

    //Other
    private val viewModel: SearchViewModel by viewModels()
    private var isNetworkAvailable by Delegates.notNull<Boolean>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //InitViews
        binding.apply {
            //Keyboard listener
            requireActivity().window.decorView.viewTreeObserver.addOnGlobalLayoutListener {
                val rect = Rect()
                requireActivity().window.decorView.getWindowVisibleDisplayFrame(rect)
                val height = requireActivity().window.decorView.height
                if (height - rect.bottom <= height * 0.1399)
                    rootMotion.transitionToStart()
                else
                    rootMotion.transitionToEnd()
            }
            //Check internet
            lifecycleScope.launchWhenStarted {
                networkChecker.checkNetworkAvailability().collect { isConnected : Boolean ->
                    initInternetLayout(isConnected)
                    isNetworkAvailable = isConnected
                }
            }
            //Search
            searchEdt.addTextChangedListener {
                if (it.toString().length>2 && isNetworkAvailable)
                    viewModel.callSearchApi(viewModel.getSearchQueries(it.toString()))
            }
        }
        //Show data
        loadRecentData()
    }

    private fun loadRecentData() {
        binding.apply {
            viewModel.searchLiveData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkRequest.Loading -> {
                        searchList.showShimmer()
                    }
                    is NetworkRequest.Success -> {
                        searchList.hideShimmer()
                        response.data?.let { data ->
                            if (data.results!!.isNotEmpty()) {
                                recentAdapter.setData(data.results)
                                initRecentRecycler()
                            }
                        }
                    }
                    is NetworkRequest.Error -> {
                        searchList.hideShimmer()
                        binding.root.showSnackBar(response.message!!)
                    }
                }
            }
        }
    }

    private fun initRecentRecycler() {
        binding.searchList.setupRecyclerview(
            LinearLayoutManager(requireContext()),
            recentAdapter
        )
        //Click
        recentAdapter.setOnItemClickListener {
            gotoDetailPage(it)
        }
    }

    private fun gotoDetailPage(id: Int) {
        val action = RecipeFragmentDirections.actionToDetailFragment(id)
        findNavController().navigate(action)
    }

    private fun initInternetLayout(isConnected: Boolean) {
        binding.internetLay.isVisible = isConnected.not()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}