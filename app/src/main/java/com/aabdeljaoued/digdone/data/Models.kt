package com.aabdeljaoued.digdone.data

import com.aabdeljaoued.digdone.model.Memo

data class UiState(
    val memos: List<Memo> = emptyList(),
    val notificationsEnabled: Boolean = true,
    val notificationPhrase: String = "",
)
