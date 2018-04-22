package com.rd.pixeldisplay.ui.availabledevices

import android.os.Bundle
import android.view.View
import com.rd.pixeldisplay.R
import com.rd.pixeldisplay.bluetooth.BluetoothManager
import com.rd.pixeldisplay.bluetooth.state.BluetoothStateActivity
import com.rd.pixeldisplay.ui.adddevice.AddDeviceActivity
import kotlinx.android.synthetic.main.ac_available_devices.*

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
        btnEnableBluetooth.setOnClickListener { BluetoothManager.enable() }
        fabNewDevice.setOnClickListener { AddDeviceActivity().start(this) }
        if (!BluetoothManager.isBluetoothAvailable()) displayEnableBluetooth()
    }

    private fun displayEnableBluetooth() {
        txtEnableBluetooth.visibility = View.VISIBLE
        btnEnableBluetooth.visibility = View.VISIBLE
    }

    private fun hideEnableBluetooth() {
        txtEnableBluetooth.visibility = View.GONE
        btnEnableBluetooth.visibility = View.GONE
    }
}
