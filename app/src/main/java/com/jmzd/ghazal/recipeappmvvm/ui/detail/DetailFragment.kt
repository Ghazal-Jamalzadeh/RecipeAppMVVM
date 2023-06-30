package com.jmzd.ghazal.recipeappmvvm.ui.detail

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.request.CachePolicy
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.chip.ChipGroup
import com.jmzd.ghazal.recipeappmvvm.R
import com.jmzd.ghazal.recipeappmvvm.adapter.InstructionsAdapter
import com.jmzd.ghazal.recipeappmvvm.adapter.SimilarAdapter
import com.jmzd.ghazal.recipeappmvvm.adapter.StepsAdapter
import com.jmzd.ghazal.recipeappmvvm.data.database.entity.DetailEntity
import com.jmzd.ghazal.recipeappmvvm.databinding.FragmentDetailBinding
import com.jmzd.ghazal.recipeappmvvm.models.detail.ResponseDetail
import com.jmzd.ghazal.recipeappmvvm.models.detail.ResponseSimilar
import com.jmzd.ghazal.recipeappmvvm.ui.recipe.RecipeFragmentDirections
import com.jmzd.ghazal.recipeappmvvm.utils.*
import com.jmzd.ghazal.recipeappmvvm.viewmodel.DetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import javax.inject.Inject

@AndroidEntryPoint
class DetailFragment : Fragment() {

    //Binding
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    //ViewModel
    private val viewModel: DetailViewModel by viewModels()

    //Args
    private val args: DetailFragmentArgs by navArgs()

    //Network checker
    @Inject
    lateinit var networkChecker: NetworkChecker

    //Adapters
    @Inject
    lateinit var instructionsAdapter: InstructionsAdapter
    @Inject
    lateinit var similarAdapter: SimilarAdapter
    @Inject
    lateinit var stepsAdapter: StepsAdapter

    //Other
    private var recipeId: Int = 0
    private var isExistsCache = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Args
        args.let {
            recipeId = args.recipeId
            //call api
            if (recipeId != 0) {
                checkExistsDetailInCache(recipeId)
            }
        }
        //Check Internet
        lifecycleScope.launchWhenStarted {
            networkChecker.checkNetworkAvailability().collect { isConnected: Boolean ->
                delay(200)
                if (isExistsCache.not()) {
                    initInternetLayout(isConnected)
                    if (isConnected) {
                        viewModel.callDetailApi(recipeId, Constants.MY_API_KEY)
                        observeDetailDataFromApi()
                    }
                }
                //Similar
                if (isConnected) {
                    viewModel.callSimilarApi(recipeId, Constants.MY_API_KEY)
                    observeSimilarDataFromApi()
                }
            }
        }

