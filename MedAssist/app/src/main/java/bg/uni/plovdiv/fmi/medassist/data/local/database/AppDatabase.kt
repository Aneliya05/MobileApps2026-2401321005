package bg.uni.plovdiv.fmi.medassist.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import bg.uni.plovdiv.fmi.medassist.data.local.dao.ContactDao
import bg.uni.plovdiv.fmi.medassist.data.local.dao.LabTestDao
import bg.uni.plovdiv.fmi.medassist.data.local.dao.VaccineDao
import bg.uni.plovdiv.fmi.medassist.data.local.entity.ContactEntity
import bg.uni.plovdiv.fmi.medassist.data.local.entity.LabTestEntity
import bg.uni.plovdiv.fmi.medassist.data.local.entity.VaccineEntity

@Database(
    entities = [ContactEntity::class, LabTestEntity::class, VaccineEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun contactDao(): ContactDao
    abstract fun labTestDao(): LabTestDao
    abstract fun vaccineDao(): VaccineDao
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "medassist_database"
                )
                    .fallbackToDestructiveMigration(true)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}