package com.example.neves.oliveira.lucas.appgaleria.view.image

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import androidx.core.content.FileProvider
import coil.compose.rememberImagePainter
import com.example.neves.oliveira.lucas.appgaleria.ui.theme.AppGaleriaTheme
import java.io.File

/**
 * Activity responsável por exibir uma imagem a partir de um arquivo recebido via Intent.
 */
class ImageViewActivity : ComponentActivity() {

    companion object {
        /**
         * Chave usada para passar o arquivo na Intent.
         */
        const val FILE = "file"
    }

    /**
     * Obtém o arquivo recebido via Intent.
     */
    val file: File? get() = intent.getSerializableExtra(FILE) as? File

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Ativa o suporte a tela cheia.

        setContent {
            AppGaleriaTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            navigationIcon = {
                                IconButton(onClick = { finish() }) { // Botão para voltar à tela anterior.
                                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                                }
                            },
                            title = {
                                Text(
                                    text = file?.name ?: "Arquivo", // Exibe o nome do arquivo ou "Arquivo" caso nulo.
                                    maxLines = 1,
                                    overflow = Ellipsis
                                )
                            },
                            colors = TopAppBarDefaults.topAppBarColors().copy(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            ),
                            actions = {
                                file?.let {
                                    IconButton(onClick = {
                                        // Obtém URI do arquivo para compartilhamento.
                                        val uri = FileProvider.getUriForFile(
                                            this@ImageViewActivity,
                                            "${this@ImageViewActivity.packageName}.provider",
                                            it
                                        )
                                        val intent = Intent(Intent.ACTION_SEND).apply {
                                            type = "image/*"
                                            putExtra(Intent.EXTRA_STREAM, uri)
                                        }
                                        startActivity(Intent.createChooser(intent, "Compartilhar imagem"))
                                    }) {
                                        Icon(Icons.Filled.Share, contentDescription = "Compartilhar")
                                    }
                                }
                            }
                        )
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentAlignment = Alignment.Center
                    ) {
                        file?.let {
                            // Exibe a imagem caso o arquivo exista.
                            Image(
                                painter = rememberImagePainter(it),
                                contentDescription = "Imagem",
                                modifier = Modifier.fillMaxSize()
                            )
                        } ?: Text("Arquivo não encontrado") // Exibe mensagem caso o arquivo não exista.
                    }
                }
            }
        }
    }
}