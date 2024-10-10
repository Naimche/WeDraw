package com.bupware.wedraw.android.ui.widget

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.bupware.wedraw.android.roomData.tables.image.Image
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


const val IMAGE_KEY = "image_key"


class WeDrawPreferences(private val context: Context) {
    companion object {
        const val WIMAGE_KEY = "wimage_key"
        val Context.dataStore : DataStore<Preferences> by preferencesDataStore(name = "wedraw_preferences")
    }

    //to get the uri
    val getUri: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[stringPreferencesKey(IMAGE_KEY)] ?: ""
    }

    //to set the uri
    suspend fun setUri(uri: String) {
        context.dataStore.edit { preferences ->
            preferences[stringPreferencesKey(WIMAGE_KEY)] = uri
        }
    }
}

fun MutablePreferences.setImageData(image: Image) {
    this[stringPreferencesKey(IMAGE_KEY)] = image.uri
}


