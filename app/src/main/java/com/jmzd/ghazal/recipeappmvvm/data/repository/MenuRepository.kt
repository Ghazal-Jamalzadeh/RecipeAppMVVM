package com.jmzd.ghazal.recipeappmvvm.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.jmzd.ghazal.recipeappmvvm.models.menu.MenuStoredModel
import com.jmzd.ghazal.recipeappmvvm.utils.Constants
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import java.io.IOException
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ActivityRetainedScoped
class MenuRepository @Inject constructor(@ApplicationContext private val context: Context) {

    private object menuStoredKeys {
        val mealId: Preferences.Key<Int> = intPreferencesKey(Constants.MENU_MEAL_ID_KEY)
        val dietId: Preferences.Key<Int> = intPreferencesKey(Constants.MENU_DIET_ID_KEY)
        val selectedMeal: Preferences.Key<String> =
            stringPreferencesKey(Constants.MENU_MEAL_TITLE_KEY)
        val selectedDiet: Preferences.Key<String> =
            stringPreferencesKey(Constants.MENU_DIET_TITLE_KEY)
    }

    private val Context.menuDataStore: DataStore<Preferences> by preferencesDataStore(Constants.MENU_DATASTORE)

    suspend fun saveMenuData(meal: String, diet: String, mealId: Int, dietId: Int) {
        context.menuDataStore.edit { pref: MutablePreferences ->
            pref[menuStoredKeys.selectedMeal] = meal
            pref[menuStoredKeys.selectedDiet] = diet
            pref[menuStoredKeys.mealId] = mealId
            pref[menuStoredKeys.dietId] = dietId
        }
    }

    val menuDataFlow : Flow<MenuStoredModel> = context.menuDataStore.data
        .catch { e ->
            if (e is IOException) {
                emit(emptyPreferences())
            } else {
                throw e
            }
        }
        .map { pref: Preferences ->
            val selectMeal = pref[menuStoredKeys.selectedMeal] ?: Constants.MAIN_COURSE
            val selectMealId = pref[menuStoredKeys.mealId] ?: 0
            val selectDiet = pref[menuStoredKeys.selectedDiet] ?: Constants.GLUTEN_FREE
            val selectDietId = pref[menuStoredKeys.dietId] ?: 0
            return@map MenuStoredModel(
                meal = selectMeal,
                diet = selectDiet,
                mealId = selectMealId,
                dietId = selectDietId)
        }

}