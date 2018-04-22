package com.rd.pixeldisplay.ui.adddevice

import android.arch.lifecycle.ViewModel
import android.bluetooth.BluetoothDevice
import com.rd.pixeldisplay.ui.data.Device

class AddDeviceViewModel : ViewModel() {

    fun convertBluetoothDevice(blutoothDevice: BluetoothDevice): Device {
        return Device(
                blutoothDevice.name,
                blutoothDevice.address,
                blutoothDevice.bondState,
                blutoothDevice.type)
    }
}