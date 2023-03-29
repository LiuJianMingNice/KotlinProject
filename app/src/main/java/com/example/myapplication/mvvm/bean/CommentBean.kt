package com.amazing.eye.bean

import com.amazing.eye.utils.getCustomTime
import com.example.myapplication.mvvm.bean.BaseBean


data class CommentBean(
    var replyList: List<ReplyListBean>?,
    var count: Int,
    var total: Int,
    var nextPageUrl: String?
) : BaseBean() {

    data class ReplyListBean(
        var sequence: Int,
        var message: String?,
        var createTime: Long,
        var user: UserBean?,
        var likeCount: String?
        ) {

        fun getTime(): String {
            return String().getCustomTime(createTime)
        }

        fun isShowLikeCount(): Boolean {
            return !likeCount.equals("0")
        }

        data class UserBean(var uid: Long, var nickname: String?, var avatar: String?)

    }
}