        //InitViews
        binding.apply {
            //Back
            backImg.setOnClickListener { findNavController().popBackStack() }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    //--- load data from cache ---//

    private fun checkExistsDetailInCache(id: Int) {
        viewModel.existsDetail(id)
        //Load
        viewModel.existsDetailLiveData.observe(viewLifecycleOwner) { isExist: Boolean ->
            isExistsCache = isExist
            if (isExist) {
                loadDetailDataFromDb()
                binding.contentLay.isVisible = true
            }
        }
    }

    private fun loadDetailDataFromDb() {
        viewModel.readDetailFromDb(recipeId)
            .observe(viewLifecycleOwner) { cachedEntity: DetailEntity ->
                initViewsWithData(cachedEntity.result)
            }
    }

    //--- api results observers ---//
    private fun observeDetailDataFromApi() {
        binding.apply {
            viewModel.detailLiveData.observe(viewLifecycleOwner) { response: NetworkRequest<ResponseDetail> ->
                when (response) {
                    is NetworkRequest.Loading -> {
                        loading.isVisible(true, contentLay)
                    }
                    is NetworkRequest.Success -> {
                        loading.isVisible(false, contentLay)
                        response.data?.let { data ->
                            initViewsWithData(data)
                        }
                    }
                    is NetworkRequest.Error -> {
                        loading.isVisible(false, contentLay)
                        binding.root.showSnackBar(response.message!!)
                    }
                }
            }
        }
    }

    private fun observeSimilarDataFromApi() {
        binding.apply {
            viewModel.similarData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkRequest.Loading -> {
                        similarList.showShimmer()
                    }
                    is NetworkRequest.Success -> {
                        similarList.hideShimmer()
                        response.data?.let { data ->
                            initSimilarData(data)
                        }
                    }
                    is NetworkRequest.Error -> {
                        similarList.hideShimmer()
                        binding.root.showSnackBar(response.message!!)
                    }
                }
            }
        }
    }

    //--- show data ---//
    @SuppressLint("SetTextI18n")
    private fun initViewsWithData(data: ResponseDetail) {
        binding.apply {
            //Favorite
            /*        viewModel.existsFavorite(data.id!!)
                    checkExistsFavorite()*/
            //Click favorites
            /*  favoriteImg.setOnClickListener {
                  if (isExistsFavorite) deleteFavorite(data) else saveFavorite(data)
              }*/
            //Image
            val imageSplit = data.image!!.split("-")
            val imageSize =
                imageSplit[1].replace(Constants.OLD_IMAGE_SIZE, Constants.NEW_IMAGE_SIZE)
            coverImg.load("${imageSplit[0]}-$imageSize") {
                crossfade(true)
                crossfade(800)
                memoryCachePolicy(CachePolicy.ENABLED)
                error(R.drawable.ic_placeholder)
            }
            //Source
            data.sourceUrl?.let { source ->
                sourceImg.isVisible = true
                sourceImg.setOnClickListener {
                    val direction = DetailFragmentDirections.actionToWebViewFragment(source)
                    findNavController().navigate(direction)
                }
            }
            //Text
            timeTxt.text = data.readyInMinutes!!.minToHour()
            nameTxt.text = data.title
            //Desc
            val summary = HtmlCompat.fromHtml(data.summary!!, HtmlCompat.FROM_HTML_MODE_COMPACT)
            descTxt.text = summary
            //Toggle
            if (data.cheap!!) cheapTxt.setDynamicallyColor(R.color.caribbean_green)
            if (data.veryPopular!!) popularTxt.setDynamicallyColor(R.color.tart_orange)
            if (data.vegan!!) veganTxt.setDynamicallyColor(R.color.caribbean_green)
            if (data.dairyFree!!) dairyTxt.setDynamicallyColor(R.color.caribbean_green)
            //Like
            likeTxt.text = data.aggregateLikes.toString()
            //price
            priceTxt.text = "${data.pricePerServing} $"
            //Healthy
            healthyTxt.text = data.healthScore.toString()
            when (data.healthScore) {
                in 90..100 -> healthyTxt.setDynamicallyColor(R.color.caribbean_green)
                in 60..89 -> healthyTxt.setDynamicallyColor(R.color.chineseYellow)
                in 0..59 -> healthyTxt.setDynamicallyColor(R.color.tart_orange)
            }
            //Instructions
            instructionsCount.text =
                "${data.extendedIngredients!!.size} ${getString(R.string.items)}"
            val instructions =
                HtmlCompat.fromHtml(data.instructions!!, HtmlCompat.FROM_HTML_MODE_COMPACT)
            instructionsDesc.text = instructions
            initInstructionsList(data.extendedIngredients.toMutableList())
            //Steps
            initStepsList(data.analyzedInstructions!![0].steps!!.toMutableList())
            stepsShowMore.setOnClickListener {
                val direction =
                    DetailFragmentDirections.actionDetailFragmentToStepsFragment(data.analyzedInstructions[0])
                findNavController().navigate(direction)
            }
            //Diets
            setupChip(data.diets!!.toMutableList(), dietsChipGroup)
        }
    }

    private fun initInternetLayout(isConnected: Boolean) {
        binding.internetLay.isVisible = isConnected.not()
    }

    //--- chips ---//
    private fun setupChip(list: MutableList<String>, view: ChipGroup) {
        list.forEach {
            val chip = Chip(requireContext())
            val drawable =
                ChipDrawable.createFromAttributes(requireContext(), null, 0, R.style.DietsChip)
            chip.setChipDrawable(drawable)
            chip.setTextColor(ContextCompat.getColor(requireContext(), R.color.darkGray))
            chip.text = it
            view.addView(chip)
        }
    }

    //--- recycler views  ---//
    private fun initInstructionsList(list: MutableList<ResponseDetail.ExtendedIngredient>) {
        if (list.isNotEmpty()) {
            instructionsAdapter.setData(list)
            binding.instructionsList.setupRecyclerview(
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false),
                instructionsAdapter
            )
        }
    }

    private fun initStepsList(list: MutableList<ResponseDetail.AnalyzedInstruction.Step>) {
        if (list.isNotEmpty()) {
            Constants.STEPS_COUNT = if (list.size < 3) {
                list.size
            } else {
                3
            }
            stepsAdapter.setData(list)
            binding.apply {
                stepsList.setupRecyclerview(LinearLayoutManager(requireContext()), stepsAdapter)
                //Show more
                if (list.size > 3) {
                    stepsShowMore.isVisible = true
                }
            }
        }
    }

    private fun initSimilarData(list: MutableList<ResponseSimilar.ResponseSimilarItem>) {
        similarAdapter.setData(list)
        binding.similarList.setupRecyclerview(
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false),
            similarAdapter
        )
        //Click
        similarAdapter.setOnItemClickListener { id: Int ->
            val action = RecipeFragmentDirections.actionToDetailFragment(id)
            findNavController().navigate(action)
        }
    }

}