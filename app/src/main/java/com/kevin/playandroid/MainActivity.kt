package com.kevin.playandroid

import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Handler
import android.view.MenuItem
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.size
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import com.google.android.material.navigation.NavigationView
import com.kevin.playandroid.base.BaseActivity
import com.kevin.playandroid.common.AboutActivity
import com.kevin.playandroid.common.Constants
import com.kevin.playandroid.common.SettingActivity
import com.kevin.playandroid.common.WebActivity
import com.kevin.playandroid.home.HomeFragment
import com.kevin.playandroid.listener.ActivityCreateListener
import com.kevin.playandroid.nav.NavFragment
import com.kevin.playandroid.project.ProjectFragment
import com.kevin.playandroid.sign.LoginActivity
import com.kevin.playandroid.sign.RegisterActivity
import com.kevin.playandroid.sign.SignModel
import com.kevin.playandroid.util.SPUtils
import com.kevin.playandroid.wxarticle.OfficialAccountFragment

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener,
    ActivityCreateListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var nickname: TextView
    private val states =
        arrayOf(intArrayOf(-android.R.attr.state_checked), intArrayOf(android.R.attr.state_checked))
    private lateinit var colors: IntArray
    private lateinit var colorStateList: ColorStateList
    private var mHomeFragment: HomeFragment? = null
    private var mProjectFragment: ProjectFragment? = null
    private var mNavFragment: NavFragment? = null
    private var mOfficialAccountFragment: OfficialAccountFragment? = null
    private var mTempFragment: Fragment? = null
    private lateinit var signModel: SignModel
    override fun getLayoutResId(): Int {
        return R.layout.activity_main
    }

    override fun initView() {
        initHostFragment()
        val icon = ContextCompat.getDrawable(this, R.drawable.ic_menu)
        icon!!.setTint(ContextCompat.getColor(this, R.color.white))
        supportActionBar!!.setHomeAsUpIndicator(icon)
        signModel = ViewModelProviders.of(this).get(SignModel::class.java)
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
        val headerView = navView.getHeaderView(0)
        nickname = headerView.findViewById(R.id.tv_nickname)
        signModel.getLogoutLoadingStatus().observe(this, Observer {
            if (it["progress"] == "success") {
                val msg = it["errorMsg"]
                if (msg!!.isEmpty()) {
                    snack(drawerLayout, "退出成功")
                    refresh()
                    nickname.text = "Kevin"
                    showSign()
                } else {
                    snack(drawerLayout, msg)
                }
            }
        })
        if (getBooleanSP(Constants.KEY_LOGIN_STATE)) {
            nickname.text = getStringSP(Constants.KEY_USER_NAME)
            this.hideSign()
        } else {
            this.showSign()

        }


    }

    private fun showSign() {
        navView.menu.findItem(R.id.menu_register).isVisible = true
        navView.menu.findItem(R.id.menu_login).isVisible = true
        navView.menu.findItem(R.id.menu_logout).isVisible = false
    }

    private fun hideSign() {
        navView.menu.findItem(R.id.menu_register).isVisible = false
        navView.menu.findItem(R.id.menu_login).isVisible = false
        navView.menu.findItem(R.id.menu_logout).isVisible = true
    }

    private fun initHostFragment() {
        mHomeFragment = HomeFragment()
        supportFragmentManager.beginTransaction().replace(R.id.fl_content, mHomeFragment!!).commit()
        mTempFragment = mHomeFragment
        toolbar!!.setTitle(R.string.menu_home)
    }

    override fun initListener() {
        RegisterActivity.setOnActivityCreatedListener(this)
        LoginActivity.setOnActivityCreatedListener(this)
        WebActivity.setOnActivityCreatedListener(this)
        AboutActivity.setOnActivityCreatedListener(this)
        SettingActivity.setOnActivityCreatedListener(this)
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
        val isLogin = getBooleanSP(Constants.KEY_LOGIN_STATE)
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
                if (isLogin) {
                    snack(drawerLayout, getString(R.string.hasLogged))
                } else {
//                    startActivity()
                    startActivityForResult(Intent(this, RegisterActivity::class.java), 0)
                }
            }
            R.id.menu_login -> {
                if (isLogin) {
                    snack(drawerLayout, getString(R.string.hasLogged))
                } else {
//                    startActivity(Intent(this, LoginActivity::class.java))
                    startActivityForResult(Intent(this, LoginActivity::class.java), 1)
                }
            }
            R.id.menu_logout -> {
                //退出登录，关闭软件两个选项
                if (isLogin) {
                    signModel.logout()
                } else {
                    snack(drawerLayout, getString(R.string.hasLogedOut))
                }
                closeDrawer()
            }
            R.id.menu_code -> {
                val intent = Intent(this, WebActivity::class.java)
                intent.putExtra(Constants.WEB_URL, getString(R.string.repoUrl))
                startActivity(intent)
            }
            R.id.menu_setting -> {
                startNewActivity(SettingActivity::class.java)
            }
            R.id.menu_about -> {
                startNewActivity(AboutActivity::class.java)
            }
            R.id.menu_update->{
                closeDrawer()
            }
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                0 -> {
                    refresh()
                }
                1 -> {
                    refresh()
                }

            }
            val username = SPUtils.getString(Constants.KEY_USER_NAME)
            if (username.isNotEmpty()) {
                nickname.text = username
            }
            if (getBooleanSP(Constants.KEY_LOGIN_STATE)) {
                this.hideSign()
            } else {
                this.showSign()

            }
        }
    }

    private fun refresh() {
        mHomeFragment?.refresh()
        mProjectFragment?.refresh()
    }


}
