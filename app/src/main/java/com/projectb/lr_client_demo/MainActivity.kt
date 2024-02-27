package com.projectb.lr_client_demo

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.projectb.lr_client_demo.ui.theme.LRClientDemoTheme
import com.projectb.lr_client_demo.databinding.ActivityMainBinding

class MainActivity : ComponentActivity() {
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = Bundle()

        binding.adminDemoButton.setOnClickListener{
            Toast.makeText(this, "Select or create admin user", Toast.LENGTH_SHORT).show()
            // Go back to admin select activity
            bundle.putInt("user_type", 1)
            val intent = Intent(this, SelectUserActivity::class.java)
            intent.putExtras(bundle)
            startActivity(intent)
        }
        binding.foremanDemoButton.setOnClickListener{
            Toast.makeText(this, "Select or create foreman user", Toast.LENGTH_SHORT).show()
            // Go back to admin select activity
            bundle.putInt("user_type", 2)
            val intent = Intent(this, SelectUserActivity::class.java)
            intent.putExtras(bundle)
            startActivity(intent)
        }
        binding.checkerDemoButton.setOnClickListener{
            Toast.makeText(this, "Select or create checker user", Toast.LENGTH_SHORT).show()
            // Go back to admin select activity
            bundle.putInt("user_type", 3)
            val intent = Intent(this, SelectUserActivity::class.java)
            intent.putExtras(bundle)
            startActivity(intent)
        }
    }
}
