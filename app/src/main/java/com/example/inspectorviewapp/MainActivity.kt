package com.example.inspectorviewapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.inspectorviewapp.ui.theme.InspectorViewAppTheme
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.rememberDropdownMenuState
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inspectorviewapp.viewmodel.ProjectViewModel
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.text.style.TextAlign
import java.io.File
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import com.example.inspectorviewapp.viewmodel.PhotoViewModel
import androidx.compose.runtime.livedata.observeAsState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.TextButton
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.TextFieldValue
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MapProperties
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ExperimentalMaterial3Api
import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview as CameraPreview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import android.provider.OpenableColumns
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.foundation.layout.align

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            InspectorViewAppTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavHost(navController = navController)
                }
            }
        }
    }
}

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "dashboard") {
        composable("dashboard") { DashboardScreen(navController) }
        composable("project_create") { ProjectCreateScreen(navController) }
        composable("inspection_camera") { InspectionCameraScreen(navController) }
        composable("map") { MapScreen(navController) }
        composable("project_detail/{projectId}") { backStackEntry ->
            val projectId = backStackEntry.arguments?.getString("projectId")
            ProjectDetailScreen(navController, projectId)
        }
    }
}

@Composable
fun DashboardScreen(navController: NavHostController) {
    val projectViewModel: ProjectViewModel = viewModel()
    val projects = projectViewModel.allProjects.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.SpaceBetween) {
            Button(onClick = { navController.navigate("map") }) {
                Text("View All on Map")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { navController.navigate("project_create") }) {
                Text("Add Project")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn {
            items(projects.value) { project ->
                Card(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { navController.navigate("project_detail/${project.id}") },
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = project.name, style = MaterialTheme.typography.titleLarge)
                        Text(text = project.type, style = MaterialTheme.typography.bodyMedium)
                        Text(text = project.location, style = MaterialTheme.typography.bodySmall)
                        if (!project.notes.isNullOrBlank()) {
                            Text(text = project.notes ?: "", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
    // Floating action button for quick add
    FloatingActionButton(
        onClick = { navController.navigate("project_create") },
        modifier = Modifier.align(Alignment.End)
    ) {
        Icon(Icons.Filled.Add, contentDescription = "Add Project")
    }
}

@Composable
fun ProjectCreateScreen(navController: NavHostController) {
    val context = LocalContext.current
    val projectViewModel: ProjectViewModel = viewModel()
    var name by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var pdfUri by remember { mutableStateOf<Uri?>(null) }
    var pdfName by remember { mutableStateOf("") }
    var typeMenuExpanded by remember { mutableStateOf(false) }
    val projectTypes = listOf("Roadway", "Bridge", "Utility", "Other")

    val pdfPickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        pdfUri = uri
        pdfName = uri?.let {
            val cursor = context.contentResolver.query(it, null, null, null, null)
            val nameIndex = cursor?.getColumnIndex(OpenableColumns.DISPLAY_NAME) ?: -1
            val name = if (cursor != null && cursor.moveToFirst() && nameIndex >= 0) {
                cursor.getString(nameIndex)
            } else "PDF Selected"
            cursor?.close()
            name
        } ?: ""
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp), verticalArrangement = Arrangement.Top) {
        Text("Create New Project", style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Project Name") },
            modifier = Modifier.fillMaxSize(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = location,
            onValueChange = { location = it },
            label = { Text("General Location") },
            modifier = Modifier.fillMaxSize(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = notes,
            onValueChange = { notes = it },
            label = { Text("Notes (optional)") },
            modifier = Modifier.fillMaxSize(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
        )
        Spacer(modifier = Modifier.height(8.dp))
        // Project Type Dropdown
        Button(onClick = { typeMenuExpanded = true }) {
            Text(if (type.isBlank()) "Select Project Type" else type)
        }
        DropdownMenu(
            expanded = typeMenuExpanded,
            onDismissRequest = { typeMenuExpanded = false }
        ) {
            projectTypes.forEach { t ->
                DropdownMenuItem(
                    text = { Text(t) },
                    onClick = {
                        type = t
                        typeMenuExpanded = false
                    }
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        // PDF Upload
        Button(onClick = { pdfPickerLauncher.launch("application/pdf") }) {
            Text(if (pdfUri == null) "Upload PDF (optional)" else pdfName)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (name.isNotBlank() && type.isNotBlank() && location.isNotBlank()) {
                    projectViewModel.insertProject(
                        com.example.inspectorviewapp.data.Project(
                            name = name,
                            type = type,
                            location = location,
                            notes = if (notes.isBlank()) null else notes,
                            pdfPath = pdfUri?.toString()
                        )
                    )
                    navController.popBackStack()
                }
            },
            enabled = name.isNotBlank() && type.isNotBlank() && location.isNotBlank()
        ) {
            Text("Create Project")
        }
    }
}

@Composable
fun InspectionCameraScreen(navController: NavHostController) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val photoViewModel: PhotoViewModel = viewModel()
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        )
    }
    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        )
    }
    val cameraPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        hasCameraPermission = granted
    }
    val locationPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        hasLocationPermission = granted
    }

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        if (!hasLocationPermission) locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    var imageCapture: ImageCapture? by remember { mutableStateOf(null) }
    var capturedUri by remember { mutableStateOf<Uri?>(null) }
    var note by remember { mutableStateOf("") }
    var isSaving by remember { mutableStateOf(false) }
    var gpsStatus by remember { mutableStateOf("Fetching GPS...") }
    var latitude by remember { mutableStateOf<Double?>(null) }
    var longitude by remember { mutableStateOf<Double?>(null) }

    fun fetchLocation() {
        if (hasLocationPermission) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    latitude = location.latitude
                    longitude = location.longitude
                    gpsStatus = "GPS: %.5f, %.5f".format(latitude, longitude)
                } else {
                    gpsStatus = "Unable to get location"
                }
            }.addOnFailureListener {
                gpsStatus = "Unable to get location"
            }
        } else {
            gpsStatus = "No location permission"
        }
    }

    if (!hasCameraPermission) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Camera permission required.")
        }
        return
    }

    if (capturedUri == null) {
        Box(modifier = Modifier.fillMaxSize()) {
            AndroidView(
                factory = { ctx ->
                    val previewView = PreviewView(ctx)
                    val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                    cameraProviderFuture.addListener({
                        val cameraProvider = cameraProviderFuture.get()
                        val preview = CameraPreview.Builder().build().also {
                            it.setSurfaceProvider(previewView.surfaceProvider)
                        }
                        imageCapture = ImageCapture.Builder().build()
                        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                        try {
                            cameraProvider.unbindAll()
                            cameraProvider.bindToLifecycle(
                                lifecycleOwner,
                                cameraSelector,
                                preview,
                                imageCapture
                            )
                        } catch (exc: Exception) {
                            Log.e("CameraX", "Use case binding failed", exc)
                        }
                    }, ContextCompat.getMainExecutor(ctx))
                    previewView
                },
                modifier = Modifier.fillMaxSize()
            )
            // Large capture button
            Button(
                onClick = {
                    val file = File(
                        context.cacheDir,
                        "IMG_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(System.currentTimeMillis())}.jpg"
                    )
                    val outputOptions = ImageCapture.OutputFileOptions.Builder(file).build()
                    imageCapture?.takePicture(
                        outputOptions,
                        ContextCompat.getMainExecutor(context),
                        object : ImageCapture.OnImageSavedCallback {
                            override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                                capturedUri = output.savedUri ?: Uri.fromFile(file)
                                fetchLocation()
                            }
                            override fun onError(exc: ImageCaptureException) {
                                Toast.makeText(context, "Photo capture failed: ${exc.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                },
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 48.dp)
                    .size(80.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {}
        }
    } else {
        // After capture: show preview, note input, GPS, Save
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Image preview
            capturedUri?.let { uri ->
                AndroidView(
                    factory = { ctx ->
                        val imageView = android.widget.ImageView(ctx)
                        imageView.setImageURI(uri)
                        imageView.scaleType = android.widget.ImageView.ScaleType.CENTER_CROP
                        imageView
                    },
                    modifier = Modifier
                        .size(240.dp)
                        .clip(RoundedCornerShape(16.dp))
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                label = { Text("Add a note (optional)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(gpsStatus, color = Color.White, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    isSaving = true
                    // Save photo, note, and GPS to Room
                    val filePath = capturedUri?.path ?: ""
                    val lat = latitude ?: 0.0
                    val lng = longitude ?: 0.0
                    photoViewModel.insertPhoto(
                        com.example.inspectorviewapp.data.Photo(
                            projectId = 0L, // TODO: Pass projectId via nav args
                            filePath = filePath,
                            note = note,
                            latitude = lat,
                            longitude = lng,
                            timestamp = System.currentTimeMillis()
                        )
                    )
                    navController.popBackStack()
                },
                enabled = !isSaving,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (isSaving) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                else Text("Save Inspection Photo")
            }
            Spacer(modifier = Modifier.height(16.dp))
            TextButton(onClick = { capturedUri = null }, modifier = Modifier.fillMaxWidth()) {
                Text("Retake Photo", color = Color.White)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(navController: NavHostController) {
    val context = LocalContext.current
    val photoViewModel: PhotoViewModel = viewModel()
    val projectViewModel: ProjectViewModel = viewModel()
    val allPhotos = photoViewModel.allPhotos.collectAsState().value
    val allProjects = projectViewModel.allProjects.collectAsState().value
    var selectedPhoto by remember { mutableStateOf<com.example.inspectorviewapp.data.Photo?>(null) }
    val cameraPositionState = rememberCameraPositionState()

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            uiSettings = MapUiSettings(zoomControlsEnabled = true),
            properties = MapProperties(isMyLocationEnabled = true)
        ) {
            allPhotos.forEach { photo ->
                val project = allProjects.find { it.id == photo.projectId }
                Marker(
                    state = MarkerState(position = com.google.android.gms.maps.model.LatLng(photo.latitude, photo.longitude)),
                    title = project?.name ?: "Project",
                    snippet = photo.note,
                    onClick = {
                        selectedPhoto = photo
                        true
                    }
                )
            }
        }
        if (selectedPhoto != null) {
            val project = allProjects.find { it.id == selectedPhoto!!.projectId }
            ModalBottomSheet(onDismissRequest = { selectedPhoto = null }) {
                Column(modifier = Modifier.padding(24.dp)) {
                    // Photo preview placeholder
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFE0E0E0)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("IMG", color = Color.Gray)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(project?.name ?: "Project", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text(selectedPhoto?.note ?: "", fontSize = 16.sp)
                    Text(
                        SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date(selectedPhoto!!.timestamp)),
                        fontSize = 12.sp, color = Color.Gray
                    )
                    Text("Lat: %.5f, Lng: %.5f".format(selectedPhoto!!.latitude, selectedPhoto!!.longitude), fontSize = 12.sp, color = Color.Gray)
                }
            }
        }
    }
}

@Composable
fun ProjectDetailScreen(navController: NavHostController, projectId: String?) {
    val context = LocalContext.current
    val projectViewModel: ProjectViewModel = viewModel()
    val photoViewModel: PhotoViewModel = viewModel()
    val project = projectId?.toLongOrNull()?.let { id -> projectViewModel.allProjects.value.find { it.id == id } }
    val photos = projectId?.toLongOrNull()?.let { photoViewModel.getPhotosByProjectId(it).collectAsState().value } ?: emptyList()

    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFFF8F8F8))
        .padding(16.dp)) {
        // Project Info Card
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            shadowElevation = 4.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(project?.name ?: "Project", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text(project?.type ?: "", fontSize = 16.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(4.dp))
                Text(project?.location ?: "", fontSize = 14.sp, color = Color.Gray)
                if (!project?.notes.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(project?.notes ?: "", fontSize = 14.sp, color = Color.DarkGray)
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Button(onClick = { navController.navigate("inspection_camera?projectId=$projectId") }) {
                Text("Add Inspection Photo")
            }
            Button(onClick = { navController.navigate("map?projectId=$projectId") }) {
                Text("View on Map")
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text("Inspection Photos", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
        Divider(modifier = Modifier.padding(vertical = 8.dp))
        if (photos.isEmpty()) {
            Text("No inspection photos yet.", color = Color.Gray, modifier = Modifier.padding(16.dp))
        } else {
            LazyColumn {
                items(photos) { photo ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Thumbnail placeholder (replace with real image loading)
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFE0E0E0)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("IMG", color = Color.Gray)
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(photo.note, fontWeight = FontWeight.Medium, maxLines = 1, overflow = TextOverflow.Ellipsis)
                            Text(
                                SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date(photo.timestamp)),
                                fontSize = 12.sp, color = Color.Gray
                            )
                            Text("Lat: %.5f, Lng: %.5f".format(photo.latitude, photo.longitude), fontSize = 12.sp, color = Color.Gray)
                        }
                    }
                    Divider()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    InspectorViewAppTheme {
        Greeting("Android")
    }
}