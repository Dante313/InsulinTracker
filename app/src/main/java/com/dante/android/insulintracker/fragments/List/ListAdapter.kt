package com.dante.android.insulintracker.fragments.List

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.dante.android.insulintracker.R
import com.dante.android.insulintracker.databinding.CustomRecyclerRowBinding
import com.dante.android.insulintracker.model.Insulin
import com.dante.android.insulintracker.viewmodel.InsulinViewModel
import kotlinx.android.synthetic.main.custom_recycler_row.view.*

class ListAdapter : RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

    private var insulinList = emptyList<Insulin>()
    private lateinit var mInsulinViewModel: InsulinViewModel

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.custom_recycler_row, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentItem = insulinList[position]
        holder.itemView.tv_name.text = currentItem.name
        holder.itemView.tv_insulinleft.text = currentItem.fullness.toString()

        holder.itemView.rowLayout.setOnClickListener {
            val action =
                ListFragmentDirections.actionListFragmentToPickedInsulinFragment(currentItem)
            holder.itemView.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return insulinList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(insulin: List<Insulin>) {
        this.insulinList = insulin
        notifyDataSetChanged()
    }

}