package com.kevin.playandroid.sign

import android.view.MenuItem
import android.view.View
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.textfield.TextInputEditText
import com.kevin.playandroid.R
import com.kevin.playandroid.base.BaseActivity
import com.kevin.playandroid.listener.ActivityCreateListener
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.layout_tool_bar.*

/**
 * Created by Kevin on 2019-12-02<br/>
 * Blog:http://student9128.top/
 * 公众号：竺小竹
 * Describe:<br/>
 */
class LoginActivity : BaseActivity() {
    companion object {
        var listener: ActivityCreateListener? = null
        open fun setOnActivityCreatedListener(listener: ActivityCreateListener) {
            this.listener = listener
        }
    }

    private lateinit var mAccount: TextInputEditText
    private lateinit var mPassword: TextInputEditText
    private lateinit var mLogin: Button
    private lateinit var signModel: SignModel
    override fun getLayoutResId(): Int {
        return R.layout.activity_login
    }

    override fun initView() {
        signModel = ViewModelProviders.of(this).get(SignModel::class.java)
        tool_bar.setTitle(R.string.menu_login)
        mAccount = findViewById(R.id.text_input_et_account)
        mPassword = findViewById(R.id.text_input_et_password)
        mLogin = findViewById(R.id.btn_login)

        signModel.getLoadingStatus().observe(this, Observer {
            pb_progress.visibility = View.GONE
            if (it["progress"] == "success") {
                val msg = it["errorMsg"]
                if (msg!!.isEmpty()) {
                    snack(mAccount, "登录成功")
                    finish()
                } else {
                    snack(mAccount, msg)
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        listener!!.onActivityCreateListener(TAG)

    }

    override fun initListener() {
        mLogin.setOnClickListener {
            val account = mAccount.text.toString().trim()
            val password = mPassword.text.toString().trim()
            if (account.isNotEmpty() && password.isNotEmpty()) {
                signModel.login(account, password)
            } else if (account.isEmpty()) {
                snack(mAccount, "请输入账号")
            } else if (password.isEmpty()) {
                snack(mAccount, "请输入密码")
            }

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return true
    }
}