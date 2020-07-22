package app.actionmobile.usertracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.onelogin.oidc.OIDCConfiguration

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        OIDCConfiguration.Builder()
            .clientId("32044570-ad95-0138-e94b-0ac5d5c515b4173933")
            .issuer("https://actionmobile-dev.onelogin.com/oidc/2")
            .redirectUrl("https://app.actionmobile.usertracker://callback")
            .scopes(listOf("openid"))
            .isDebug(true)
            .build()

    }
}