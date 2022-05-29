package app.moc.android.ui.career.history.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.moc.model.DateTime
import app.moc.model.Image
import app.moc.model.PlanCheckQueryInfo
import app.moc.shared.domain.career.GetCareerChecksUseCase
import app.moc.shared.domain.image.GetImagesUseCase
import app.moc.shared.result.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import javax.inject.Inject

@HiltViewModel
class CalendarItemDialogViewModel @Inject constructor(
    private val getImagesUseCase: GetImagesUseCase
): ViewModel(){

    private val _images = MutableStateFlow<Result<List<Image>>>(Result.Loading)
    val images = _images.asStateFlow()

    fun getImages(imageTag: String){
        viewModelScope.launch {
            getImagesUseCase(imageTag).collectLatest { result ->
                _images.value = result
            }
        }
    }
}