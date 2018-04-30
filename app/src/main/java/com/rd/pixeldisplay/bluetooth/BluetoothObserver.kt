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

class BluetoothManager {

    fun observ(activity: AppCompatActivity, listener: Listener?) {
        activity.lifecycle.addObserver(BluetoothObserver(activity, listener))
    }

    interface Listener {
        fun onBluetoothOff()

        fun onBluetoothTurningOff()

        fun onBluetoothOn()

        fun onBluetoothTurningOn()

        fun onBluetoothError()
    }

    class BluetoothObserver(private val activity: AppCompatActivity, listener: Listener?) : LifecycleObserver {

        @OnLifecycleEvent(Lifecycle.Event.ON_START)
        private fun registerStateReceiver() {
            val intentFilter = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
            activity.registerReceiver(stateReceiver, intentFilter)
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
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