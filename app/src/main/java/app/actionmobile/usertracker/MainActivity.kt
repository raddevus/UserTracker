package app.actionmobile.usertracker

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.onelogin.oidc.Callback
import com.onelogin.oidc.OIDCConfiguration
import com.onelogin.oidc.OneLoginOIDC
import com.onelogin.oidc.login.SignInError
import com.onelogin.oidc.login.SignInSuccess

class MainActivity : AppCompatActivity() {
    var signInButton : Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val config = OIDCConfiguration.Builder()
            .clientId("32044570-ad95-0138-e94b-0ac5d5c515b4173933")
            .issuer("https://actionmobile-dev.onelogin.com/oidc/2")
            .redirectUrl("https://app.actionmobile.usertracker://callback")
            .scopes(listOf("openid"))
            .isDebug(true)
            .build()

        signInButton = findViewById(R.id.signInButton) as Button

        signInButton!!.setOnClickListener {
            Log.d("MainActivity", "sign-in button clicked.")
            OneLoginOIDC.initialize(this.applicationContext,config)
            val oidcClient = OneLoginOIDC.getClient()
            Log.d("MainActivity", "got client...")
            oidcClient.signIn(this,object : Callback<SignInSuccess, SignInError> {
                override fun onSuccess(success: SignInSuccess) {
                    // The user has been authenticated successfully,
                    // the `success` param will contain the `SessionInfo` with the tokens ready to be used
                    Log.d("MainActivity", "token: $success.sessionInfo.idToken");
                }

                override fun onError(loginError: SignInError) {
                    Log.d("MainActivity", "message: $loginError.message");
                }
            });
        }

    }
}