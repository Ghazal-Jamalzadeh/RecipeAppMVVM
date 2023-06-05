package com.jmzd.ghazal.recipeappmvvm.ui.recipe

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.jmzd.ghazal.recipeappmvvm.R
import com.jmzd.ghazal.recipeappmvvm.adapter.PopularAdapter
import com.jmzd.ghazal.recipeappmvvm.adapter.RecentAdapter
import com.jmzd.ghazal.recipeappmvvm.data.database.RecipeEntity
import com.jmzd.ghazal.recipeappmvvm.databinding.FragmentRecipeBinding
import com.jmzd.ghazal.recipeappmvvm.models.recipe.ResponseRecipes
import com.jmzd.ghazal.recipeappmvvm.models.recipe.ResponseRecipes.*
import com.jmzd.ghazal.recipeappmvvm.models.register.RegisterStoredModel
import com.jmzd.ghazal.recipeappmvvm.utils.Constants
import com.jmzd.ghazal.recipeappmvvm.utils.NetworkRequest
import com.jmzd.ghazal.recipeappmvvm.utils.setupRecyclerview
import com.jmzd.ghazal.recipeappmvvm.utils.showSnackBar
import com.jmzd.ghazal.recipeappmvvm.viewmodel.RecipeViewModel
import com.jmzd.ghazal.recipeappmvvm.viewmodel.RegisterViewModel
import com.todkars.shimmer.ShimmerRecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RecipeFragment : Fragment() {

    private val TAG = "RecipeFragment"
    //Binding
    private var _binding: FragmentRecipeBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var popularAdapter: PopularAdapter

    @Inject
    lateinit var recentAdapter: RecentAdapter

    //viewModel
    private val registerViewModel: RegisterViewModel by viewModels()
    private val viewModel: RecipeViewModel by viewModels()

    //other
    private var autoScrollIndex = 0

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
        //get data
        getPopularData()
//        viewModel.getRecents(viewModel.getRecentQueries())
        //load data
        loadPopularData()
        loadRecentData()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    //---Cache---//
    private fun getPopularData() {
        initPopularRecycler()
        viewModel.popularFromDbLiveData.observe(viewLifecycleOwner) { database : List<RecipeEntity> ->
            if (database.isNotEmpty()) {
                database[0].response.results?.let { result : List<Result> ->
                    setupLoading(false, binding.popularList)
                    fillPopularAdapter(result.toMutableList())
                }
                Log.d(TAG, "load data from db : ")
            } else {
                viewModel.callPopularApi(viewModel.getPopularQueries())
                Log.d(TAG, "call api ")
            }
        }
    }

    //---observers---//
    private fun loadPopularData() {
        binding.apply {
            viewModel.popularLiveData.observe(viewLifecycleOwner) { response : NetworkRequest<ResponseRecipes>->
                Log.d(TAG, "load data  from api ")
                when (response) {
                    is NetworkRequest.Loading -> {
                        setupLoading(true, popularList)
                    }
                    is NetworkRequest.Success -> {
                        setupLoading(false, popularList)
                        response.data?.let { data : ResponseRecipes ->
                            if (data.results!!.isNotEmpty()) {
                                fillPopularAdapter(data.results.toMutableList())
                            }
                        }
                    }
                    is NetworkRequest.Error -> {
                        setupLoading(false, popularList)
                        binding.root.showSnackBar(response.message!!)
                    }
                }
            }
        }
    }

    private fun loadRecentData() {
        binding.apply {
            viewModel.recentsLiveData.observe(viewLifecycleOwner) { response : NetworkRequest<ResponseRecipes> ->
                when (response) {
                    is NetworkRequest.Loading -> {
                        setupLoading(true, recipesList)
                    }
                    is NetworkRequest.Success -> {
                        setupLoading(false, recipesList)
                        response.data?.let { data : ResponseRecipes->
                            if (data.results!!.isNotEmpty()) {
                                recentAdapter.setData(data.results)
                                initRecentRecycler()
                            }
                        }
                    }
                    is NetworkRequest.Error -> {
                        setupLoading(false, recipesList)
                        binding.root.showSnackBar(response.message!!)
                    }
                }
            }
        }
    }

    //---UI---//
    private fun setupLoading(isShownLoading: Boolean, shimmer: ShimmerRecyclerView) {
        shimmer.apply {
            if (isShownLoading) showShimmer() else hideShimmer()
        }
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

    private fun autoScrollPopular(list: List<Result>) {
        /* زمانی که صفحه ساخته شد این بلاک اجرا میشه*/
        lifecycleScope.launchWhenCreated {
            /* کدهای داخل این بلاک این تعداد تکرار میشه*/
            repeat(Constants.REPEAT_TIME) {
                /* هر ۵ ثانیه این کد اجرا میشه*/
                delay(Constants.DELAY_TIME)
                if (autoScrollIndex < list.size) {
                    autoScrollIndex += 1
                } else {
                    autoScrollIndex = 0
                }
                binding.popularList.smoothScrollToPosition(autoScrollIndex)
            }
        }
    }

    //---init recyclers---//
    private fun fillPopularAdapter(result: MutableList<Result>) {
        popularAdapter.setData(result)
        autoScrollPopular(result)
    }

    private fun initPopularRecycler() {
        binding.popularList.setupRecyclerview(
           myLayoutManager =  LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false),
            myAdapter = popularAdapter
        )
        //Snap
        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(binding.popularList)
        //Click
        popularAdapter.setOnItemClickListener { id : Int ->
            //go to detail page
        }
    }

    private fun initRecentRecycler() {
        binding.recipesList.setupRecyclerview(
            LinearLayoutManager(requireContext()),
            recentAdapter
        )
        //Click
        recentAdapter.setOnItemClickListener {
         /*   gotoDetailPage(it)*/
        }
    }

}