package com.rd.pixeldisplay.ui.adddevice

import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.rd.pixeldisplay.R
import com.rd.pixeldisplay.bluetooth.BluetoothManager
import com.rd.pixeldisplay.bluetooth.discovery.BluetoothDiscoveryManager
import com.rd.pixeldisplay.bluetooth.state.BluetoothStateActivity
import kotlinx.android.synthetic.main.ac_add_device.*

class AddDeviceActivity : BluetoothStateActivity(), BluetoothDiscoveryManager.DiscoveryListener {

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
        initViews()

        BluetoothDiscoveryManager.observ(this, this)
        BluetoothDiscoveryManager.startDiscovery()
    }

    override fun onDestroy() {
        BluetoothDiscoveryManager.cancelDiscovery()
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_refresh, menu)
        menuRefresh = menu.findItem(R.id.actionRefresh)
        menuRefresh?.isEnabled = !BluetoothManager.isDiscovering()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home -> finish()
            R.id.actionRefresh ->  BluetoothDiscoveryManager.startDiscovery()
        }
        return true
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

    private fun initViews() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
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
        swipeRefresh.setOnRefreshListener({ BluetoothDiscoveryManager.startDiscovery() })

        adapter = AddDeviceAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
    }
}