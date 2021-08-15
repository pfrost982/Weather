package ru.gb.weather.view.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import ru.gb.weather.R
import ru.gb.weather.databinding.FragmentMainBinding
import ru.gb.weather.model.City
import ru.gb.weather.model.Weather
import ru.gb.weather.view.details.DetailsFragment
import ru.gb.weather.viewmodel.AppState
import ru.gb.weather.viewmodel.MainViewModel

class MainFragment : Fragment() {
    companion object {
        fun newInstance() = MainFragment()
    }

    fun interface OnItemViewClickListener {
        fun onItemViewClick(city: City)
    }

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }
    private val adapter = MainFragmentAdapter { viewModel.getWeatherFromRemoteSource(it) }

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
        binding.mainFragmentFab.setOnClickListener { showDialogView() }
        viewModel.getLiveData().observe(viewLifecycleOwner, { renderData(it) })
        viewModel.getCitiesListFromLocalSource()
    }

    private fun showDialogView() {
        val dialogView: View = layoutInflater.inflate(R.layout.dialog_new_city, null)
        val dialogBuilder = context?.let { AlertDialog.Builder(it) }
        dialogBuilder?.setView(dialogView)
        val inputCity: EditText = dialogView.findViewById(R.id.dialog_input_text)
        dialogBuilder
            ?.setCancelable(false)
            ?.setPositiveButton("OK") { _, _ ->
                viewModel.getWeatherFromRemoteSource(
                    City(0, inputCity.text.toString(), 0.0, 0.0),
                    true
                )
            }
            ?.setNegativeButton("Отмена") { dialog, _ -> dialog.cancel() }
        val alertDialog = dialogBuilder?.create()
        alertDialog?.show()
    }

    private fun renderData(appState: AppState) =
        when (appState) {
            is AppState.SuccessList -> {
                binding.mainFragmentLoadingLayout.visibility = View.GONE
                adapter.setWeather(appState.citiesDataList)
            }
            is AppState.SuccessWeather -> {
                openDetailsFragment(appState.weatherData, appState.newCity)
            }
            is AppState.Loading -> {
                binding.mainFragmentLoadingLayout.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                binding.mainFragmentLoadingLayout.visibility = View.GONE
                binding.mainFragmentRootView.showSnackBar(
                    appState.error.toString(),
                    getString(R.string.reload), {
                        viewModel.getCitiesListFromLocalSource()
                    })
            }
        }

    private fun openDetailsFragment(weather: Weather, newCity: Boolean = false) {
        binding.mainFragmentLoadingLayout.visibility = View.GONE
        activity?.supportFragmentManager?.apply {
            beginTransaction().add(
                R.id.container,
                DetailsFragment.newInstance(viewModel, Bundle().apply {
                    putParcelable(DetailsFragment.EXTRA_WEATHER, weather)
                    putBoolean(DetailsFragment.EXTRA_NEW_CITY, newCity)
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