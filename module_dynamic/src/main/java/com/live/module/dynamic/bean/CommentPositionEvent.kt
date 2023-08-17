package com.live.module.dynamic.bean

/**
 * author: Tany
 * date: 2022/6/11
 * description:
 */
data class CommentPositionEvent(
    var commentBean: DynamicTantaCommentBean?=null,
    var isLongClick:Boolean=false,
    var position:Int=0
)