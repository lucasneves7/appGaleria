package com.example.neves.oliveira.lucas.appgaleria.view.galery

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import java.io.File

/**
 * ViewModel responsável por gerenciar a galeria de imagens.
 */
class GalleryVM : ViewModel() {

    /**
     * Lista mutável de arquivos que representa os itens da galeria.
     */
    private val _items = mutableStateListOf<File>()

    /**
     * Lista pública e imutável de arquivos exposta para a UI.
     */
    val items: List<File> = _items

    /**
     * Adiciona um novo arquivo à lista da galeria.
     * @param item Arquivo a ser adicionado.
     */
    fun addItem(item: File) {
        _items.add(item)
    }
}