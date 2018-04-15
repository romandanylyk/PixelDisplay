package com.rd.pixeldisplay.ui.availabledevices

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.view.View
import android.widget.TextView
import com.rd.pixeldisplay.R
import com.rd.pixeldisplay.bluetooth.BluetoothManager
import com.rd.pixeldisplay.bluetooth.BluetoothStateActivity
import com.rd.pixeldisplay.ui.addnewdevice.AddNewDeviceActivity

class AvailableDevicesActivity : BluetoothStateActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_available_devices)
        initViews()
    }

    override fun onBluetoothOff() {
        super.onBluetoothOff()
        displayEnableBluetooth()
    }

    override fun onBluetoothOn() {
        super.onBluetoothOn()
        hideEnableBluetooth()
    }

    private fun initViews() {
        findViewById<TextView>(R.id.btnEnableBluetooth).setOnClickListener { BluetoothManager().enable() }
        findViewById<FloatingActionButton>(R.id.fabNewDevice).setOnClickListener { AddNewDeviceActivity().start(this) }

        if (!BluetoothManager().isBluetoothAvailable()) {
            displayEnableBluetooth()
        }
    }

    private fun displayEnableBluetooth() {
        findViewById<TextView>(R.id.txtEnableBluetooth).visibility = View.VISIBLE
        findViewById<TextView>(R.id.btnEnableBluetooth).visibility = View.VISIBLE
    }

    private fun hideEnableBluetooth() {
        findViewById<TextView>(R.id.txtEnableBluetooth).visibility = View.GONE
        findViewById<TextView>(R.id.btnEnableBluetooth).visibility = View.GONE
    }
}
