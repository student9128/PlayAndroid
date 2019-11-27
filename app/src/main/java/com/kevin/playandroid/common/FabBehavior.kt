package com.kevin.playandroid.common

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.kevin.playandroid.util.LogUtils

class FabBehavior(context: Context, attrs: AttributeSet) :
    FloatingActionButton.Behavior(context, attrs) {
    private var totalDyConsumed = 0
    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: FloatingActionButton, directTargetChild: View, target: View,
        axes: Int, type: Int
    ): Boolean {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL || super.onStartNestedScroll(
            coordinatorLayout,
            child, directTargetChild, target, axes, type
        )
    }

    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: FloatingActionButton, target: View, dxConsumed: Int, dyConsumed: Int,
        dxUnconsumed: Int, dyUnconsumed: Int, type: Int
    ) {
        super.onNestedScroll(
            coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed,
            dyUnconsumed, type
        )
        totalDyConsumed += dyConsumed
        if (totalDyConsumed > 5000 && child.visibility != View.VISIBLE) {
            child.show()
        } else if (dyConsumed < 0 && child.visibility == View.VISIBLE && totalDyConsumed < 1000) {
            child.hide(object : FloatingActionButton.OnVisibilityChangedListener() {
                @SuppressLint("RestrictedApi")
                override fun onHidden(fab: FloatingActionButton) {
                    super.onHidden(fab)
                    fab.visibility = View.INVISIBLE
                }
            })
        }
    }
}