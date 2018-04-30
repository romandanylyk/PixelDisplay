package com.rd.pixeldisplay.ui.availabledevices

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.rd.pixeldisplay.R
import com.rd.pixeldisplay.ui.adddevice.AddDeviceActivity
import kotlinx.android.synthetic.main.ac_available_devices.*

class AvailableDevicesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_available_devices)
        initViews()
    }

    private fun initViews() {
        fabNewDevice.setOnClickListener { AddDeviceActivity().start(this) }
    }
}
