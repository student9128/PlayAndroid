package com.kevin.playandroid.listener

/**
 * Created by Kevin on 2019-11-21<br/>
 * Blog:http://student9128.top/
 * 公众号：竺小竹
 * Describe:<br/>
 * 监听某个Activity创建了<br/>
 * 这里用来判断创建后才关闭DrawerLayout,解决从主页跳转Activity视觉不好的效果
 */
interface ActivityCreateListener {
    fun onActivityCreateListener(activityName: String)
}