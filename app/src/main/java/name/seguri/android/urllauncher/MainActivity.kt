package name.seguri.android.urllauncher

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import name.seguri.android.urllauncher.ui.theme.URLLauncherTheme

class MainActivity : ComponentActivity() {
  private lateinit var sharedPreferences: SharedPreferences

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    sharedPreferences = getPreferences(Context.MODE_PRIVATE)

    if (isFirstLaunch() || getSavedUrl().isEmpty()) {
      setContent {
        URLLauncherTheme {
          Scaffold { paddingValues ->
            Column(
              modifier = Modifier.padding(paddingValues).fillMaxSize(),
              verticalArrangement = Arrangement.Center,
              horizontalAlignment = Alignment.CenterHorizontally,
            ) {
              var url by remember { mutableStateOf(getString(R.string.default_url)) }

              OutlinedTextField(
                value = url,
                onValueChange = { url = it },
                label = { Text(getString(R.string.enter_url)) },
              )

              Spacer(modifier = Modifier.height(16.dp))

              Button(
                onClick = {
                  saveUrl(url)
                  launchUrl(url)
                }
              ) {
                Text(getString(R.string.save_and_launch))
              }
            }
          }
        }
      }
    } else {
      launchUrl(getSavedUrl())
    }
  }

  private fun isFirstLaunch(): Boolean {
    val isFirstLaunch = sharedPreferences.getBoolean(getString(R.string.first_launch_key), true)
    if (isFirstLaunch) {
      with(sharedPreferences.edit()) {
        putBoolean("first_launch", false)
        apply()
      }
    }
    return isFirstLaunch
  }

  private fun getSavedUrl(): String {
    return sharedPreferences.getString(getString(R.string.saved_url_key), "") ?: ""
  }

  private fun saveUrl(url: String) {
    with(sharedPreferences.edit()) {
      putString(getString(R.string.saved_url_key), url)
      apply()
    }
  }

  private fun launchUrl(url: String) {
    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    finish()
  }
}
