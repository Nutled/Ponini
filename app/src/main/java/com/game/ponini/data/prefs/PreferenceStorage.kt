package com.game.ponini.data.prefs

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.WorkerThread
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.game.ponini.BuildConfig
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Created by Viнt@rь on 28.01.2020
 */
interface PreferenceStorage {
    var notifications: Boolean
    var observableNotifications: LiveData<Boolean>
}

class SharedPreferenceStorage constructor(private val context: Context) : PreferenceStorage {

    companion object {
        private const val NOTIFICATIONS = "notifications"
    }

    private val _observableNotifications = MutableLiveData<Boolean>()

    private val prefs: Lazy<SharedPreferences> = lazy {
        // Lazy to prevent IO access to main thread.
        context.applicationContext.getSharedPreferences(
            BuildConfig.APPLICATION_ID,
            Context.MODE_PRIVATE
        ).apply {
            registerOnSharedPreferenceChangeListener(changeListener)
        }
    }
    private val changeListener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
        when (key) {
            NOTIFICATIONS -> _observableNotifications.value = notifications
        }
    }

    override var notifications by BooleanPreference(prefs, NOTIFICATIONS, false)
    override var observableNotifications: LiveData<Boolean>
        get() {
            _observableNotifications.value = notifications
            return _observableNotifications
        }
        set(_) = throw IllegalAccessException("This property can't be changed")
}

class BooleanPreference(
    private val preferences: Lazy<SharedPreferences>,
    private val name: String,
    private val defaultValue: Boolean
) : ReadWriteProperty<Any, Boolean> {

    @WorkerThread
    override fun getValue(thisRef: Any, property: KProperty<*>): Boolean {
        return preferences.value.getBoolean(name, defaultValue)
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Boolean) {
        preferences.value.edit { putBoolean(name, value) }
    }
}