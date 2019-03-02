package lab.uro.kitori.samplecoroutine

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import lab.uro.kitori.samplecoroutine.sample.Sample
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

class MainViewModel(
    app: Application
) : AndroidViewModel(app), CoroutineScope {
    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default + job

    val textLiveData = MutableLiveData<String>().apply {
        value = "start..."
    }

    fun start() {
        launch {
            runCatching {
                Timber.d("!!! start... thread= ${Thread.currentThread().name}")

                val sample = Sample()
                sample.sample1()
                sample.sample2()
                sample.sample3()
                sample.sample4()
                sample.sample5()
//                sample.sample6() // !!! コメント解除するとここでブロックされる !!!

                Timber.d("!!! ...end")
            }.onFailure { exception ->
                if (exception is CancellationException)
                    Timber.d("!!! cancel parent: $exception")
            }
        }
    }

    fun cancel() {
        job.cancel()
        textLiveData.value = "cancel!!"
    }

    fun cancelChildren() {
        job.cancelChildren()
        textLiveData.value = "cancelChildren!!"
    }
}
