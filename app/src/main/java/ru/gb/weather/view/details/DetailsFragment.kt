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

class DetailsFragment : Fragment() {

    companion object {
        const val EXTRA_WEATHER = "EXTRA_WEATHER"
        const val EXTRA_NEW_CITY = "EXTRA_CITY"
        private var viewModelS: MainViewModel? = null
        fun newInstance(viewModel: MainViewModel, bundle: Bundle): DetailsFragment {
            viewModelS = viewModel
            return DetailsFragment().apply {
                this.arguments = bundle
            }
        }
    }

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
        val newCity = arguments?.getBoolean(EXTRA_NEW_CITY)
        arguments?.getParcelable<Weather>(EXTRA_WEATHER)?.let { weather ->
            if (newCity == true){
                viewModelS?.addCity(weather)
            }
            weather.city.also { city ->
                binding.cityName.text = city.cityName
                binding.cityCoordinates.text = String.format(
                    getString(R.string.city_coordinates),
                    city.lat.toString(),
                    city.lon.toString()
                )
                binding.temperatureValue.text = weather.temperature.toString()
                binding.feelsLikeValue.text = weather.feelsLike.toString()
                binding.description.text = weather.description
                Glide.with(this)
                    .load("https://openweathermap.org/img/wn/${weather.icon}@4x.png")
                    .centerCrop()
                    .into(binding.imageView)
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
