package com.rd.pixeldisplay.ui.addnewdevice

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.rd.pixeldisplay.R
import com.rd.pixeldisplay.bluetooth.BluetoothStateActivity


class AddNewDeviceActivity : BluetoothStateActivity() {

    private lateinit var menuRefresh: MenuItem

    fun start(activity: Activity) {
        val intent = Intent(activity, AddNewDeviceActivity::class.java)
        activity.startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_add_new_device)
        initViews()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_refresh, menu)
        menuRefresh = menu.findItem(R.id.actionRefresh)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home -> onBackClicked()
            R.id.actionRefresh -> onRefreshClicked()
        }
        return true
    }

    private fun initViews() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun onBackClicked() {
        finish()
    }

    private fun onRefreshClicked() {

    }
}