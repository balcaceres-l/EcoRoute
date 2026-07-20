package com.example.ecoroute

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class ConfirmCollectionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_collection)
        
        findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbarConfirm).setNavigationOnClickListener {
            finish()
        }
    }
}
