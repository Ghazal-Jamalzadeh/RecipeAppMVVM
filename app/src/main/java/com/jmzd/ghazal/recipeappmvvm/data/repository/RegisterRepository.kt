package com.jmzd.ghazal.recipeappmvvm.data.repository

import android.content.Context
import com.jmzd.ghazal.recipeappmvvm.data.source.RemoteDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class RegisterRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val remote: RemoteDataSource
) {

    //Store user info
//    private object StoredKeys {
//        val username = stringPreferencesKey(Constants.REGISTER_USERNAME)
//        val hash = stringPreferencesKey(Constants.REGISTER_HASH)
//    }

//    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(Constants.REGISTER_USER_INFO)

//    suspend fun saveRegisterData(username: String, hash: String) {
//        context.dataStore.edit {
//            it[StoredKeys.username] = username
//            it[StoredKeys.hash] = hash
//        }
//    }

//    val readRegisterData: Flow<RegisterStoredModel> = context.dataStore.data
//        .catch { e ->
//            if (e is IOException) {
//                emit(emptyPreferences())
//            } else {
//                throw e
//            }
//        }.map {
//            val username = it[StoredKeys.username] ?: ""
//            val hash = it[StoredKeys.hash] ?: ""
//            RegisterStoredModel(username, hash)
//        }
//
//    //API
//    suspend fun postRegister(apiKey: String, body: BodyRegister) = remote.postRegister(apiKey, body)
}