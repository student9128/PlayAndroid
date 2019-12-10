package com.kevin.playandroid.common

import android.net.Uri
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.core.text.HtmlCompat
import com.kevin.playandroid.R
import com.kevin.playandroid.base.BaseActivity
import com.kevin.playandroid.listener.ActivityCreateListener
import com.kevin.playandroid.sign.RegisterActivity
import com.kevin.playandroid.util.LogUtils
import org.mozilla.geckoview.*
import org.mozilla.geckoview.GeckoSessionSettings.USER_AGENT_MODE_MOBILE

/**
 * Created by Kevin on 2019/11/28<br/>
 * Blog:http://student9128.top/
 * 公众号：竺小竹
 * Describe:<br/>
 */
class WebActivity : BaseActivity() {
    private lateinit var geckoView: GeckoView
    private lateinit var geckoSession: GeckoSession
    private lateinit var urlEditText: EditText
    private lateinit var progressView: ProgressBar
    private lateinit var ivBack: ImageView
    private lateinit var trackersCount: TextView
    private var trackersBlockedList: List<ContentBlocking.BlockEvent> =
        mutableListOf()
    private var isCanGoBack: Boolean = false
    private var isCanGoForward: Boolean = false

    companion object {
        var listener: ActivityCreateListener? = null
        open fun setOnActivityCreatedListener(listener: ActivityCreateListener) {
            this.listener = listener
        }
    }

    override fun onResume() {
        super.onResume()
       listener!!.onActivityCreateListener(TAG)
    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_web
    }

    override fun initView() {
        val settings = GeckoSessionSettings.Builder()
//            .usePrivateMode(true)
            .useTrackingProtection(true)
            .userAgentMode(USER_AGENT_MODE_MOBILE)
            .userAgentOverride("")
            .suspendMediaWhenInactive(true)
            .allowJavascript(true)
            .build()
        geckoSession = GeckoSession(settings)
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        ivBack = findViewById(R.id.iv_back)
        setupGeckoView()
        setupUrlEditText()
    }

    override fun initListener() {
        ivBack.setOnClickListener {
            if (isCanGoBack) {
                geckoSession.goBack()
            } else {
                onBackPressed()
            }
        }
    }

    private fun setupGeckoView() {

        geckoView = findViewById(R.id.gecko_view)
        progressView = findViewById(R.id.page_progress)

        geckoSession.progressDelegate = createProgressDelegate()
        geckoSession.navigationDelegate = createNavigationDelegate()
        // java.lang.IllegalStateException: Failed to initialize GeckoRuntime
        //不要用Create GeckoRuntime.create(this)
        val runtime = GeckoRuntime.getDefault(this)
        geckoSession.open(runtime)
        geckoView.setSession(geckoSession)
        val url = intent.getStringExtra(Constants.WEB_URL)
        geckoSession.loadUri(url)

        geckoSession.settings.useTrackingProtection = true
        geckoSession.contentBlockingDelegate = createBlockingDelegate()
        setupTrackersCounter()
    }

    private fun setupUrlEditText() {
        urlEditText = findViewById(R.id.location_view)
        urlEditText.setOnEditorActionListener(object :
            View.OnFocusChangeListener, TextView.OnEditorActionListener {

            override fun onFocusChange(view: View?, hasFocus: Boolean) = Unit

            override fun onEditorAction(
                textView: TextView,
                actionId: Int,
                event: KeyEvent?
            ): Boolean {
                clearTrackersCount()
                onCommit(textView.text.toString())
//                textView.hideKeyboard()
                return true
            }
        })
    }

    fun onCommit(text: String) {
        if ((text.contains(".") ||
                    text.contains(":")) &&
            !text.contains(" ")
        ) {
            geckoSession.loadUri(text)
        } else {
            geckoSession.loadUri("https://www.baidu.com/s?wd=$text")
        }

        geckoView.requestFocus()
    }

    private fun createProgressDelegate(): GeckoSession.ProgressDelegate {
        return object : GeckoSession.ProgressDelegate {

            override fun onPageStop(session: GeckoSession, success: Boolean) = Unit

            override fun onSecurityChange(
                session: GeckoSession,
                securityInfo: GeckoSession.ProgressDelegate.SecurityInformation
            ) = Unit

            override fun onPageStart(session: GeckoSession, url: String) {
                urlEditText.setText(url)
            }

            override fun onProgressChange(session: GeckoSession, progress: Int) {
                progressView.progress = progress

                if (progress in 1..99) {
                    progressView.visibility = View.VISIBLE
                } else {
                    progressView.visibility = View.GONE
                }
            }
        }
    }


    private fun createBlockingDelegate(): ContentBlocking.Delegate {
        return object : ContentBlocking.Delegate {
            override fun onContentBlocked(
                session: GeckoSession,
                event: ContentBlocking.BlockEvent
            ) {
                trackersBlockedList = trackersBlockedList + event
                trackersCount.text = "${trackersBlockedList.size}"
            }
        }
    }

    private fun ContentBlocking.BlockEvent.categoryToString(): String {
        val stringResource = when (categories) {
            ContentBlocking.NONE -> R.string.none
            ContentBlocking.AT_ANALYTIC -> R.string.analytic
            ContentBlocking.AT_AD -> R.string.ad
            ContentBlocking.AT_TEST -> R.string.test
            ContentBlocking.SB_MALWARE -> R.string.malware
            ContentBlocking.SB_UNWANTED -> R.string.unwanted
            ContentBlocking.AT_SOCIAL -> R.string.social
            ContentBlocking.AT_CONTENT -> R.string.content
            ContentBlocking.SB_HARMFUL -> R.string.harmful
            ContentBlocking.SB_PHISHING -> R.string.phishing
            else -> R.string.none
        }
        return getString(stringResource)
    }

    private fun getFriendlyTrackersUrls(): List<Spanned> {
        return trackersBlockedList.map { blockEvent ->
            val host = Uri.parse(blockEvent.uri).host
            val category = blockEvent.categoryToString()
            if (Build.VERSION.SDK_INT >= 24) Html.fromHtml(
                "<b><font color='#D55C7C'>[$category]</font></b> <br/> $host",
                HtmlCompat.FROM_HTML_MODE_COMPACT
            ) else Html.fromHtml("<b><font color='#D55C7C'>[$category]</font></b> <br/> $host")
        }
    }

    private fun setupTrackersCounter() {
        // 1
        trackersCount = findViewById(R.id.trackers_count)
        trackersCount.text = "0"
        // 2
        trackersCount.setOnClickListener {
            if (trackersBlockedList.isNotEmpty()) {
                val friendlyURLs = getFriendlyTrackersUrls()
//                showDialog(friendlyURLs)
                friendlyURLs.forEach { i->printD("$i") }
            }
        }
    }

    private fun clearTrackersCount() {
        trackersBlockedList = emptyList()
        trackersCount.text = "0"
    }

    private fun createNavigationDelegate(): GeckoSession.NavigationDelegate {
        return object : GeckoSession.NavigationDelegate {
            override fun onCanGoBack(session: GeckoSession, canGoBack: Boolean) {
                isCanGoBack = canGoBack
            }

            override fun onCanGoForward(session: GeckoSession, canGoForward: Boolean) {
                isCanGoForward = canGoForward
            }

            override fun onLoadError(
                session: GeckoSession,
                uri: String?,
                error: WebRequestError
            ): GeckoResult<String>? {
                return super.onLoadError(session, uri, error)
            }
        }
    }


}