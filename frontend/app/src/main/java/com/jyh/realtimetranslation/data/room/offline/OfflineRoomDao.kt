package com.jyh.realtimetranslation.data.room.offline

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface OfflineRoomDao {
    @Query("SELECT * FROM offline_room")
    fun getAll(): LiveData<List<OfflineRoomItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg offlineRoom: OfflineRoomItem)

    @Delete
    fun delete(offlineRoom: OfflineRoomItem)
}