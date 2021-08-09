package ru.gb.weather.view.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import ru.gb.weather.R
import ru.gb.weather.databinding.FragmentMainBinding
import ru.gb.weather.model.Weather
import ru.gb.weather.view.details.DetailsFragment
import ru.gb.weather.viewmodel.AppState
import ru.gb.weather.viewmodel.MainViewModel

class MainFragment : Fragment() {
    companion object {
        fun newInstance() = MainFragment()
    }

    fun interface OnItemViewClickListener {
        fun onItemViewClick(weather: Weather)
    }

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }
    private val adapter = MainFragmentAdapter { viewModel.getWeatherFromRemoteSource(it.city) }

    private var isDataSetRus: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mainFragmentRecyclerView.adapter = adapter
        binding.mainFragmentFAB.setOnClickListener { changeWeatherDataSet() }
        viewModel.getLiveData().observe(viewLifecycleOwner, { renderData(it) })
        viewModel.getWeatherFromLocalSourceRus()
    }

    private fun changeWeatherDataSet() = when {
        isDataSetRus -> {
            viewModel.getWeatherFromLocalSourceWorld()
            binding.mainFragmentFAB.setImageResource(R.drawable.russia_b)
            binding.imageView.setImageResource(R.drawable.world)
            binding.imageView.alpha = 0.4F
        }
        else -> {
            viewModel.getWeatherFromLocalSourceRus()
            binding.mainFragmentFAB.setImageResource(R.drawable.world_b)
            binding.imageView.setImageResource(R.drawable.home)
            binding.imageView.alpha = 0.85F
        }
    }.also {
        isDataSetRus = !isDataSetRus
    }

    private fun renderData(appState: AppState) =
        when (appState) {
            is AppState.SuccessList -> {
                binding.mainFragmentLoadingLayout.visibility = View.GONE
                adapter.setWeather(appState.weatherDataList)
            }
            is AppState.SuccessWeather -> {
                openDetailsFragment(appState.weatherData)
            }
            is AppState.Loading -> {
                binding.mainFragmentLoadingLayout.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                binding.mainFragmentLoadingLayout.visibility = View.GONE
                binding.mainFragmentRootView.showSnackBar(
                    appState.error.toString(),
                    getString(R.string.reload), {
                        viewModel.getWeatherFromLocalSourceRus()
                        binding.mainFragmentFAB.setImageResource(R.drawable.world_b)
                        binding.imageView.setImageResource(R.drawable.home)
                        binding.imageView.alpha = 0.85F
                        isDataSetRus = true
                    })
            }
        }

    private fun openDetailsFragment(weather: Weather) {
        binding.mainFragmentLoadingLayout.visibility = View.GONE
        activity?.supportFragmentManager?.apply {
            beginTransaction().add(
                R.id.container,
                DetailsFragment.newInstance(Bundle().apply {
                    putParcelable(DetailsFragment.BUNDLE_EXTRA, weather)
                })
            )
                .addToBackStack("")
                .commitAllowingStateLoss()
        }
    }

    private fun View.showSnackBar(
        text: String,
        actionText: String,
        action: (View) -> Unit,
        length: Int = Snackbar.LENGTH_INDEFINITE
    ) = Snackbar.make(this, text, length).setAction(actionText, action).show()


    override fun onDestroyView() {
        _binding = null
        adapter.removeListener()
        super.onDestroyView()
    }
}