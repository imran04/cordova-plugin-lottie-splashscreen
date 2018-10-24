package de.dustplanet.cordova.lottie

import android.app.Dialog
import android.util.Log
import android.view.ViewGroup.LayoutParams
import android.widget.LinearLayout
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import org.apache.cordova.CallbackContext
import org.apache.cordova.CordovaArgs
import org.apache.cordova.CordovaPlugin

class LottieSplashScreen : CordovaPlugin() {

    private var splashDialog: Dialog? = null
    private var animationView: LottieAnimationView? = null

    override fun pluginInitialize() {
        super.pluginInitialize()
        Log.d(LOG_TAG, "pluginInitialize: called")
        foo()
    }

    override fun execute(action: String?, args: CordovaArgs?, callbackContext: CallbackContext?): Boolean {
        return super.execute(action, args, callbackContext)
    }

    private fun foo() {
        cordova.activity.runOnUiThread(object : Runnable {
            override fun run() {
                val display = cordova.activity.windowManager.defaultDisplay
                val context = webView.context

                animationView = LottieAnimationView(context)
                val remoteEnabled = preferences.getBoolean("LottieRemoteEnabled", false)
                val location = preferences.getString("LottieAnimationLocation", "")
                if (remoteEnabled) {
                    animationView!!.setAnimationFromUrl(location)
                } else {
                    animationView!!.setAnimation(location)
                }
                animationView!!.repeatCount = LottieDrawable.INFINITE

                val layoutParams = LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
                animationView!!.layoutParams = layoutParams

                animationView!!.minimumHeight = preferences.getInteger("LottieHeight", 200)
                Log.d(LOG_TAG, preferences.getInteger("LottieHeight", 200).toString())
                animationView!!.maxHeight = preferences.getInteger("LottieHeight", 200)
                animationView!!.minimumWidth = preferences.getInteger("LottieWidth", 200)
                animationView!!.maxWidth = preferences.getInteger("LottieWidth", 200)

                animationView!!.setBackgroundColor(ColorHelper.parseColor(preferences.getString("LottieBackgroundColor", "#ffffff")))

                splashDialog = Dialog(context, android.R.style.Theme_Translucent_NoTitleBar)
                splashDialog!!.setContentView(animationView!!)
                splashDialog!!.setCancelable(false)
                splashDialog!!.show()


                animationView!!.playAnimation()
                animationView!!.setOnClickListener {
                    val cancelOnTap = preferences.getBoolean("LottieCancelOnTap", false)
                    if (cancelOnTap) {
                        splashDialog!!.dismiss()
                    }
                }
            }
        })
    }

    companion object {
        private val LOG_TAG = "LottieSplashScreen"
    }
}
