package com.example.inspectorviewapp.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.inspectorviewapp.R
import com.example.inspectorviewapp.viewmodel.StartScreenViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.viewinterop.AndroidView
import android.content.Context
import androidx.compose.ui.unit.Dp
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.clickable
import android.view.Surface
import androidx.compose.ui.platform.LocalConfiguration
import android.view.WindowManager

/**
 * StartScreen composable for InspectorViewApp.
 * Displays CameraX preview, overlays, and handles permissions and errors.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartScreen(
    viewModel: StartScreenViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val heading by viewModel.heading.collectAsState()
    val latitude by viewModel.latitude.collectAsState()
    val longitude by viewModel.longitude.collectAsState()
    val timestamp by viewModel.timestamp.collectAsState()
    val cameraPermissionDenied by viewModel.cameraPermissionDenied.collectAsState()
    val locationPermissionDenied by viewModel.locationPermissionDenied.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // State for rationale dialogs
    var showCameraRationale by remember { mutableStateOf(false) }
    var showLocationRationale by remember { mutableStateOf(false) }

    // Camera permission launcher
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            Timber.d("Camera permission result: $granted")
            if (!granted) {
                viewModel.onCameraPermissionDenied()
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(
                        message = context.getString(R.string.camera_permission_denied)
                    )
                }
            } else {
                // Optionally reset denial state
                viewModel.onCameraPermissionGranted()
            }
        }
    )
    // Location permission launcher
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            Timber.d("Location permission result: $granted")
            if (!granted) {
                viewModel.onLocationPermissionDenied()
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(
                        message = context.getString(R.string.location_permission_denied)
                    )
                }
            } else {
                // Optionally reset denial state
                viewModel.onLocationPermissionGranted()
            }
        }
    )

    // Request camera permission on first launch if not granted
    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Timber.d("Requesting camera permission on launch")
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Black,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(padding)
        ) {
            val isTablet = maxWidth >= 600.dp
            // Responsive dimensions
            val shutterButtonSize = if (isTablet) 96.dp else dimensionResource(id = R.dimen.shutter_button_size)
            val iconSize = if (isTablet) 32.dp else dimensionResource(id = R.dimen.icon_size)
            val topPadding = if (isTablet) 48.dp else 32.dp
            val topEndPadding = if (isTablet) 40.dp else 24.dp
            val iconSpacing = if (isTablet) 16.dp else dimensionResource(id = R.dimen.spacing_small)
            val bottomPadding = if (isTablet) 32.dp else 16.dp
            val sidePadding = if (isTablet) 48.dp else 24.dp
            val headingFontSize = if (isTablet) 20.sp else dimensionResource(id = R.dimen.text_size_large).value.sp
            val bottomFontSize = if (isTablet) 16.sp else dimensionResource(id = R.dimen.text_size_small).value.sp

            // Only show CameraX preview if camera permission is granted
            if (!cameraPermissionDenied && ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                CameraPreviewBox()
            } else {
                // Fallback UI if camera permission is not granted
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = stringResource(R.string.camera_permission_denied),
                            color = Color.White,
                            fontSize = 18.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = {
                            // Show rationale if needed
                            if (androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale(
                                    (context as? android.app.Activity) ?: return@Button,
                                    Manifest.permission.CAMERA
                                )
                            ) {
                                showCameraRationale = true
                            } else {
                                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                            }
                        }) {
                            Text("Grant Camera Permission")
                        }
                    }
                }
            }

            // Top overlays: Heading and icons
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = topPadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.heading_format, heading),
                    color = Color.White,
                    fontSize = headingFontSize
                )
            }
            Row(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = topEndPadding, end = topEndPadding),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { /* TODO: Settings action */ }) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = stringResource(R.string.settings),
                        tint = Color.White,
                        modifier = Modifier.size(iconSize)
                    )
                }
                Spacer(modifier = Modifier.width(iconSpacing))
                IconButton(onClick = { /* TODO: Close action */ }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(R.string.close),
                        tint = Color.White,
                        modifier = Modifier.size(iconSize)
                    )
                }
            }
            // Shutter button (bottom center)
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = bottomPadding)
            ) {
                Button(
                    onClick = {
                        // Request camera permission in-context
                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            if (androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale(
                                    (context as? android.app.Activity) ?: return@Button,
                                    Manifest.permission.CAMERA
                                )
                            ) {
                                showCameraRationale = true
                            } else {
                                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                            }
                        } else {
                            // TODO: Shutter action
                        }
                    },
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = dimensionResource(id = R.dimen.shutter_button_elevation)
                    ),
                    modifier = Modifier
                        .size(shutterButtonSize)
                        .testTag("ShutterButton")
                ) {}
            }
            // Bottom overlays: GPS and timestamp
            Row(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .padding(start = sidePadding, end = sidePadding, bottom = bottomPadding),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.gps_format, latitude, longitude),
                    color = if (locationPermissionDenied) Color.Red else Color.White,
                    fontSize = bottomFontSize,
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            // Request location permission only when user taps GPS
                            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                Timber.d("Requesting location permission on GPS tap")
                                if (androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale(
                                        (context as? android.app.Activity) ?: return@clickable,
                                        Manifest.permission.ACCESS_FINE_LOCATION
                                    )
                                ) {
                                    showLocationRationale = true
                                } else {
                                    locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                                }
                            }
                        }
                )
                Text(
                    text = timestamp,
                    color = Color.White,
                    fontSize = bottomFontSize,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }

            // Camera rationale dialog
            if (showCameraRationale) {
                AlertDialog(
                    onDismissRequest = { showCameraRationale = false },
                    title = { Text("Camera Permission Required") },
                    text = { Text("This feature requires camera access to capture photos. Please grant permission.") },
                    confirmButton = {
                        TextButton(onClick = {
                            Timber.d("User confirmed camera rationale dialog")
                            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                            showCameraRationale = false
                        }) { Text("Continue") }
                    },
                    dismissButton = {
                        TextButton(onClick = { showCameraRationale = false }) { Text("Not now") }
                    }
                )
            }
            // Location rationale dialog
            if (showLocationRationale) {
                AlertDialog(
                    onDismissRequest = { showLocationRationale = false },
                    title = { Text("Location Permission Required") },
                    text = { Text("This feature requires location access to display GPS coordinates. Please grant permission.") },
                    confirmButton = {
                        TextButton(onClick = {
                            Timber.d("User confirmed location rationale dialog")
                            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                            showLocationRationale = false
                        }) { Text("Continue") }
                    },
                    dismissButton = {
                        TextButton(onClick = { showLocationRationale = false }) { Text("Not now") }
                    }
                )
            }
        }
    }
}

