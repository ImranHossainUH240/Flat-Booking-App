package com.example.flatbookingapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class ProfileFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // Find the new Landlord button and set the click listener
        val btnListProperty = view.findViewById<Button>(R.id.btnListProperty)

        btnListProperty?.setOnClickListener {
            val intent = Intent(requireContext(), ListPropertyActivity::class.java)
            startActivity(intent)
        }

        return view
    }
}