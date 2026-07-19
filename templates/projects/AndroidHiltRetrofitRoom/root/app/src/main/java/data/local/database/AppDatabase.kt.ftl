package \${packageName}.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import \${packageName}.data.local.dao.ItemDao
import \${packageName}.data.local.entity.ItemEntity

@Database(entities = [ItemEntity::class], version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao
}