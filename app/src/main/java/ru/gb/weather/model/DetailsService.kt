package ru.gb.weather.model

import android.app.IntentService
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager

class DetailsService : IntentService("DetailService") {

    override fun onHandleIntent(intent: Intent?) {
        val repositoryImpl = RepositoryImpl()
        val city = intent?.getParcelableExtra<City>("EXTRA_CITY_FOR_SERVICE")
        val weather = city?.let { repositoryImpl.getWeatherFromServer(it) }
        val broadcastIntent = Intent("DETAILS_INTENT_FILTER")
        broadcastIntent.putExtra("EXTRA_WEATHER_FOR_BROADCAST_RECEIVER", weather)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
    }
}