package com.jyh.realtimetranslation.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jyh.realtimetranslation.data.room.offline.OfflineMessageDao
import com.jyh.realtimetranslation.data.room.offline.OfflineMessageItem
import com.jyh.realtimetranslation.data.room.offline.OfflineRoomDao
import com.jyh.realtimetranslation.data.room.offline.OfflineRoomItem

@Database(entities = [OfflineRoomItem::class, OfflineMessageItem::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun offlineRoomDao(): OfflineRoomDao
    abstract fun offlineMessageDao(): OfflineMessageDao

    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(
            context: Context
        ): AppDatabase {
            if(INSTANCE ==null){
                synchronized(this){
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "database"
                    ).build()
                    INSTANCE = instance
                }
            }
            return INSTANCE!!
        }
    }
}