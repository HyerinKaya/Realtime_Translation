package com.jyh.realtimetranslation.data.room.offline

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "offline_message")
data class OfflineMessageItem(
    @PrimaryKey val id: Long,
    val offlineRoomId: Long,
    val isSending: Boolean,
    val originalText:String,
    val translatedText:String
)