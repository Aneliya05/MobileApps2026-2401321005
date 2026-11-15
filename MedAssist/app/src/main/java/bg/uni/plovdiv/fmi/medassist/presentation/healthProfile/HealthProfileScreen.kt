package bg.uni.plovdiv.fmi.medassist.presentation.kit

import android.content.Context
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import bg.uni.plovdiv.fmi.medassist.data.local.entity.LabTestEntity
import bg.uni.plovdiv.fmi.medassist.data.local.entity.VaccineEntity
import bg.uni.plovdiv.fmi.medassist.presentation.healthProfile.HealthViewModel
import bg.uni.plovdiv.fmi.medassist.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HealthProfileScreen(
    onBack: () -> Unit,
    viewModel: HealthViewModel = viewModel()
) {
    val context = LocalContext.current
    val labTests by viewModel.labTests.collectAsState()
    val vaccines by viewModel.vaccines.collectAsState()
    val scannedText by viewModel.scannedText.collectAsState()

    var showLabDialog by remember { mutableStateOf(false) }
    var testNameInput by remember { mutableStateOf("") }
    var extractedTextInput by remember { mutableStateOf("") }

    var showVaccineDialog by remember { mutableStateOf(false) }
    var vaccineNameInput by remember { mutableStateOf("") }
    var vaccineDateInput by remember {
        mutableStateOf(SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date()))
    }
    var nextDoseInput by remember { mutableStateOf("") }

    LaunchedEffect(scannedText) {
        if (scannedText.isNotEmpty()) {
            testNameInput = "Blood / Lab Test"
            extractedTextInput = scannedText
            showLabDialog = true
        }
    }

    // CAMERA INTENT
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            viewModel.scanImageWithAI(bitmap)
        }
    }

    Scaffold(
        containerColor = ScreenBackground,
        topBar = {
            TopAppBar(
                title = { Text("Health Profile", fontWeight = FontWeight.Bold, fontSize = 20.sp) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = ScreenBackground)
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // ==========================================
            // СЕКЦИЯ 1: LAB TESTS (AI SCANNER)
            // ==========================================
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Recent Lab Tests",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Button(
                        onClick = { cameraLauncher.launch(null) },
                        colors = ButtonDefaults.buttonColors(containerColor = EmergencyRedLight),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Icon(Icons.Default.CameraAlt, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("AI Scan", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            if (labTests.isEmpty()) {
                item {
                    EmptySectionPlaceholder("No lab tests scanned yet. Tap 'AI Scan' to scan a paper result.")
                }
            } else {
                items(labTests, key = { "lab_${it.id}" }) { lab ->
                    LabTestCard(
                        labTest = lab,
                        onShareClick = { shareLabTest(context, lab) },
                        onDeleteClick = { viewModel.deleteLabTest(lab) }
                    )
                }
            }

            item {
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = CardBorderGray)
            }

            // ==========================================
            // СЕКЦИЯ 2: VACCINES (ИМУНИЗАЦИИ)
            // ==========================================
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Vaccines & Immunizations",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    OutlinedButton(
                        onClick = {
                            vaccineNameInput = ""
                            nextDoseInput = ""
                            showVaccineDialog = true
                        },
                        border = BorderStroke(1.dp, EmergencyRedLight),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, tint = EmergencyRedLight, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Add", color = EmergencyRedLight, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            if (vaccines.isEmpty()) {
                item {
                    EmptySectionPlaceholder("No vaccines recorded yet. Tap '+ Add' to log an immunization.")
                }
            } else {
                items(vaccines, key = { "vac_${it.id}" }) { vaccine ->
                    VaccineCard(
                        vaccine = vaccine,
                        onDeleteClick = { viewModel.deleteVaccine(vaccine) }
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }
        }

        // --- ДИАЛОГ ЗА ЗАПАЗВАНЕ НА ИЗСЛЕДВАНЕ ---
        if (showLabDialog) {
            AlertDialog(
                onDismissRequest = { showLabDialog = false },
                containerColor = Color.White,
                title = { Text("AI Scanned Lab Result", fontWeight = FontWeight.Bold) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(
                            value = testNameInput,
                            onValueChange = { testNameInput = it },
                            label = { Text("Test Name *") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = extractedTextInput,
                            onValueChange = { extractedTextInput = it },
                            label = { Text("Extracted Values") },
                            modifier = Modifier.fillMaxWidth().height(120.dp)
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (testNameInput.isNotBlank()) {
                                viewModel.saveLabTest(testNameInput, extractedTextInput)
                                showLabDialog = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = EmergencyRedLight)
                    ) { Text("Save", color = Color.White) }
                },
                dismissButton = {
                    TextButton(onClick = { showLabDialog = false }) { Text("Cancel") }
                }
            )
        }

        // --- ДИАЛОГ ЗА ДОБАВЯНЕ НА ВАКСИНА ---
        if (showVaccineDialog) {
            AlertDialog(
                onDismissRequest = { showVaccineDialog = false },
                containerColor = Color.White,
                title = { Text("Add Vaccine", fontWeight = FontWeight.Bold) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(
                            value = vaccineNameInput,
                            onValueChange = { vaccineNameInput = it },
                            label = { Text("Vaccine Name * (e.g. Tetanus)") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = vaccineDateInput,
                            onValueChange = { vaccineDateInput = it },
                            label = { Text("Date Administered *") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = nextDoseInput,
                            onValueChange = { nextDoseInput = it },
                            label = { Text("Next Dose / Booster (Optional)") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (vaccineNameInput.isNotBlank() && vaccineDateInput.isNotBlank()) {
                                viewModel.saveVaccine(vaccineNameInput, vaccineDateInput, nextDoseInput)
                                showVaccineDialog = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = EmergencyRedLight)
                    ) { Text("Save", color = Color.White) }
                },
                dismissButton = {
                    TextButton(onClick = { showVaccineDialog = false }) { Text("Cancel") }
                }
            )
        }
    }
}

@Composable
fun LabTestCard(
    labTest: LabTestEntity,
    onShareClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, CardBorderGray)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Science, contentDescription = null, tint = EmergencyRedLight)
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(labTest.testName, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = TextPrimary)
                        Text(labTest.date, fontSize = 12.sp, color = TextSecondary)
                    }
                }
                Row {
                    IconButton(onClick = onShareClick) {
                        Icon(Icons.Default.Share, contentDescription = "Share", tint = Color.Gray, modifier = Modifier.size(20.dp))
                    }
                    IconButton(onClick = onDeleteClick) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Gray, modifier = Modifier.size(20.dp))
                    }
                }
            }

            if (labTest.extractedText.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(labTest.extractedText, fontSize = 13.sp, color = TextSecondary, maxLines = 4)
            }

            Spacer(modifier = Modifier.height(8.dp))
            val badgeColor = if (labTest.hasAbnormalities) SoftPinkBanner else Color(0xFFE8F5E9)
            val badgeTextColor = if (labTest.hasAbnormalities) EmergencyRedDark else Color(0xFF2E7D32)
            val badgeText = if (labTest.hasAbnormalities) "⚠ Abnormal values detected" else "✓ Values look normal"

            Box(
                modifier = Modifier
                    .background(badgeColor, RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(badgeText, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = badgeTextColor)
            }
        }
    }
}

