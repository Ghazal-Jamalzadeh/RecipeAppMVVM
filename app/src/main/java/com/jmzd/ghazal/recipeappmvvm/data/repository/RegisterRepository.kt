package com.jmzd.ghazal.recipeappmvvm.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.jmzd.ghazal.recipeappmvvm.data.source.RemoteDataSource
import com.jmzd.ghazal.recipeappmvvm.models.register.BodyRegister
import com.jmzd.ghazal.recipeappmvvm.models.register.RegisterStoredModel
import com.jmzd.ghazal.recipeappmvvm.utils.Constants
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

@ActivityRetainedScoped
class RegisterRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val remote: RemoteDataSource
) {
    //Store user info
    private object StoredKeys {
        val username : Preferences.Key<String> = stringPreferencesKey(Constants.REGISTER_USERNAME)
        val hash : Preferences.Key<String> = stringPreferencesKey(Constants.REGISTER_HASH)
    }

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(Constants.REGISTER_USER_INFO)

    suspend fun saveRegisterData(username: String, hash: String) {
        context.dataStore.edit { preferences : MutablePreferences ->
            preferences[StoredKeys.username] = username
            preferences[StoredKeys.hash] = hash
        }
    }

    val readRegisterData: Flow<RegisterStoredModel> = context.dataStore.data
        .catch { e : Throwable ->
            if (e is IOException) {
                emit(emptyPreferences())
            } else {
                throw e
            }
        }.map { preferences : Preferences ->
            val username = preferences[StoredKeys.username] ?: "" //elvis condition
            val hash = preferences[StoredKeys.hash] ?: ""

            /*میتونیم خط آخر رو بدون ریترن به این شکل هم بنویسیم. خودش میفهمه باید ریترن شه */
            /* از کجا میفهمه؟ چون بالا = قرار دادیم */
            /* مساوی خودش به معنی ریترن هست*/
            //RegisterStoredModel(username, hash)

            return@map RegisterStoredModel(username, hash)
        }
    //API
    suspend fun postRegister(apiKey: String, body: BodyRegister) = remote.postRegister(apiKey, body)
}