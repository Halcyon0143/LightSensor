package com.example.lightsensor

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import com.mikhaellopez.circularprogressbar.CircularProgressBar

class MainActivity : AppCompatActivity(), SensorEventListener {

    //lateinit->is used for properties which will be initialized later

    private lateinit var sensorManager: SensorManager //instance of sensorManager used to access sensor
    private var brightness: Sensor? = null
    private lateinit var text: TextView
    private lateinit var pb: CircularProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        text = findViewById(R.id.tv_text)
        pb = findViewById(R.id.circularProgressBar)

        setUpSensorStuff()
    }
    private fun setUpSensorStuff(){

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager //basiclly use a system service in this case sensor services of android and store an instace in sensorManager for accessing and working on them

        brightness = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
    }

//contains info about the sensor that caused the event and the new sensor values
    override fun onSensorChanged(event: SensorEvent?) {
    if (event?.sensor?.type == Sensor.TYPE_LIGHT) {
        val light1 = event.values[0]

        text.text = "Sensor: $light1\n${brightness(light1)}"
        pb.setProgressWithAnimation(light1)
    }
    }
    private fun brightness(brightness: Float): String {

        return when (brightness.toInt()) {
            0 -> "Pitch black"
            in 1..10 -> "Dark"
            in 11..50 -> "Grey"
            in 51..5000 -> "Normal"
            in 5001..25000 -> "Incredibly bright"
            else -> "This light will blind you"
        }
    }

//this is called when accuracy of the sensor changes
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
       return
    }

    override fun onResume() {
        super.onResume()
        // Register a listener for the sensor.
        sensorManager.registerListener(this, brightness, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

}