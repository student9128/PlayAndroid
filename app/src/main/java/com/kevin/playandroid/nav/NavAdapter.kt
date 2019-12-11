package com.kevin.playandroid.nav

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.flexbox.FlexboxLayout
import com.kevin.playandroid.R
import com.kevin.playandroid.util.DisplayUtils

/**
 * Created by Kevin on 2019-12-10<br/>
 * Blog:http://student9128.top/
 * 公众号：竺小竹
 * Describe:<br/>
 */
class NavAdapter(var context: Context, var data: MutableList<NavData>) :
    BaseExpandableListAdapter() {

    fun update(d: List<NavData>) {
        data.clear()
        data.addAll(d)
        notifyDataSetChanged()
    }

    override fun getGroup(groupPosition: Int): Any {
        return data[groupPosition]
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun getGroupView(
        groupPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        var groupViewHolder: GroupViewHolder
        var view = convertView
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.adapter_item_nav_title, null)
            groupViewHolder = GroupViewHolder()
            groupViewHolder.title = view.findViewById(R.id.tv_title)
            view!!.tag = groupViewHolder
        } else {
            groupViewHolder = view!!.tag as GroupViewHolder
        }
        groupViewHolder.title.text = data[groupPosition].name
        return view!!

    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return if (data[groupPosition].articles.isNotEmpty()) 1 else 0
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return data[groupPosition].articles[childPosition]
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        var childViewHolder: ChildViewHolder
        var view = convertView
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.adapter_item_nav_content, null)
            childViewHolder = ChildViewHolder()
            childViewHolder.flexBox = view.findViewById(R.id.flex_box)
            view!!.tag = childViewHolder
        } else {
            childViewHolder = view!!.tag as ChildViewHolder
        }
        val size = data[groupPosition].articles.size
        val childCount = childViewHolder.flexBox.childCount
        if (size != childCount) {
            childViewHolder.flexBox.removeAllViews()
            for (i in 0 until size - 1) {
                val textView = TextView(context)
                var lp = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                textView.layoutParams = lp
                val paddingV = DisplayUtils.dp2px(5f)
                val paddingH = DisplayUtils.dp2px(10f)
                textView.setPadding(paddingH, paddingV, paddingH, paddingV)
                textView.gravity = Gravity.CENTER
                textView.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
                textView.text = data[groupPosition].articles[i].title
                childViewHolder.flexBox.addView(textView)
                textView.setOnClickListener {
                    if (listener != null) {
                        listener!!.onChildItemClick(groupPosition, i)
                    }
                }
            }
        }
        childViewHolder.flexBox
        return view
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun getGroupCount(): Int {
        return data.size
    }

    class GroupViewHolder {
        lateinit var title: TextView
    }

    class ChildViewHolder {
        lateinit var flexBox: FlexboxLayout
    }

    private var listener: OnChildItemClickListener? = null

    interface OnChildItemClickListener {
        fun onChildItemClick(groupPosition: Int, childPosition: Int)
    }

    open fun setOnChildItemClickListener(l: OnChildItemClickListener) {
        listener = l;
    }
}