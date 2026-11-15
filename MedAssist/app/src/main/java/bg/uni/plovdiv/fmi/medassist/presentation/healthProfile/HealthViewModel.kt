package bg.uni.plovdiv.fmi.medassist.presentation.healthProfile

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import bg.uni.plovdiv.fmi.medassist.data.local.database.AppDatabase
import bg.uni.plovdiv.fmi.medassist.data.local.entity.LabTestEntity
import bg.uni.plovdiv.fmi.medassist.data.local.entity.VaccineEntity
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class HealthViewModel(application: Application) : AndroidViewModel(application) {
    private val labTestsDao = AppDatabase.getDatabase(application).labTestDao()
    private val vaccinesDao = AppDatabase.getDatabase(application).vaccineDao()


    // Отделен StateFlow за изследванията
    val labTests: StateFlow<List<LabTestEntity>> = labTestsDao.getAllLabTests()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Отделен StateFlow за ваксините
    val vaccines: StateFlow<List<VaccineEntity>> = vaccinesDao.getAllVaccines()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _scannedText = MutableStateFlow("")
    val scannedText: StateFlow<String> = _scannedText

    fun scanImageWithAI(bitmap: Bitmap) {
        val image = InputImage.fromBitmap(bitmap, 0)
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

        recognizer.process(image)
            .addOnSuccessListener { visionText ->
                _scannedText.value = visionText.text
            }
            .addOnFailureListener {
                _scannedText.value = "Error reading text from image."
            }
    }

    //ADD UNIT TESTS FOR THAT - DO NOT FORGET PLSSS
    private val singleLetterRegex = Regex("""(?i)(?:^|[\s\d])(h|l)(?:$|[\s])""")
    private fun checkForAbnormalities(text: String): Boolean {
        val keywords = listOf(
            "high", "low", "*", "abnormal", "висок", "нисък",
            "↑", "↓", "⬆", "⬇", "▲", "▼"
        )
        return keywords.any { text.contains(it, ignoreCase = true) } ||
                singleLetterRegex.containsMatchIn(text)
    }

    fun saveLabTest(testName: String, extractedText: String) {
        val currentDate = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date())
        val abnormal = checkForAbnormalities(extractedText)

        viewModelScope.launch {
            labTestsDao.insertLabTest(
                LabTestEntity(
                    testName = testName.trim(),
                    date = currentDate,
                    extractedText = extractedText.trim(),
                    hasAbnormalities = abnormal
                )
            )
            _scannedText.value = ""
        }
    }

    fun deleteLabTest(labTest: LabTestEntity) {
        viewModelScope.launch { labTestsDao.deleteLabTest(labTest) }
    }

    fun saveVaccine(vaccineName: String, dateAdministered: String, nextDoseDate: String) {
        viewModelScope.launch {
            vaccinesDao.insertVaccine(
                VaccineEntity(
                    vaccineName = vaccineName.trim(),
                    dateAdministered = dateAdministered.trim(),
                    nextDoseDate = nextDoseDate.trim()
                )
            )
        }
    }

    fun deleteVaccine(vaccine: VaccineEntity) {
        viewModelScope.launch { vaccinesDao.deleteVaccine(vaccine) }
    }
}