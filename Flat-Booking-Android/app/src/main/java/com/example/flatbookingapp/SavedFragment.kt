package com.example.flatbookingapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flatbookingapp.adapters.PropertyAdapter
import com.example.flatbookingapp.models.Property

class SavedFragment : Fragment() {

    private lateinit var recyclerSaved: RecyclerView
    private lateinit var propertyAdapter: PropertyAdapter
    private val savedList = mutableListOf<Property>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_saved, container, false)

        recyclerSaved = view.findViewById(R.id.recyclerSaved)
        recyclerSaved.layoutManager = LinearLayoutManager(requireContext())

        // Add a dummy "saved" property
        savedList.clear()
        savedList.add(Property("Premium Student Flat", "Private flat with study room.", 900, "Central", 1.5, 1.0, 15, 8.0, true, true, true))

        propertyAdapter = PropertyAdapter(requireContext(), savedList) { clickedProperty ->
            val intent = Intent(requireContext(), PropertyDetailsActivity::class.java)
            startActivity(intent)
        }

        recyclerSaved.adapter = propertyAdapter

        return view
    }
}