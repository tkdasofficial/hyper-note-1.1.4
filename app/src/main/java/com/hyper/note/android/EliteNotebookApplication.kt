package com.hyper.note.android

import android.app.Application
import android.content.Context
import com.hyper.note.android.data.NoteDatabase
import com.hyper.note.android.data.NoteRepository
import com.hyper.note.android.security.CryptoManager
import java.io.PrintWriter
import java.io.StringWriter

class EliteNotebookApplication : Application() {
    val database by lazy { NoteDatabase.getDatabase(this) }
    val cryptoManager by lazy { CryptoManager() }
    val repository by lazy { NoteRepository(database.noteDao(), cryptoManager) }
    val userPreferences by lazy { com.hyper.note.android.data.UserPreferences(this) }

    override fun onCreate() {
        super.onCreate()
        val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, exception ->
            val sw = StringWriter()
            exception.printStackTrace(PrintWriter(sw))
            getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                .edit()
                .putString("last_crash", sw.toString())
                .commit()
            defaultHandler?.uncaughtException(thread, exception)
        }
    }
}
