package com.example.ecoroute

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton

class MapFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_map, container, false)
        view.findViewById<MaterialButton>(R.id.btnStartNavigation).setOnClickListener {
            startActivity(Intent(requireContext(), ConfirmCollectionActivity::class.java))
        }
        return view
    }
}
