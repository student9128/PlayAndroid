package com.kevin.playandroid.sign

import android.app.Activity
import android.content.Intent
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.textfield.TextInputEditText
import com.kevin.playandroid.MainActivity
import com.kevin.playandroid.R
import com.kevin.playandroid.base.BaseActivity
import com.kevin.playandroid.listener.ActivityCreateListener
import com.kevin.playandroid.util.ToastUtils
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.layout_tool_bar.*

/**
 * Created by Kevin on 2019-11-21<br/>
 * Blog:http://student9128.top/
 * 公众号：竺小竹
 * Describe:<br/>
 */
class RegisterActivity : BaseActivity() {
    companion object {
        var listener: ActivityCreateListener? = null
        open fun setOnActivityCreatedListener(listener: ActivityCreateListener) {
            this.listener = listener
        }
    }

    private lateinit var mAccount: TextInputEditText
    private lateinit var mPassword: TextInputEditText
    private lateinit var mConfirmPassword: TextInputEditText
    private lateinit var mRegister: Button
    private lateinit var signModel: SignModel

    override fun getLayoutResId(): Int {
        return R.layout.activity_register
    }

    override fun initView() {
        signModel = ViewModelProviders.of(this).get(SignModel::class.java)
        tool_bar.setTitle(R.string.menu_register)
        mAccount = findViewById(R.id.text_input_et_account)
        mPassword = findViewById(R.id.text_input_et_password)
        mConfirmPassword = findViewById(R.id.text_input_et_re_password)
        mRegister = findViewById(R.id.btn_register)

        signModel.getLoadingStatus().observe(this, Observer {
            pb_progress.visibility = View.GONE
            if (it["progress"] == "success") {
                val msg = it["errorMsg"]
                if (msg!!.isEmpty()) {
                    snack(mAccount, "注册成功")
                } else {
                    snack(mAccount, msg)
                }
            } else {
                val msg = it["errorMsg"]
                snack(mAccount, msg!!)
            }
        })
        signModel.getLoginLoadingStatus().observe(this, Observer {
            if (it["progress"] == "success") {
                val msg = it["errorMsg"]
                if (msg!!.isEmpty()) {
                    snack(mAccount, "已为你自动登录成功")
                    var i = Intent(this, MainActivity::class.java)
                    setResult(Activity.RESULT_OK, i)
                    finish()
                } else {
                    snack(mAccount, msg)
                }
            } else {
                val msg = it["errorMsg"]
                snack(mAccount, msg!!)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        listener!!.onActivityCreateListener(TAG)
    }

    override fun initListener() {
        mRegister.setOnClickListener {
            val account = mAccount.text.toString().trim()
            val password = mPassword.text.toString().trim()
            val confirmPassword = mConfirmPassword.text.toString().trim()
            if (account.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                if (confirmPassword == password) {
                    pb_progress.visibility = View.VISIBLE
                    signModel.register(account, password, confirmPassword)
                } else {
                    //两次输入密码不一致
                    snack(mAccount, "两次输入密码不一致")
                }
            } else if (account.isEmpty()) {
                snack(mAccount, "请输入账号")

            } else if (password.isEmpty()) {
                snack(mAccount, "请输入密码")

            } else if (confirmPassword.isEmpty()) {
                snack(mAccount, "请输入确认密码")

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