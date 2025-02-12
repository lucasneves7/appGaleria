package com.example.neves.oliveira.lucas.appgaleria

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.neves.oliveira.lucas.appgaleria.view.galery.GalleryActivity
import com.example.neves.oliveira.lucas.appgaleria.view.galery.GalleryVM
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module

/**
 * Activity principal responsável pela inicialização do aplicativo.
 *
 * - Inicializa o Koin para injeção de dependências, se ainda não estiver inicializado.
 * - Redireciona imediatamente para a `GalleryActivity`.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializa o Koin para injeção de dependências, se ainda não estiver inicializado.
        if (savedInstanceState == null) {
            startKoin {
                // Define o contexto do Android para o Koin
                androidContext(this@MainActivity)
                // Registra os módulos de dependências do Koin
                modules(appModule)
            }
        }

        // Inicia a GalleryActivity e finaliza esta Activity
        val intent = Intent(this, GalleryActivity::class.java)
        startActivity(intent)
        finish()
    }
}

/**
 * Módulo de dependências do Koin.
 * Define a injeção de `GalleryVM` como ViewModel para a `GalleryActivity`.
 */
private val appModule = module {
    viewModel { GalleryVM() }
}