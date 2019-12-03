package com.kevin.playandroid.sign

/**
 * Created by Kevin on 2019-12-02<br/>
 * Blog:http://student9128.top/
 * 公众号：竺小竹
 * Describe:<br/>
 */
//===========注册成功=======
//{
//    "data": {
//        "admin": false,
//        "chapterTops": [],
//        "collectIds": [],
//        "email": "",
//        "icon": "",
//        "id": 37298,
//        "nickname": "hellohello",
//        "password": "",
//        "publicName": "hellohello",
//        "token": "",
//        "type": 0,
//        "username": "hellohello"
//    },
//    "errorCode": 0,
//    "errorMsg": ""
//}
//===========注册失败==========
//{
//    "data": null,
//    "errorCode": -1,
//    "errorMsg": "用户名已经被注册！"
//}
data class Sign(
    val `data`: Data,
    val errorCode: Int,
    val errorMsg: String
)

data class Data(
    val admin: Boolean,
    val chapterTops: List<Any>,
    val collectIds: List<Int>,
    val email: String,
    val icon: String,
    val id: Int,
    val nickname: String,
    val password: String,
    val publicName: String,
    val token: String,
    val type: Int,
    val username: String
)