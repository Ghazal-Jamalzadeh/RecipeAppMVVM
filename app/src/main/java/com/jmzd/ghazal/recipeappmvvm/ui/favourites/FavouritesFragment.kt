package com.jmzd.ghazal.recipeappmvvm.ui.favourites

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.jmzd.ghazal.recipeappmvvm.R
import com.jmzd.ghazal.recipeappmvvm.adapter.FavoriteAdapter
import com.jmzd.ghazal.recipeappmvvm.databinding.FragmentDetailBinding
import com.jmzd.ghazal.recipeappmvvm.databinding.FragmentFavouritesBinding
import com.jmzd.ghazal.recipeappmvvm.ui.recipe.RecipeFragmentDirections
import com.jmzd.ghazal.recipeappmvvm.utils.isVisible
import com.jmzd.ghazal.recipeappmvvm.utils.setupRecyclerview
import com.jmzd.ghazal.recipeappmvvm.viewmodel.FavoriteViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FavouritesFragment : Fragment() {

    //Binding
    private var _binding: FragmentFavouritesBinding? = null
    private val binding get() = _binding!!

    //ViewModel
    private val viewModel: FavoriteViewModel by viewModels()

    //Adapter
    @Inject
    lateinit var favoriteAdapter: FavoriteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View{
        _binding = FragmentFavouritesBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //InitViews
        binding.apply {
            //Load favorites
            viewModel.favoritesLiveData.observe(viewLifecycleOwner) {
                if (it.isNotEmpty()) {
                    emptyTxt.isVisible(false, favoriteList)
                    //Data
                    favoriteAdapter.setData(it)
                    favoriteList.setupRecyclerview(LinearLayoutManager(requireContext()), favoriteAdapter)
                    //Click
                    favoriteAdapter.setOnItemClickListener { id ->
                        val action = RecipeFragmentDirections.actionToDetailFragment(id)
                        findNavController().navigate(action)
                    }
                } else {
                    emptyTxt.isVisible(true, favoriteList)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}