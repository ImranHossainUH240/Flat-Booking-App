package com.example.flatbookingapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flatbookingapp.adapters.PropertyAdapter
import com.example.flatbookingapp.models.Property
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {

    private lateinit var recyclerProperties: RecyclerView
    private lateinit var propertyAdapter: PropertyAdapter

    private val masterPropertyList = mutableListOf<Property>()
    private val displayList = mutableListOf<Property>()

    private val filterLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val filteredList = result.data?.getSerializableExtra("filteredList") as? ArrayList<Property>

            if (filteredList != null) {
                displayList.clear()
                displayList.addAll(filteredList)
                propertyAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        recyclerProperties = view.findViewById(R.id.recyclerProperties)
        recyclerProperties.layoutManager = LinearLayoutManager(requireContext())

        propertyAdapter = PropertyAdapter(requireContext(), displayList) { clickedProperty ->
            val intent = Intent(requireContext(), PropertyDetailsActivity::class.java)
            startActivity(intent)
        }
        recyclerProperties.adapter = propertyAdapter

        // Setup Filters
        view.findViewById<Button>(R.id.btnFilter).setOnClickListener {
            val intent = Intent(requireContext(), FilterActivity::class.java)
            intent.putExtra("propertyList", ArrayList(masterPropertyList))
            filterLauncher.launch(intent)
        }

        // Fetch Live Data!
        fetchPropertiesFromApi()

        return view
    }

    private fun fetchPropertiesFromApi() {
        // Use Coroutines to make the network request on a background thread
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                // This calls the Ktor Server!
                val liveProperties = RetrofitClient.apiService.getProperties()

                // Switch back to the Main thread to update the UI
                withContext(Dispatchers.Main) {
                    masterPropertyList.clear()
                    masterPropertyList.addAll(liveProperties)

                    displayList.clear()
                    displayList.addAll(liveProperties)

                    propertyAdapter.notifyDataSetChanged()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Failed to load data: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}