package com.bashsupn.projectschedule.sharedpreferences

import android.content.Context
import android.content.SharedPreferences
import com.bashsupn.projectschedule.R

class PrefManager (context : Context) {
    private val IS_LOGIN = "is_login"
    private val prefs : SharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
    var editor: SharedPreferences.Editor? = prefs?.edit()
    companion object{
        const val ACCESS_TOKEN = "token"
    }
    /**
     * Fungsi simpan access_token
     */
    fun saveAccessToken(token: String) {
        val editor = prefs.edit()
        editor.putString(ACCESS_TOKEN, token)
            .apply()
    }

    /**
     * Fungsi get access_token
     */
    fun fetchAccessToken(): String? {
        return prefs.getString(ACCESS_TOKEN, null)
    }

    /**
     * Fungsi set login
     */
    fun setLoggin(isLogin: Boolean) {
        editor?.putBoolean(IS_LOGIN, isLogin)
        editor?.commit()
    }

    /**
     * Fungsi set role
     */
    fun setrole(role: Int) {
        editor?.putInt("role_id", role)
        editor?.commit()
    }

    /**
     * Fungsi get login
     */
    fun isLogin(): Boolean? {
        return prefs?.getBoolean(IS_LOGIN, false)
    }

    /**
     * Fungsi get role
     */
    fun getrole(): Int? {
        return prefs?.getInt("role_id", 0)
    }

    /**
     * Fungsi set user id
     */
    fun setUserId(id: Int) {
        editor?.putInt("id", id)
        editor?.commit()
    }

    /**
     * Fungsi get user id
     */
    fun getUserId(): Int {
        return prefs.getInt("id", 0)
    }

    /**
     * Fungsi set user name
     */
    fun setUserName(name: String) {
        editor?.putString("name", name)
        editor?.commit()
    }

    /**
     * Fungsi get user name
     */
    fun getUserName(): String? {
        return prefs.getString("name", null)
    }

    /**
     * Fungsi delete access_token / clear shared preferences
     */
    fun deleteAccessToken() {
        val editor = prefs.edit()
        editor.clear()
            .apply()
    }
}