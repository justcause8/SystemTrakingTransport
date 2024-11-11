package com.example.systemtrackingtransport

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.systemtrackingtransport.db.DaoMaster
import com.example.systemtrackingtransport.db.DaoSession

class DatabaseHelper(context: Context) {
    var daoSession: DaoSession

    init {
        // Используем DevOpenHelper для создания или открытия базы данных
        val helper = DaoMaster.DevOpenHelper(context, "system_tracking_transport-db", null)
        val db: SQLiteDatabase? = helper.writableDatabase // Открытие базы данных для записи
        val daoMaster = DaoMaster(db)
        daoSession = daoMaster.newSession()
    }

    // Метод для получения экземпляра DaoSession
    fun getDaoSessionInstance(): DaoSession {
        return daoSession
    }
}
