package com.tku.usrcare.view.ui.notification

import android.R
import android.content.Intent
import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import com.tku.usrcare.model.BroadcastData
import com.tku.usrcare.repository.TimeTools
import com.tku.usrcare.view.SettingActivity
import com.tku.usrcare.view.SportsActivity
import com.tku.usrcare.view.component.findActivity
import com.tku.usrcare.viewmodel.NotificationViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationList(
    notificationViewModel: NotificationViewModel,
    sheetState: BottomSheetScaffoldState
) {
    val sessionManager = notificationViewModel.mSessionManager
    val notificationList by notificationViewModel.notificationList.observeAsState(sessionManager.getBroadcastDataList())
    var topIndex = notificationList.size - 1
    if (topIndex < 0) {
        topIndex = 0
    }
    // 使用 rememberLazyListState 記住滾動狀態
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = topIndex)
    // 使用 rememberCoroutineScope 創建一個協程範疇
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = notificationList.size) {
        // 當項目列表的大小改變時，這個效果會被重新啟動
        // 滾動到列表的最底部
        coroutineScope.launch {
            // 嘗試滾動到最後一項
            listState.animateScrollToItem(index = topIndex)
        }
    }
    LazyColumn(
        state = listState,
        modifier = androidx.compose.ui.Modifier
            .fillMaxWidth()
            .padding(start = 30.dp, end = 30.dp)
            .animateContentSize(),
        reverseLayout = true
    ) {
        item {
            Spacer(modifier = androidx.compose.ui.Modifier.height(20.dp))
        }
        items(notificationList.size) { index ->
            NotificationItem(broadcastData = notificationList[index], notificationViewModel = notificationViewModel , sheetState = sheetState , coroutineScope = coroutineScope)
            Spacer(modifier = androidx.compose.ui.Modifier.height(20.dp))
        }
        item {
            Spacer(modifier = androidx.compose.ui.Modifier.height(80.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationItem(
    broadcastData: BroadcastData,
    notificationViewModel: NotificationViewModel,
    sheetState: BottomSheetScaffoldState,
    coroutineScope: CoroutineScope
) {
    val context = LocalContext.current
    Box(
        modifier = androidx.compose.ui.Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(shape = RoundedCornerShape(18.dp))
            .background(colorResource(id = R.color.white))
            .clickable {
                when (broadcastData.action) {
                    "video" -> {
                        notificationViewModel.setSelectedMessage(broadcastData)
                        context.findActivity()?.startActivity(
                            Intent(context, SportsActivity::class.java).apply {
                                putExtra("action", broadcastData.action)//移動到其他頁面
                                putExtra("url",broadcastData.url)//傳送影片url
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP//為了觸發onNewIntent必要條件
                            }
                        )
                    }
                    /*"setting" -> {
                        context.findActivity()?.startActivity(
                            Intent(
                                context, SettingActivity::class.java
                            )
                        )
                    }*/
                    else -> {
                        Log.d("fcm", "nothing to do")
                    }
                }
                /*coroutineScope.launch {
                    sheetState.bottomSheetState.expand()
                }*/
            }
    ) {
        Row(
            modifier = androidx.compose.ui.Modifier
                .fillMaxWidth().padding(15.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(id = com.tku.usrcare.R.drawable.ic_notification),
                contentDescription = "notification",
                tint = Color.Black,
                modifier = androidx.compose.ui.Modifier
                    .size(60.dp)
                    .padding(end = 10.dp)
            )
            Column(
                modifier = androidx.compose.ui.Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = broadcastData.title,
                    color = colorResource(id = R.color.black),
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                    fontSize = 20.sp,
                    maxLines = 2,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )
                if (broadcastData.message != " ") {
                    Text(
                        text = broadcastData.message,
                        color = colorResource(id = R.color.black),
                        fontSize = 13.sp,
                        maxLines = 5,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                    )
                }
                Text(
                    text = TimeTools.howLongAgo(broadcastData.time.toLong()),
                    color = colorResource(id = R.color.black),
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
@Preview
fun NotificationListPreview() {

}