@Composable
fun getDisplayRotation(): Int {
    val context = LocalContext.current
    val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        context.display?.rotation ?: Surface.ROTATION_0
    } else {
        @Suppress("DEPRECATION")
        windowManager.defaultDisplay.rotation
    }
}

/**
 * CameraX Preview composable using AndroidView.
 * Fills the background with the camera preview.
 */
@Composable
fun CameraPreviewBox() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val rotation = getDisplayRotation()
    AndroidView(
        factory = { ctx: Context ->
            val previewView = PreviewView(ctx).apply {
                implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                scaleType = PreviewView.ScaleType.FILL_CENTER
            }
            val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
            cameraProviderFuture.addListener({
                try {
                    val cameraProvider = cameraProviderFuture.get()
                    val preview = Preview.Builder()
                        .setTargetRotation(rotation)
                        .build().also {
                            it.setSurfaceProvider(previewView.surfaceProvider)
                        }
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        CameraSelector.DEFAULT_BACK_CAMERA,
                        preview
                    )
                } catch (e: Exception) {
                    Timber.e(e, "CameraX binding failed")
                }
            }, ContextCompat.getMainExecutor(ctx))
            previewView
        },
        modifier = Modifier
            .fillMaxSize()
            .testTag("CameraPreviewBox")
    )
} 