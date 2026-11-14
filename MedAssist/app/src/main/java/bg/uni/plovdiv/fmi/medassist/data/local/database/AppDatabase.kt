package bg.uni.plovdiv.fmi.medassist.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import bg.uni.plovdiv.fmi.medassist.data.local.dao.ContactDao
import bg.uni.plovdiv.fmi.medassist.data.local.entity.ContactEntity

@Database(entities = [ContactEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun contactDao(): ContactDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "medassist_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}