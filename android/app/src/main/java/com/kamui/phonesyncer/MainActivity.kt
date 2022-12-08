package com.kamui.phonesyncer

import android.app.Activity
import android.app.role.RoleManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.kamui.phonesyncer.ui.theme.PhoneSyncerTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.Q)
    private fun requestScreeningRole(){
        val roleManager = getSystemService(Context.ROLE_SERVICE) as RoleManager
        val isHeld = roleManager.isRoleHeld(RoleManager.ROLE_CALL_SCREENING)
        if(!isHeld){
            // ask the user to set your app as the default screening app
            val intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_CALL_SCREENING)
            getResult.launch(intent)
        } else {
            // you are already the default screening app!
            println("ALREADY DEFAULT SCREENING APP")
        }
    }

    private val getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            // now the default screening app
            println("SET TO DEFAULT SCREENING APP")
        } else {
            // declined
            println("DECLINED PERM REQUEST")
        }

    }
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestScreeningRole()
        setContent {
            PhoneSyncerTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PhoneSyncerTheme {
        Greeting("Android")
    }
}