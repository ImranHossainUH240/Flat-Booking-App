package com.example.flatbookingapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.flatbookingapp.R
import com.example.flatbookingapp.models.Property

class PropertyAdapter(
    private val context: Context,
    private val propertyList: List<Property>,
    private val onItemClick: (Property) -> Unit // The click listener action
) : RecyclerView.Adapter<PropertyAdapter.PropertyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PropertyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_property, parent, false)
        return PropertyViewHolder(view)
    }

    override fun onBindViewHolder(holder: PropertyViewHolder, position: Int) {
        val property = propertyList[position]

        holder.tvPrice.text = "$${property.rent}/mo"
        holder.tvType.text = property.title
        holder.tvDistance.text = "${property.distanceToUniversity} mi from campus"

        // Trigger the screen change when clicked
        holder.btnQuickView.setOnClickListener {
            onItemClick(property)
        }
    }

    override fun getItemCount(): Int {
        return propertyList.size
    }

    class PropertyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvPrice: TextView = itemView.findViewById(R.id.tvPrice)
        val tvType: TextView = itemView.findViewById(R.id.tvType)
        val tvDistance: TextView = itemView.findViewById(R.id.tvDistance)
        val btnQuickView: Button = itemView.findViewById(R.id.btnQuickView)
    }
}