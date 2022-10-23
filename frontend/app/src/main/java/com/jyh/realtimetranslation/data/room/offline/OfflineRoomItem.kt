package com.jyh.realtimetranslation.data.room.offline

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "offline_room")
data class OfflineRoomItem(
    @PrimaryKey val roomId: Long,
    var recentlyUpdatedDate: Long,
    var preview: String
)