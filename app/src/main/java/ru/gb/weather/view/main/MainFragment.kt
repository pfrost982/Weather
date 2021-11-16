package ru.gb.weather.view.main

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.material.snackbar.Snackbar
import ru.gb.weather.R
import ru.gb.weather.databinding.FragmentMainBinding
import ru.gb.weather.model.DetailsService
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
    private val adapter = MainFragmentAdapter { weather ->
        binding.mainFragmentLoadingLayout.visibility = View.VISIBLE
        context?.let { context ->
            context.startService(Intent(context, DetailsService::class.java).apply {
                putExtra("EXTRA_CITY_FOR_SERVICE", weather.city)
            })
        }
        //viewModel.getWeatherFromRemoteSource(weather.city)
    }

    private val loadResultsReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val weather = intent.getParcelableExtra<Weather>("EXTRA_WEATHER_FOR_BROADCAST_RECEIVER")
            weather?.let { openDetailsFragment(it) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.let {
            LocalBroadcastManager.getInstance(it)
                .registerReceiver(loadResultsReceiver, IntentFilter("DETAILS_INTENT_FILTER"))
        }
    }

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
                    getString(R.string.error),
                    getString(R.string.reload),
                    { viewModel.getWeatherFromLocalSourceRus() })
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
        context?.let {
            LocalBroadcastManager.getInstance(it).unregisterReceiver(loadResultsReceiver)
        }
        super.onDestroyView()
    }
}