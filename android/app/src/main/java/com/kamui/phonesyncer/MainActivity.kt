package com.kamui.phonesyncer

import android.annotation.SuppressLint
import android.app.Activity
import android.app.role.RoleManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.room.*

@Composable
fun ServerModal(title: String, close: () -> (Unit), onChange: (Server) -> (Unit), server: Server? = null) {
    var serverName by remember { mutableStateOf(server?.name ?: "") }
    var serverAddress by remember { mutableStateOf(server?.address ?: "") }
    var serverPort by remember { mutableStateOf(server?.port?.toString() ?: "8080") }
    AlertDialog(
        onDismissRequest = {  },
        title = { Text(title) },
        text = {
            Column {
                TextField(value = serverName,
                    isError = serverName.isEmpty(),
                    onValueChange = { serverName = it },
                    modifier = Modifier.padding(vertical = 10.dp),
                    singleLine = true,
                    label = {
                        Text("Device Name")
                    })
                TextField(value = serverAddress,
                    isError = !IPAddressValidator().validate(serverAddress),
                    onValueChange = { serverAddress = it },
                    singleLine = true,
                    label = {
                        Text("IPV4 Host")
                    })
                TextField(value = serverPort,
                    isError = serverPort.toInt() !in 1..655355,
                    onValueChange = { serverPort = it },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    label = {
                        Text("Port")
                    })
            }
        },
        confirmButton = {
            TextButton(onClick = {
                if (serverName.isNotEmpty() && IPAddressValidator().validate(
                        serverAddress
                    )
                ) {
                    onChange(Server(serverName, serverAddress, serverPort.toInt()))
                    close()
                }
            }) { Text(text = "OK") }
        },
        dismissButton = {
            TextButton(onClick = {
                close()
            }) { Text(text = "Cancel") }
        },
    )

}


class MainActivity : ComponentActivity() {
    companion object {
        var db: AppDatabase? = null
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun requestScreeningRole() {
        val roleManager = getSystemService(Context.ROLE_SERVICE) as RoleManager
        val isHeld = roleManager.isRoleHeld(RoleManager.ROLE_CALL_SCREENING)
        if (!isHeld) {
            // ask the user to set your app as the default screening app
            val intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_CALL_SCREENING)
            getResult.launch(intent)
        } else {
            // you are already the default screening app!
        }
    }

    private val getResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                // now the default screening app
//                println("SET TO DEFAULT SCREENING APP")
            } else {
                // declined
//                println("DECLINED PERM REQUEST")
            }

        }

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestScreeningRole()
        db = AppDatabase.getInstance(applicationContext)

        setContent {
            PhoneSyncerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background
                ) {
                    // state
                    val openDialog = remember { mutableStateOf(false) }
                    var serverList  by remember { mutableStateOf(db!!.serverDao().getAll()) }

                    fun syncData() {
                        serverList = db!!.serverDao().getAll()
                    }

                    Scaffold(topBar = {
                        TopAppBar(backgroundColor = blackColor, title = {
                            Text(
                                text = "Server List",
                                modifier = Modifier.fillMaxWidth(),
                                color = Color.White
                            )
                        })
                    },
                        floatingActionButton = {
                            FloatingActionButton(onClick = {
                                openDialog.value = true
                            }) {
                                Icon(Icons.Filled.Add, "")
                            }
                        },
                        floatingActionButtonPosition = FabPosition.End,
                        isFloatingActionButtonDocked = true
                    ) {
                        ServerList(serverList, ::syncData)
                        if (openDialog.value) {
                            ServerModal(title = "Add a new server", close = { openDialog.value = false }, onChange = { server ->
                                val serverDao = db!!.serverDao()
                                serverDao.insert(server)
                                syncData()
                            })
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ServerList(servers: List<Server>, syncServers: () -> (Unit)) {
    val openDialog = remember { mutableStateOf(false) }
    var selectedServer by remember { mutableStateOf(Server("", "", 8080)) }
    var serverName by remember { mutableStateOf("") }
    var serverAddress by remember { mutableStateOf("") }
    Column {
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            itemsIndexed(items = servers, itemContent = { index, server ->
                var showMenu by remember { mutableStateOf(false) }
                ListItem(modifier = Modifier.clickable(onClick = {}),
                    text = { Text(server.name) },
                    secondaryText = { Text(server.address) },
                    trailing = {
                        IconButton(onClick = { showMenu = !showMenu }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "Options")
                        }
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            DropdownMenuItem(onClick = {
                                selectedServer = server
                                serverName = server.name
                                serverAddress = server.address
                                openDialog.value = true
                                showMenu = false
                            }) {
                                Text("Edit")
                            }
                            DropdownMenuItem(onClick = {
                                val serverDao = MainActivity.db!!.serverDao()
                                serverDao.delete(server)
                                syncServers()
                                showMenu = false
                            }) {
                                Text("Delete")
                            }
                        }
                    }
                )
                if (index < servers.lastIndex) Divider()
            })
        }
        if (openDialog.value) {
            ServerModal(title = "Edit server", close = { openDialog.value = false }, onChange = { server ->
                val serverDao = MainActivity.db!!.serverDao()
                serverDao.update(server)
                syncServers()
            }, selectedServer)
        }
    }
}