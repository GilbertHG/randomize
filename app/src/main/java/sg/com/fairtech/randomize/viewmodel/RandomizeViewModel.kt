package sg.com.fairtech.randomize.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import sg.com.fairtech.randomize.database.RandomizeRepository
import sg.com.fairtech.randomize.model.Randomize

class RandomizeViewModel(application: Application): AndroidViewModel(application) {
    private var randomizeRepository = RandomizeRepository(application)
    private var randomizeList: LiveData<List<Randomize>>? = randomizeRepository.getRandomizeList()

    fun getRandomizeList(): LiveData<List<Randomize>>? {
        return randomizeList
    }

    fun insertRandomize(randomize: Randomize) {
        randomizeRepository.insert(randomize)
    }

    fun updateRandomize(randomize: Randomize) {
        randomizeRepository.update(randomize)
    }

    fun deleteRandomize(randomize: Randomize) {
        randomizeRepository.delete(randomize)
    }

}