package com.rd.pixeldisplay.ui.adddevice

import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.rd.pixeldisplay.R
import com.rd.pixeldisplay.bluetooth.BluetoothActivity
import com.rd.pixeldisplay.bluetooth.BluetoothController
import com.rd.pixeldisplay.bluetooth.BluetoothDiscoveryManager
import com.rd.pixeldisplay.ui.data.Device
import kotlinx.android.synthetic.main.ac_add_device.*

class AddDeviceActivity : BluetoothActivity(), BluetoothDiscoveryManager.DiscoveryListener, AddDeviceAdapter.Listener {

    private lateinit var viewModel: AddDeviceViewModel
    private lateinit var adapter: AddDeviceAdapter
    private var menuRefresh: MenuItem? = null

    fun start(activity: Activity) {
        val intent = Intent(activity, AddDeviceActivity::class.java)
        activity.startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_add_device)

        viewModel = ViewModelProviders.of(this).get(AddDeviceViewModel::class.java)
        BluetoothDiscoveryManager.observ(this, this)

        initViews()
        startDiscovery()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_refresh, menu)
        menuRefresh = menu.findItem(R.id.actionRefresh)
        menuRefresh?.isEnabled = !BluetoothController.isDiscovering()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            R.id.actionRefresh -> BluetoothDiscoveryManager.startDiscovery()
        }
        return true
    }

    override fun onBluetoothOn() {
        super.onBluetoothOn()
        startDiscovery()
    }

    override fun onBluetoothOff() {
        super.onBluetoothOff()
        displayEnableBluetooth()
    }

    override fun onBluetoothDiscoveryStarted() {
        adapter.clear()
        menuRefresh?.isEnabled = false
        swipeRefresh.isRefreshing = true
    }

    override fun onBluetoothDeviceFound(device: BluetoothDevice) {
        adapter.add(viewModel.convertBluetoothDevice(device))
    }

    override fun onBluetoothDiscoveryEnded() {
        menuRefresh?.isEnabled = true
        swipeRefresh.isRefreshing = false
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == BluetoothController.ACTION_REQUEST_ENABLE_LOCATION) {
            if (grantResults.contains(PackageManager.PERMISSION_GRANTED)) startDiscovery()
        }
    }

    override fun onDeviceClicked(device: Device) {
        displayConnectionDialog(device)
    }

    private fun initViews() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        btnEnableBluetooth.setOnClickListener { startDiscovery() }

        swipeRefresh.setColorSchemeColors(
                resources.getColor(R.color.blue_100),
                resources.getColor(R.color.blue_200),
                resources.getColor(R.color.blue_300),
                resources.getColor(R.color.blue_400),
                resources.getColor(R.color.blue_500),
                resources.getColor(R.color.blue_400),
                resources.getColor(R.color.blue_300),
                resources.getColor(R.color.blue_200),
                resources.getColor(R.color.blue_100))
        swipeRefresh.setOnRefreshListener({ startDiscovery() })

        adapter = AddDeviceAdapter()
        adapter.setListener(this)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
    }

    private fun startDiscovery() {
        if (isBluetoothEnabled()) {
            hideEnableBluetooth()
            BluetoothDiscoveryManager.startDiscovery()
        } else displayEnableBluetooth()
    }

    private fun isBluetoothEnabled(): Boolean {
        return if (!BluetoothController.isLocationAvailable(this)) {
            BluetoothController.requestLocationPermission(this)
            false

        } else if (!BluetoothController.isBluetoothAvailable()) {
            BluetoothController.requestBluetoothEnable(this)
            false

        } else true
    }

    private fun displayEnableBluetooth() {
        txtEnableBluetooth.visibility = View.VISIBLE
        btnEnableBluetooth.visibility = View.VISIBLE
        menuRefresh?.isEnabled = false
        swipeRefresh.isEnabled = false
    }

    private fun hideEnableBluetooth() {
        txtEnableBluetooth.visibility = View.GONE
        btnEnableBluetooth.visibility = View.GONE
        menuRefresh?.isEnabled = true
        swipeRefresh.isEnabled = true
    }

    private fun displayConnectionDialog(device: Device?) {
        if (device == null || TextUtils.isEmpty(device.name) || TextUtils.isEmpty(device.address)) {
            return
        }

        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.confirm_your_action)
        builder.setMessage(String.format(getString(R.string.connection_confirmation), device.name))
        builder.setNegativeButton(R.string.Cancel, null)
        builder.setPositiveButton(R.string.Continue, { dialogInterface, i -> viewModel.pairDevice(device) })

        builder.show()
    }
}