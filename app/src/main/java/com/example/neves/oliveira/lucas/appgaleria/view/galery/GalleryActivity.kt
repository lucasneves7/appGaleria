package com.example.neves.oliveira.lucas.appgaleria.view.galery

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.rememberImagePainter
import com.example.neves.oliveira.lucas.appgaleria.ui.theme.AppGaleriaTheme
import com.example.neves.oliveira.lucas.appgaleria.view.image.ImageViewActivity
import org.koin.androidx.compose.koinViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Activity responsável por exibir a galeria de imagens e permitir a captura de novas imagens.
 */
class GalleryActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Ativa o suporte a tela cheia.

        setContent {
            AppGaleriaTheme {
                GridViewer(this)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GridViewer(context: Context, viewModel: GalleryVM = koinViewModel()) {
    val items by remember { derivedStateOf { viewModel.items } }

    val photoFile = remember {
        createImageFile(context)
    }

    val photoUri = remember {
        FileProvider.getUriForFile(
            context,
            "${context.applicationContext.packageName}.provider",
            photoFile
        )
    }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            // A imagem foi salva com sucesso no URI fornecido
            viewModel.addItem(photoFile)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("App Galeria") },
                colors = TopAppBarDefaults.topAppBarColors().copy(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                actions = {
                    IconButton({
                        // Solicitar permissão para câmera
                        if (ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.CAMERA
                            ) == PackageManager.PERMISSION_GRANTED
                        ) {
                            // Abre a câmera
                            cameraLauncher.launch(photoUri)
                        } else {
                            // Solicitar permissão para câmera
                            ActivityCompat.requestPermissions(
                                context as Activity,
                                arrayOf(Manifest.permission.CAMERA),
                                1001
                            )
                        }
                    }) {
                        Icon(Icons.Filled.CameraAlt, contentDescription = "Abrir câmera")
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            contentPadding = PaddingValues(4.dp),
            modifier = Modifier.fillMaxSize().padding(innerPadding)
        ) {
            items(items) { item ->
                Image(
                    painter = rememberImagePainter(item),
                    contentDescription = "Imagem selecionada",
                    modifier = Modifier
                        .size(160.dp)
                        .border(2.dp, MaterialTheme.colorScheme.tertiary)
                        .clickable {
                            val intent = Intent(context, ImageViewActivity::class.java)
                            intent.putExtra(ImageViewActivity.FILE, item)
                            context.startActivity(intent)
                        }
                )
            }
        }
    }
}

/**
 * Cria um arquivo temporário para armazenar a imagem capturada.
 * @param context Contexto da aplicação.
 * @return Arquivo temporário criado.
 */
private fun createImageFile(context: Context): File {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    val storageDir = context.cacheDir // Usa o diretório de cache
    return File.createTempFile(
        imageFileName, /* prefix */
        ".jpg", /* suffix */
        storageDir /* directory */
    )
}