package com.rd.pixeldisplay.ui.devices

import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.rd.pixeldisplay.R
import com.rd.pixeldisplay.bluetooth.BluetoothManager
import com.rd.pixeldisplay.bluetooth.BluetoothStateActivity

class DevicesActivity : BluetoothStateActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_devices)
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
        val txtEnableBluetooth = findViewById<TextView>(R.id.btnEnableBluetooth)
        txtEnableBluetooth.setOnClickListener { } //TODO handle click action

        if (!BluetoothManager().isBlueoothAvailable()) {
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
