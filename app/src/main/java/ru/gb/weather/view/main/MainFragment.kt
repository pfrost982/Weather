package ru.gb.weather.view.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
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

const val REQUEST_CODE = 404
const val TAG = "@@@"

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
        binding.mainFragmentFab.setOnClickListener { getNewCity() }
        binding.mainLocationFab.setOnClickListener { getLocation() }
        viewModel.getLiveData().observe(viewLifecycleOwner, { renderData(it) })
        viewModel.getCitiesListFromLocalSource()
    }

    private fun getLocation() {
        context?.let { context ->
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                showCoordinates()
            } else {
                requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_CODE
                )
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun showCoordinates() {
        val locationManager = context?.getSystemService(LOCATION_SERVICE) as LocationManager
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            var first = true
            val provider = locationManager.getProvider(LocationManager.GPS_PROVIDER)
            Log.d(TAG, "showCoordinates: $provider")
            val locationListener = object : LocationListener {
                override fun onLocationChanged(location: Location) {
                    Log.d(TAG, "requestLocationUpdates")
                    if (first) showCoordinatesAlert(location.latitude, location.longitude)
                    first = false
                }

                override fun onProviderDisabled(provider: String) {}
                override fun onProviderEnabled(provider: String) {}
            }

            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                100,
                0f,
                locationListener
            )

        } else {
            val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if(location != null){
                showCoordinatesAlert(location.latitude, location.longitude)
            } else{
                Log.d(TAG, "showCoordinates: null")
            }
        }
    }

    private fun showCoordinatesAlert(lat: Double, lon: Double) {
        context?.let { context ->
            AlertDialog.Builder(context)
                .setTitle("Координаты: ")
                .setMessage("Широта: ${lat}\nДолгота: $lon")
                .setPositiveButton("Открыть карту") { _, _ ->
                    //todo
                }
                .setNegativeButton("Закрыть") { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)
                ) {
                    showCoordinates()
                } else {
                    context?.let {
                        AlertDialog.Builder(it)
                            .setTitle("Доступ к геолокации")
                            .setMessage("Вы не позволили приложению получить доступ к геолокации, но без этого разрешения приложение не сможет получить координаты")
                            .setNegativeButton("Закрыть") { dialog, _ -> dialog.dismiss() }
                            .create()
                            .show()
                    }
                }
                return
            }
        }
    }


    private fun getNewCity() {
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