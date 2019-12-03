package com.kevin.playandroid

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Handler
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.size
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import com.google.android.material.navigation.NavigationView
import com.kevin.playandroid.base.BaseActivity
import com.kevin.playandroid.home.HomeFragment
import com.kevin.playandroid.listener.ActivityCreateListener
import com.kevin.playandroid.nav.NavFragment
import com.kevin.playandroid.project.ProjectFragment
import com.kevin.playandroid.sign.LoginActivity
import com.kevin.playandroid.sign.RegisterActivity
import com.kevin.playandroid.wxarticle.OfficialAccountFragment

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener,
    ActivityCreateListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private val states =
        arrayOf(intArrayOf(-android.R.attr.state_checked), intArrayOf(android.R.attr.state_checked))
    private lateinit var colors: IntArray
    private lateinit var colorStateList: ColorStateList
    private var mHomeFragment: HomeFragment? = null
    private var mProjectFragment: ProjectFragment? = null
    private var mNavFragment: NavFragment? = null
    private var mOfficialAccountFragment: OfficialAccountFragment? = null
    private var mTempFragment: Fragment? = null
    override fun getLayoutResId(): Int {
        return R.layout.activity_main
    }

    override fun initView() {
        initHostFragment()
        val icon = ContextCompat.getDrawable(this, R.drawable.ic_menu)
        icon!!.setTint(ContextCompat.getColor(this, R.color.white))
        supportActionBar!!.setHomeAsUpIndicator(icon)
        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)
//        val navController = findNavController(R.id.nav_host_fragment)
//        appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.nav_home, R.id.nav_project, R.id.nav_navigation,
//                R.id.nav_official_account
//            ), drawerLayout
//        )
//        setupActionBarWithNavController(navController, appBarConfiguration)
//        navView.setupWithNavController(navController)
//
        navView.setNavigationItemSelectedListener(this)

        colors = intArrayOf(Color.GRAY, ContextCompat.getColor(this, R.color.colorAccent))
        colorStateList = ColorStateList(states, colors)
        navView.itemTextColor = colorStateList
        navView.itemIconTintList = colorStateList
        setNavItemCheck(navView.menu.getItem(0))
        RegisterActivity.setOnActivityCreatedListener(this)
        LoginActivity.setOnActivityCreatedListener(this)

    }

    private fun initHostFragment() {
        mHomeFragment = HomeFragment()
        supportFragmentManager.beginTransaction().replace(R.id.fl_content, mHomeFragment!!).commit()
        mTempFragment = mHomeFragment
        toolbar!!.setTitle(R.string.menu_home)
    }

    override fun initListener() {
    }

    override fun onActivityCreateListener(activityName: String) {
        Handler().postDelayed({ closeDrawer() }, 500)
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
        var idArray =
            arrayOf(R.id.nav_home, R.id.nav_project, R.id.nav_navigation, R.id.nav_official_account)
        if (!idArray.contains(item.itemId)) return
        closeDrawer()
        item.isChecked = true
        val size = navView.menu.size
        for (i in 0 until size) {
            if (navView.menu.getItem(i).itemId != item.itemId) {
                navView.menu.getItem(i).isChecked = false
            }
        }
    }

    private fun closeDrawer() {
        printD("closeDrawer")
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers()
        }
    }

    private fun switchFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        if (mTempFragment != fragment) {
            if (fragment.isAdded) {
                transaction.hide(mTempFragment!!).show(fragment)
            } else {
                transaction.hide(mTempFragment!!).add(R.id.fl_content, fragment)
            }
            mTempFragment = fragment
            transaction.commit()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        setNavItemCheck(item)
        when (item.itemId) {
            R.id.nav_home -> {
                if (mHomeFragment == null) {
                    mHomeFragment = HomeFragment()
                }
                switchFragment(mHomeFragment!!)
                toolbar!!.setTitle(R.string.menu_home)
            }
            R.id.nav_project -> {
                if (mProjectFragment == null) {
                    mProjectFragment = ProjectFragment()
                }
                switchFragment(mProjectFragment!!)
                toolbar!!.setTitle(R.string.menu_project)
            }
            R.id.nav_navigation -> {
                if (mNavFragment == null) {
                    mNavFragment = NavFragment()
                }
                switchFragment(mNavFragment!!)
                toolbar!!.setTitle(R.string.menu_nav)
            }
            R.id.nav_official_account -> {
                if (mOfficialAccountFragment == null) {
                    mOfficialAccountFragment = OfficialAccountFragment()
                }
                switchFragment(mOfficialAccountFragment!!)
                toolbar!!.setTitle(R.string.menu_official_account)
            }
            R.id.menu_register -> {
                startActivity(Intent(this, RegisterActivity::class.java))
            }
            R.id.menu_login -> {
                startActivity(Intent(this, LoginActivity::class.java))
            }
            R.id.menu_logout -> {
                //退出登录，关闭软件两个选项
            }
        }
//        closeDrawer()
        return true
    }


}
