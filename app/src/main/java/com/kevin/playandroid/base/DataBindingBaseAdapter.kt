package com.kevin.playandroid.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Kevin on 2019-11-21<br/>
 * Blog:http://student9128.top/
 * 公众号：竺小竹
 * Describe:<br/>
 */
abstract class DataBindingBaseAdapter<DB : ViewDataBinding, T>(var list: MutableList<T>) :
    RecyclerView.Adapter<DataBindingBaseAdapter.BindViewHolder<DB>>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindViewHolder<DB> {
        var db: DB =
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                getLayoutResID(),
                parent,
                false
            )
        return BindViewHolder(db.root)
    }


    override fun onBindViewHolder(holder: BindViewHolder<DB>, position: Int) {
    }

    override fun getItemCount(): Int {
        return list.size
    }


    class BindViewHolder<DB>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var bindView: DB? = null
    }

    abstract fun getLayoutResID(): Int

}