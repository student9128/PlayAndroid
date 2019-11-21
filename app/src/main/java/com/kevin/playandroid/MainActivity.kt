package com.kevin.playandroid

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.MenuItem
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.size
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import com.google.android.material.navigation.NavigationView
import com.kevin.playandroid.base.BaseActivity
import com.kevin.playandroid.home.HomeFragment

class MainActivity : BaseActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private val states =
        arrayOf(intArrayOf(-android.R.attr.state_checked), intArrayOf(android.R.attr.state_checked))
    private lateinit var colors: IntArray
    private lateinit var colorStateList: ColorStateList
    private var mHomeFragment: HomeFragment? = null
    override fun initView() {
        setContentView(R.layout.activity_main)
        initToolbar()
        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_project, R.id.nav_navigation,
                R.id.nav_official_account
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

        navView.setupWithNavController(navController)
//
//        navView.setNavigationItemSelectedListener(this)
//
//        colors = intArrayOf(Color.GRAY, ContextCompat.getColor(this, R.color.colorAccent))
//        colorStateList = ColorStateList(states, colors)
//        navView.itemTextColor = colorStateList
//        navView.itemIconTintList = colorStateList
//        setNavItemCheck(navView.menu.getItem(0))
    }

    override fun initListener() {
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> drawerLayout.openDrawer(GravityCompat.START, true)
        }
        return true
    }

    /**
     * 设置NavigationView Item的状态
     */
    private fun setNavItemCheck(item: MenuItem) {
        item.isChecked = true
        closeDrawer()
        val size = navView.menu.size
        for (i in 0 until size) {
            if (navView.menu.getItem(i).itemId != item.itemId) {
                navView.menu.getItem(i).isChecked = false
            }
        }
    }

    private fun closeDrawer() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers()
        }
    }

//    override fun onNavigationItemSelected(item: MenuItem): Boolean {
//        setNavItemCheck(item)
//        val transaction = supportFragmentManager.beginTransaction()
//        when (item.itemId) {
//            R.id.nav_home -> {
//                toast("Home")
//                if (mHomeFragment == null) {
//                    mHomeFragment=HomeFragment()
//                }
//            }
//            R.id.nav_project -> {
//            }
//            R.id.nav_navigation -> {
//            }
//            R.id.nav_official_account -> {
//            }
//            R.id.menu_register -> {
//            }
//            R.id.menu_login -> {
//            }
//            R.id.menu_logout -> {
//                //退出登录，关闭软件两个选项
//            }
//        }
//        return true
//    }


}
