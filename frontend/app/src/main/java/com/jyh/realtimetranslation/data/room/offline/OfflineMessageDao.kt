package com.jyh.realtimetranslation.data.room.offline

import androidx.room.*

@Dao
interface OfflineMessageDao {
    @Query("SELECT * FROM offline_message")
    fun getAll(): List<OfflineMessageItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg offlineMessage: OfflineMessageItem)

    @Delete
    fun delete(offlineMessage: OfflineMessageItem)
}