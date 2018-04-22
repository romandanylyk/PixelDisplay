package com.rd.pixeldisplay.bluetooth.state

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

@SuppressLint("Registered")
open class BluetoothStateActivity : AppCompatActivity(), BluetoothStateManager.Listener {

    override fun onBluetoothOff() {/*empty*/}

    override fun onBluetoothTurningOff() {/*empty*/}

    override fun onBluetoothOn() {/*empty*/}

    override fun onBluetoothTurningOn() {/*empty*/}

    override fun onBluetoothError() {/*empty*/}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BluetoothStateManager().observ(this, this)
    }
}