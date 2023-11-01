package com.wasim.csdl.patient_chatapp.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wasim.csdl.patient_chatapp.R
import com.wasim.csdl.patient_chatapp.views.adapters.UserAdapter


abstract class BaseAdapter<VH : BaseViewHolder, D> : RecyclerView.Adapter<VH>() {
    abstract val layoutId: Int
    abstract fun setData(holder: VH, model: D, position: Int)

    private var list = ArrayList<D>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return when (layoutId) {
            R.layout.user_search_item_layout ->
                UserAdapter.UserAdapterViewHolder(
                    LayoutInflater.from(
                        parent.context
                    ).inflate(layoutId, parent, false)
                ) as VH
            else -> UserAdapter.UserAdapterViewHolder(
                LayoutInflater.from(
                    parent.context
                ).inflate(layoutId, parent, false)
            ) as VH


        }
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        if (list.size > 0)
            setData(holder, list[position], position)
    }

    override fun getItemCount() = list.size

    fun updateData(newList: ArrayList<D>) {
        list = newList
        notifyDataSetChanged()
    }

    fun clearData() {
        list.clear()
        notifyDataSetChanged()
    }

}