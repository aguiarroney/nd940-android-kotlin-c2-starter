package com.udacity.asteroidradar.main

import android.app.Application
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment(), MainFragmentAdapter.OnAsteroidItemClickListener {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(
            this, MainViewModel.Factory(
                requireActivity().application
            )
        ).get(MainViewModel::class.java)
    }

    private val asteroidAdapter by lazy {
        MainFragmentAdapter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        binding.asteroidRecycler.adapter = asteroidAdapter

        setHasOptionsMenu(true)

        viewModel.fetchPichOfTheDay()
        viewModel.fetchAsteroidsOnline()
        viewModel.fetchAsteroidsFromDB()

        viewModel.pictureOfDay.observe(viewLifecycleOwner, Observer {
            it?.let {
                Picasso.with(context).load(it).into(binding.activityMainImageOfTheDay)
            }
        })

        viewModel.asteroidList.observe(viewLifecycleOwner, Observer {
            asteroidAdapter.addAsteroidList(it as ArrayList<Asteroid>)
        })

        viewModel.navigationToDetail.observe(viewLifecycleOwner, Observer { id ->
            id?.let {
                val asteroid: Asteroid? = viewModel.asteroidList.value?.get(id.toInt())
                this.findNavController()
                    .navigate(MainFragmentDirections.actionMainFragmentToDetailFragment(asteroid!!))
                viewModel.onNavigationEnd()
            }
        })

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }

    override fun onItemClick(position: Long) {
        Log.i("onItemClick", "${viewModel.asteroidList.value?.get(position.toInt())?.codename}")
        viewModel.navigationToDetail.value = position
    }
}
