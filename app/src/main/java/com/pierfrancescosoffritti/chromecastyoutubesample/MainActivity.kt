package com.pierfrancescosoffritti.chromecastyoutubesample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import com.google.android.gms.cast.framework.*
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var castSession: CastSession
    private lateinit var sessionManager: SessionManager
    private val sessionManagerListener = MySessionManagerListener()

    override fun onCreate(savedInstanceState: Bundle?) {
        sessionManager = CastContext.getSharedInstance(this).sessionManager;

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        CastButtonFactory.setUpMediaRouteButton(applicationContext, media_route_button)
        val castContext = CastContext.getSharedInstance(this)

    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        super.onCreateOptionsMenu(menu)
//        menuInflater.inflate(R.menu.cast_menu, menu)
//        CastButtonFactory.setUpMediaRouteButton(applicationContext, menu, R.id.media_route_menu_item)
//        return true
//    }

    override fun onResume() {
        if(sessionManager.currentCastSession != null)
            castSession = sessionManager.currentCastSession

        sessionManager.addSessionManagerListener(sessionManagerListener)
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        sessionManager.removeSessionManagerListener(sessionManagerListener)
//        castSession = null
    }
}
