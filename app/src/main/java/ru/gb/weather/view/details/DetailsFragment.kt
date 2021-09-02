package ru.gb.weather.view.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import ru.gb.weather.R
import ru.gb.weather.databinding.FragmentDetailsBinding
import ru.gb.weather.model.Weather
import ru.gb.weather.viewmodel.MainViewModel

class DetailsFragment(
    private val viewModel: MainViewModel,
    val weather: Weather,
    var newCity: Boolean
) : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!newCity) {
            binding.detailsFragmentFab.setImageResource(R.drawable.ic_baseline_location_off_24)
        }

        binding.cityName.text = weather.city.cityName
        binding.cityCoordinates.text = String.format(
            getString(R.string.city_coordinates),
            weather.city.lat.toString(),
            weather.city.lon.toString()
        )

        binding.temperatureValue.text = weather.temperature.toString()
        binding.feelsLikeValue.text = weather.feelsLike.toString()
        binding.description.text = weather.description
        Glide.with(this)
            .load("https://openweathermap.org/img/wn/${weather.icon}@4x.png")
            .centerCrop()
            .into(binding.imageView)

        binding.detailsFragmentFab.setOnClickListener {
            newCity = if (newCity == false) {
                viewModel.deleteCity(weather.city)
                binding.detailsFragmentFab.setImageResource(R.drawable.ic_baseline_add_24)
                true
            } else {
                viewModel.addCity(weather.city)
                binding.detailsFragmentFab.setImageResource(R.drawable.ic_baseline_location_off_24)
                false
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}

