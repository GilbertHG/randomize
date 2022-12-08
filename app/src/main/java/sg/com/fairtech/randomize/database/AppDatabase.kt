package sg.com.fairtech.randomize.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import sg.com.fairtech.randomize.model.Randomize

@Database(entities = [Randomize::class], exportSchema = false, version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun RandomizeDao(): RandomizeDao

    companion object {

        private const val DB_NAME = "RANDOMIZE_DB"
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase? {
            if (instance == null) {
                synchronized(AppDatabase::class) {
                    instance = Room
                        .databaseBuilder(
                            context,
                            AppDatabase::class.java,
                            DB_NAME
                        )
                        .build()
                }
            }
            return instance
        }
    }
}