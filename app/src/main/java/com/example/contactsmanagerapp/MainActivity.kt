package com.example.contactsmanagerapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.contactsmanagerapp.viewModel.UserViewModel
import com.example.contactsmanagerapp.viewModel.UserViewModelFactory
import com.example.contactsmanagerapp.databinding.ActivityMainBinding
import com.example.contactsmanagerapp.room.User
import com.example.contactsmanagerapp.room.UserDatabase
import com.example.contactsmanagerapp.room.UserRepository
import com.example.contactsmanagerapp.viewUI.MyRecyclerViewAdapter

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var userViewModel: UserViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //Room database

        val dao = UserDatabase.getInstance(application).userDAO
        val repository = UserRepository(dao)
        val factory = UserViewModelFactory(repository)

        userViewModel = ViewModelProvider(this, factory).get(UserViewModel::class.java)

        binding.userViewModel = userViewModel

        binding.lifecycleOwner = this

        initRecyclerView()
    }

    private fun initRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        DisplayusersList()
    }

    private fun DisplayusersList() {
        userViewModel.users.observe(this, Observer {
            binding.recyclerView.adapter = MyRecyclerViewAdapter(
                it, { selectedItem: User -> listitemClicked(selectedItem) }
            )

        })
    }

    private fun listitemClicked(selectedItem: User) {
        Toast.makeText(this, "selected name is ${selectedItem.name}", Toast.LENGTH_LONG).show()
        userViewModel.initUpdateAndDelete(selectedItem)
    }
}