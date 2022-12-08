package sg.com.fairtech.randomize.database

import androidx.lifecycle.LiveData
import androidx.room.*
import sg.com.fairtech.randomize.model.Randomize

@Dao
interface RandomizeDao {
    @Query("SELECT * FROM randomize_list")
    fun getRandomizeList(): LiveData<List<Randomize>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRandomize(randomize: Randomize)

    @Delete
    suspend fun deleteRandomize(randomize: Randomize)

    @Update
    suspend fun updateRandomize(randomize: Randomize)

}