@Composable
fun VaccineCard(
    vaccine: VaccineEntity,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, CardBorderGray)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                Icon(Icons.Default.Vaccines, contentDescription = null, tint = EmergencyRedLight)
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(vaccine.vaccineName, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = TextPrimary)
                    Text("Date: ${vaccine.dateAdministered}", fontSize = 12.sp, color = TextSecondary)
                    if (vaccine.nextDoseDate.isNotBlank()) {
                        Text("Next dose: ${vaccine.nextDoseDate}", fontSize = 12.sp, color = EmergencyRedLight, fontWeight = FontWeight.Medium)
                    }
                }
            }
            IconButton(onClick = onDeleteClick) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Gray, modifier = Modifier.size(20.dp))
            }
        }
    }
}

@Composable
fun EmptySectionPlaceholder(text: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF3F4F6))
    ) {
        Text(
            text = text,
            fontSize = 13.sp,
            color = TextSecondary,
            modifier = Modifier.padding(14.dp)
        )
    }
}

fun shareLabTest(context: Context, labTest: LabTestEntity) {
    val sendIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, "Medical Lab Result: ${labTest.testName}")
        putExtra(
            Intent.EXTRA_TEXT,
            "Lab Test: ${labTest.testName} (${labTest.date})\n\n${labTest.extractedText}"
        )
    }

    val shareIntent = Intent.createChooser(sendIntent, "Share lab result via")

    context.startActivity(shareIntent)
}