package pro.schmid.sliderbug

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pro.schmid.sliderbug.ui.theme.SliderBugTheme
import kotlin.math.round

private val TAG: String = MainActivity::class.java.toString()

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = MyViewModel()

        setContent {
            SliderBugTheme {
                val dimmerValue by viewModel.value.observeAsState(MyData(0f))
                MySlider(dimmerValue = dimmerValue.value, viewModel::setValue)
            }
        }
    }
}

@Composable
fun MySlider(dimmerValue: Float, valueSetter: (Float) -> Unit) {
    var sliderState by remember(dimmerValue) { mutableStateOf(dimmerValue) }
    Log.d(TAG, "VM value: $dimmerValue")
    Log.d(TAG, "sliderState value: $sliderState")
    Slider(
        value = sliderState,
        onValueChange = {
            Log.d(TAG, "onValueChange value: $it")
            sliderState = it
        },
        valueRange = 0f..100f,
        onValueChangeFinished = {
            Log.d(TAG, "onValueChangeFinished value: $sliderState")
            valueSetter(sliderState)
        }
    )
}

class MyViewModel : ViewModel() {

    val value = MutableLiveData(MyData(50.0f))

    fun setValue(newValue: Float) {
        viewModelScope.launch {
            delay(2000)
            val rounded = round(newValue)
            value.postValue(MyData(rounded))
        }
    }

}

data class MyData(
    val value: Float
)