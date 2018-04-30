package com.rd.pixeldisplay.bluetooth

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

@SuppressLint("Registered")
open class BluetoothActivity : AppCompatActivity(), BluetoothManager.Listener {

    override fun onBluetoothOff() {/*empty*/}

    override fun onBluetoothTurningOff() {/*empty*/}

    override fun onBluetoothOn() {/*empty*/}

    override fun onBluetoothTurningOn() {/*empty*/}

    override fun onBluetoothError() {/*empty*/}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BluetoothManager().observ(this, this)
    }
}