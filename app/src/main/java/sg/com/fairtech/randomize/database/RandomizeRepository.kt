package sg.com.fairtech.randomize.database

import android.app.Application
import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import sg.com.fairtech.randomize.model.Randomize

class RandomizeRepository(application: Application) {
    private val randomizeDao: RandomizeDao?
    private var randomizeList: LiveData<List<Randomize>>? = null

    init {
        val db = AppDatabase.getInstance(application.applicationContext)
        randomizeDao = db?.RandomizeDao()
        randomizeList = randomizeDao?.getRandomizeList()
    }

    fun getRandomizeList(): LiveData<List<Randomize>>? {
        return randomizeList
    }

    fun insert(randomize: Randomize) = runBlocking {
        this.launch(Dispatchers.IO) {
            randomizeDao?.insertRandomize(randomize)
        }
    }

    fun delete(randomize: Randomize) {
        runBlocking {
            this.launch(Dispatchers.IO) {
                randomizeDao?.deleteRandomize(randomize)
            }
        }
    }

    fun update(randomize: Randomize) = runBlocking {
        this.launch(Dispatchers.IO) {
            randomizeDao?.updateRandomize(randomize)
        }
    }

}