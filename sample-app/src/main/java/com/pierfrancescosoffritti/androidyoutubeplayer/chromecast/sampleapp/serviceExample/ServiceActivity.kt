package com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.sampleapp.serviceExample

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.sampleapp.R
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.sampleapp.utils.MediaRouterButtonUtils
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.sampleapp.utils.PlayServicesUtils
import kotlinx.android.synthetic.main.activity_basic_example.*


class ServiceActivity : AppCompatActivity() {

    private val googlePlayServicesAvailabilityRequestCode = 1

    private var mediaPlayerService: MediaPlayerService? = null
    var serviceBound = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service)

        MediaRouterButtonUtils.initMediaRouteButton(media_route_button)
        PlayServicesUtils.checkGooglePlayServicesAvailability(this, googlePlayServicesAvailabilityRequestCode) { startCastService() }
    }

    public override fun onSaveInstanceState(savedInstanceState: Bundle) {
        savedInstanceState.putBoolean("ServiceState", serviceBound)
        super.onSaveInstanceState(savedInstanceState)
    }

    public override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        serviceBound = savedInstanceState.getBoolean("ServiceState")
    }

    override fun onDestroy() {
        super.onDestroy()
        if (serviceBound) {
            unbindService(serviceConnection)
            mediaPlayerService?.stopSelf()
        }
    }

    private fun startCastService() {
        if (!serviceBound) {
            val playerIntent = Intent(this, MediaPlayerService::class.java)
            bindService(playerIntent, serviceConnection, Context.BIND_AUTO_CREATE)
            startService(playerIntent)
        } else {
            Log.e(javaClass.simpleName, "service already bound")
        }
    }

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as MediaPlayerService.LocalBinder
            mediaPlayerService = binder.service
            serviceBound = true
        }

        override fun onServiceDisconnected(name: ComponentName) {
            mediaPlayerService = null
            serviceBound = false
        }
    }
}
