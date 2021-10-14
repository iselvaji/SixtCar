package com.sixt.sixtcar.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sixt.sixtcar.R
import com.sixt.sixtcar.model.data.Vehicle
import kotlinx.android.synthetic.main.list_item_vehicle.view.*

class VehicleAdapter (val data: List<Vehicle>, val cellClickListener: CellClickListener) :
    RecyclerView.Adapter<VehicleAdapter.VehicleViewHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehicleViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_item_vehicle, parent, false)
        return VehicleViewHolder(v)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: VehicleViewHolder, position: Int) {
        holder.bind(data[position])
        holder.itemView.setOnClickListener {
            cellClickListener.onCellClickListener(data[position])
        }
    }

    class VehicleViewHolder(val view: View): RecyclerView.ViewHolder(view){

        fun bind(vehicle: Vehicle){

            view.tvName.text = vehicle.name
            view.tvModelName.text = vehicle.modelName
            view.tvLicens.text = vehicle.licensePlate
            view.tvMake.text = vehicle.make

            Glide.with(view.context).
                load(vehicle.carImageUrl).
                placeholder(R.drawable.ic_car)
                .into(view.imageVehicle)
        }
    }

    interface CellClickListener {
        fun onCellClickListener(vehicle: Vehicle)
    }
}