package com.tku.usrcare.viewmodel

import androidx.lifecycle.ViewModel
import com.tku.usrcare.repository.SessionManager

class SportsViewModel(private val sessionManager: SessionManager) : ViewModel() {
    data class VdList(val title: String , val url : String)
    val vdlist = listOf<VdList>(
        VdList("一、健康操完整版","https://www.youtube.com/watch?v=_w50TfdCmKU"),
        VdList("二、健康操台語精簡版","https://www.youtube.com/watch?v=VEAqBmJHbIw"),
        VdList("三、居家自主健康操","https://www.youtube.com/watch?v=slZgx4uGa7Y"),
    )

    fun getYtVideoId(url: String): String {
        return url.split("v=")[1]
    }

    fun getYtThumbnailUrl(url: String): String {
        return "https://img.youtube.com/vi/${getYtVideoId(url)}/0.jpg"
    }

}