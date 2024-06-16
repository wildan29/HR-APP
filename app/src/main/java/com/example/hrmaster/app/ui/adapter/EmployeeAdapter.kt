package com.example.hrmaster.app.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hrmaster.data.models.response.DataUser
import com.example.hrmaster.databinding.ItemEmployeeBinding

class EmployeeAdapter(private val employee: List<DataUser>):
    RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder>() {
    inner class EmployeeViewHolder(private val binding: ItemEmployeeBinding)
        :RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeViewHolder {
        val binding = ItemEmployeeBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return EmployeeViewHolder(binding)
    }

    override fun getItemCount() = 10

    override fun onBindViewHolder(holder: EmployeeViewHolder, position: Int) {

    }
}