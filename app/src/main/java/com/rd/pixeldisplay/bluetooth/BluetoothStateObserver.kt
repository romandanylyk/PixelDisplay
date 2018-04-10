package com.rd.pixeldisplay.bluetooth

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.support.v7.app.AppCompatActivity

class BluetoothStateManager {

    fun observ(activity: AppCompatActivity, listener: BluetoothStateListener?) {
        activity.lifecycle.addObserver(BluetoothStateObserver(activity, listener))
    }

    interface BluetoothStateListener {
        fun onBluetoothOff()

        fun onBluetoothTurningOff()

        fun onBluetoothOn()

        fun onBluetoothTurningOn()

        fun onBluetoothError()
    }

    class BluetoothStateObserver(private val activity: AppCompatActivity, listener: BluetoothStateListener?) : LifecycleObserver {

        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        private fun registerStateReceiver() {
            val intentFilter = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
            activity.registerReceiver(stateReceiver, intentFilter)
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        fun unregisterDiscoverReceiver() {
            activity.unregisterReceiver(stateReceiver)
        }

        private val stateReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val isActionChanged = intent.action == BluetoothAdapter.ACTION_STATE_CHANGED

                if (isActionChanged) {
                    val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)
                    when (state) {
                        BluetoothAdapter.STATE_OFF -> listener?.onBluetoothOff()

                        BluetoothAdapter.STATE_TURNING_OFF -> listener?.onBluetoothTurningOff()

                        BluetoothAdapter.STATE_ON -> listener?.onBluetoothOn()

                        BluetoothAdapter.STATE_TURNING_ON -> listener?.onBluetoothTurningOn()

                        BluetoothAdapter.ERROR -> listener?.onBluetoothError()
                    }
                }
            }
        }
    }
}