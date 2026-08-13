package com.hyper.note.android

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hyper.note.android.ui.HomeScreen
import com.hyper.note.android.ui.NoteDetailScreen
import com.hyper.note.android.ui.NoteViewModel
import com.hyper.note.android.ui.NoteViewModelFactory
import com.hyper.note.android.ui.theme.MyApplicationTheme

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        val app = application as EliteNotebookApplication
        val viewModelFactory = NoteViewModelFactory(app.repository)
        val userPreferences = app.userPreferences
        
        setContent {
            val themeMode by userPreferences.theme.collectAsState()
            
            MyApplicationTheme(themeMode = themeMode) {
                Surface(
                    modifier = Modifier.fillMaxSize().safeDrawingPadding(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val prefs = getSharedPreferences("user_prefs", android.content.Context.MODE_PRIVATE)
                    val lastCrash = prefs.getString("last_crash", null)
                    
                    if (lastCrash != null) {
                        androidx.compose.foundation.layout.Column(
                            modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState())
                        ) {
                            androidx.compose.material3.Text("CRASH LOG:", color = androidx.compose.ui.graphics.Color.Red)
                            androidx.compose.material3.Text(lastCrash, color = MaterialTheme.colorScheme.onBackground)
                            androidx.compose.material3.Button(onClick = {
                                prefs.edit().remove("last_crash").apply()
                                recreate()
                            }) {
                                androidx.compose.material3.Text("Clear Crash Log")
                            }
                        }
                    } else {
                        val viewModel: NoteViewModel = viewModel(factory = viewModelFactory)
                        val notes by viewModel.notes.collectAsState()
                        
                        val encryptionKey by userPreferences.encryptionKey.collectAsState()
                        val enableBiometrics by userPreferences.enableBiometrics.collectAsState()
                        var isUnlocked by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }
                        
                        if (!isUnlocked) {
                            com.hyper.note.android.ui.AppLockScreen(
                                correctPin = encryptionKey,
                                enableBiometrics = enableBiometrics,
                                onUnlocked = { isUnlocked = true }
                            )
                        } else {
                            val navController = rememberNavController()
                            NavHost(navController = navController, startDestination = "home") {
                                composable("home") {
                                    HomeScreen(
                                        notes = notes,
                                        userPreferences = userPreferences,
                                        onAddNote = {
                                            navController.navigate("note_detail/-1")
                                        },
                                        onNoteClick = { note ->
                                            navController.navigate("note_detail/${note.id}")
                                        },
                                        onEraseAll = {
                                            viewModel.deleteAllNotes()
                                        }
                                    )
                                }
                                composable("note_detail/{noteId}") { backStackEntry ->
                                    val noteId = backStackEntry.arguments?.getString("noteId")?.toIntOrNull() ?: -1
                                    val note = notes.find { it.id == noteId }
                                    NoteDetailScreen(
                                        note = note,
                                        onSave = { title, content, isVoice ->
                                            if (note != null) {
                                                viewModel.updateNote(note.copy(title = title, content = content, isVoiceNote = isVoice))
                                            } else {
                                                if (title.isNotEmpty() || content.isNotEmpty()) {
                                                    viewModel.addNote(title, content, isVoice)
                                                }
                                            }
                                        },
                                        onBack = {
                                            navController.popBackStack()
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
