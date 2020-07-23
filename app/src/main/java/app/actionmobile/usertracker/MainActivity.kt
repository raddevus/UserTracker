package app.actionmobile.usertracker

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.onelogin.oidc.Callback
import com.onelogin.oidc.OIDCClient
import com.onelogin.oidc.OIDCConfiguration
import com.onelogin.oidc.OneLoginOIDC
import com.onelogin.oidc.data.stores.OneLoginEncryptionManager
import com.onelogin.oidc.login.SignInError
import com.onelogin.oidc.login.SignInSuccess
import com.onelogin.oidc.revoke.RevokeError
import com.onelogin.oidc.revoke.RevokeSuccess
import com.onelogin.oidc.userInfo.UserInfo
import com.onelogin.oidc.userInfo.UserInfoError

class MainActivity : AppCompatActivity() {
    var signInButton : Button? = null
    var signOutButton : Button? = null
    var userTextView : TextView? = null
    var oidcClient : OIDCClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val config = OIDCConfiguration.Builder()
            .clientId("dab1c550-af23-0138-a003-0615bd700714174077")
            .issuer("https://actionmobile-dev.onelogin.com/oidc/2")
            .redirectUrl("app.actionmobile.usertracker://callback")
            .scopes(listOf("openid"))
            .isDebug(true)
            .build()

        signInButton = findViewById(R.id.signInButton) as Button
        signOutButton = findViewById(R.id.signOutbutton) as Button
        userTextView = findViewById(R.id.userTextView) as TextView

        signInButton!!.setOnClickListener {
            Log.d("MainActivity", "sign-in button clicked.")
            OneLoginOIDC.initialize(this.applicationContext,config)
            oidcClient = OneLoginOIDC.getClient()

            Log.d("MainActivity", "got client...")
            oidcClient!!.signIn(this,object : Callback<SignInSuccess, SignInError> {
                override fun onSuccess(success: SignInSuccess) {
                    // The user has been authenticated successfully,
                    // the `success` param will contain the `SessionInfo` with the tokens ready to be used
                    Log.d("MainActivity", "token: $success.sessionInfo.idToken");
                    displayUserName(oidcClient)
                    //Log.d("MainActivity", success.sessionInfo)
                }

                override fun onError(loginError: SignInError) {
                    Log.d("MainActivity", "message: $loginError.message : $loginError.cause");

                }
            });
        }

        signOutButton!!.setOnClickListener {
            Log.d("MainActivity", "sign-out button clicked")
            // insure oidcClient is initialized
            if (oidcClient == null){
                OneLoginOIDC.initialize(this.applicationContext,config)
                oidcClient = OneLoginOIDC.getClient()
            }

            Log.d("MainActivity", "initialized")
            try {
                oidcClient!!.revokeToken(object : Callback<RevokeSuccess, RevokeError> {
                    override fun onSuccess(success: RevokeSuccess) {
                        Log.d("MainActivity", "token: $success.sessionInfo.idToken");
                        userTextView!!.setText("");
                    }

                    override fun onError(error: RevokeError) {
                        Log.d("MainActivity", "message: $error.message");
                    }
                })
            }
            catch (ex : Exception){
                Log.d("MainActivity", "Error : ${ex.message}")
            }
        }

    }

    fun displayUserName(oidcClient: OIDCClient?){
        oidcClient!!.getUserInfo(object : Callback<UserInfo,UserInfoError>{
            override fun onSuccess(success: UserInfo) {
                Log.d("MainActivity","${success.name} : ${success}")
                userTextView!!.setText("${success.name}")
            }
            override fun onError(error: UserInfoError){
                Log.d("MainActivity", "info error")
            }
        })
    }
}