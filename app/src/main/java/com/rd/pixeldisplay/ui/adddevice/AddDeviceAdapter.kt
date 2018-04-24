package com.rd.pixeldisplay.ui.adddevice

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.rd.pixeldisplay.R
import com.rd.pixeldisplay.ui.data.Device
import com.rd.pixeldisplay.ui.extentions.inflate
import kotlinx.android.synthetic.main.item_add_device.view.*

class AddDeviceAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val dataList: MutableList<Device> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = AddDeviceViewHolder(parent.inflate(R.layout.item_add_device))

    override fun getItemCount() = dataList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? AddDeviceViewHolder)?.bind(dataList[position])
    }

    fun add(device: Device) {
        if (!dataList.contains(device)) {
            dataList.add(device)
            notifyDataSetChanged()
        }
    }

    fun set(devices: List<Device>) {
        dataList.clear()
        dataList.addAll(devices)
        notifyDataSetChanged()
    }

    fun clear() {
        dataList.clear()
        notifyDataSetChanged()
    }

    class AddDeviceViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(device: Device) = with(itemView) {
            txtDevice.text = device.name
        }
    }
}