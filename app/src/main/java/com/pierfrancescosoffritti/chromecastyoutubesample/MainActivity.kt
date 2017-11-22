package com.pierfrancescosoffritti.chromecastyoutubesample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.cast.framework.*
import kotlinx.android.synthetic.main.activity_main.*
import com.google.android.gms.cast.framework.CastContext

class MainActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    private lateinit var sessionManagerListener: SessionManagerListener<Session>
    private val customChannel = CustomChannel()

    override fun onCreate(savedInstanceState: Bundle?) {
        sessionManager = CastContext.getSharedInstance(this).sessionManager
        sessionManagerListener = MySessionManagerListener(this, sessionManager, customChannel)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        CastButtonFactory.setUpMediaRouteButton(applicationContext, media_route_button)
        val castContext = CastContext.getSharedInstance(this)
    }

    override fun onResume() {
        sessionManager.addSessionManagerListener(sessionManagerListener)
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        sessionManager.removeSessionManagerListener(sessionManagerListener)
    }

    fun sendMessage(message: String) = try {
        sessionManager.currentCastSession
                .sendMessage(customChannel.namespace, message)
                .setResultCallback{
                    if(it.isSuccess)
                        Log.d(this.javaClass.simpleName, "message sent")
                }
    } catch (e: Exception) {
        throw RuntimeException(e)
    }
}
