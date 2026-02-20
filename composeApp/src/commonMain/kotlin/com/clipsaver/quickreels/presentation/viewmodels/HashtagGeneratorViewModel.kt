package com.clipsaver.quickreels.presentation.viewmodels

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clipsaver.quickreels.Platform
import com.clipsaver.quickreels.data.remote.NetworkHelper
import com.clipsaver.quickreels.data.remote.models.Tag
import com.clipsaver.quickreels.models.TrendingCategory
import com.clipsaver.quickreels.ui.theme.PrimaryColor //import com.clipsaver.quickreels.ui.theme.PrimaryColor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class HashtagGeneratorViewModel(
        private val platform: Platform,
        private val networkHelper: NetworkHelper,
        private val hashtagRepo: com.clipsaver.quickreels.domain.repositories.HashtagRepo
) : ViewModel() {

    private val _inputText = MutableStateFlow("")
    val inputText: StateFlow<String> = _inputText.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    data class TagUiState(val tag: Tag, val isSelected: Boolean = false)

    private val _generatedHashtags = MutableStateFlow<List<TagUiState>>(emptyList())
    val trendingCategories =
            listOf(
                    TrendingCategory("Travel", Icons.Default.Flight, PrimaryColor),
                    TrendingCategory(
                            "Food",
                            Icons.Default.Restaurant,
                            Color(0xFF22C55E)
                    ), // Green-500
                    TrendingCategory(
                            "Fitness",
                            Icons.Default.FitnessCenter,
                            Color(0xFFF97316)
                    ), // Orange-500
                    TrendingCategory("Art", Icons.Default.Palette, Color(0xFFA855F7)), // Purple-500
                    TrendingCategory(
                            "Fashion",
                            Icons.Default.Checkroom,
                            Color(0xFFEC4899)
                    ) // Pink-500
            )
    var lastUserInput = ""
    val generatedHashtags: StateFlow<List<TagUiState>> = _generatedHashtags.asStateFlow()

    private val _history = MutableStateFlow<List<String>>(emptyList())
    val history: StateFlow<List<String>> = _history.asStateFlow()

    init {
        loadHistory()
    }

    val trendingTopics = listOf("Travel", "Food", "Fitness", "Nature", "Art", "Fashion", "Tech")

    private fun loadHistory() {
        val jsonString = hashtagRepo.getTags()
        if (jsonString.isNotBlank() && jsonString != "[]") {
            try {
                _history.value = Json.decodeFromString(jsonString)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun onInputTextChanged(text: String) {
        if (text.length <= 500) {
            _inputText.value = text
        }
    }
    fun regenrateTags() {
        generateHashtags("Some New tags on $lastUserInput")
    }

    fun onTopicSelected(
            topic: String
    ) { // Append topic or replace? Usually append or set as context.
        // For now, let's just append it to the text if not present
        if (!_inputText.value.contains(topic, ignoreCase = true)) {
            val prefix =
                    if (_inputText.value.isNotEmpty() && !_inputText.value.endsWith(" ")) " "
                    else ""
            _inputText.value += "$prefix$topic"
        }
    }

    fun closeKeyBoard() {
        platform.closeKeyBoard()
    }

    fun generateHashtags(query: String) {
        if (query.isBlank()) return

        viewModelScope.launch {
            _isLoading.value = true

            runCatching {
                val tags = networkHelper.fetchHashTags(query).getOrThrow().tags.orEmpty()
                val processedTags =
                    tags.map {
                        val tagWithHash =
                            if (it.tag?.startsWith("#") == true) it
                            else it.copy(tag = "#${it.tag}")
                        TagUiState(tagWithHash, isSelected = false) // Default to not selected
                    }

                _generatedHashtags.value = processedTags
                lastUserInput = query

                if (processedTags.isNotEmpty()) {
                    val tagsString = processedTags.joinToString(" ") { it.tag.tag.orEmpty() }
                    addToHistory(tagsString)
                }
            }.onFailure {
                println("generateHashtags() Error ${it.message}")
            }
            _isLoading.value = false

        }
    }

    fun toggleSelection(index: Int) {
        val currentList = _generatedHashtags.value.toMutableList()
        if (index in currentList.indices) {
            val item = currentList[index]
            currentList[index] = item.copy(isSelected = !item.isSelected)
            _generatedHashtags.value = currentList
        }
    }

    fun toggleSelectAll() {
        val currentList = _generatedHashtags.value
        val allSelected = currentList.all { it.isSelected }

        val newList = currentList.map { it.copy(isSelected = !allSelected) }
        _generatedHashtags.value = newList
    }

    private fun addToHistory(tagsString: String) {
        val currentList = _history.value.toMutableList()
        // Avoid duplicates or move to top
        currentList.remove(tagsString)
        currentList.add(0, tagsString)
        _history.value = currentList
        saveHistory()
    }

    fun deleteHistoryItem(item: String) {
        val currentList = _history.value.toMutableList()
        currentList.remove(item)
        _history.value = currentList
        saveHistory()
    }

    private fun saveHistory() {
        try {
            val jsonString = Json.encodeToString(_history.value)
            hashtagRepo.saveTags(jsonString)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun copyHashtags() {
        val currentList = _generatedHashtags.value
        val selectedTags = currentList.filter { it.isSelected }

        if (selectedTags.isEmpty()) {
            platform.showToast("Please select tags to copy")
            return
        }

        val textToCopy = selectedTags.joinToString(" ") { it.tag.tag.orEmpty() }
        if (textToCopy.isNotBlank()) {
            platform.copyToClipboard(textToCopy)
            platform.showToast("Selected hashtags copied!")
        }
    }

    fun copyHistoryItem(item: String) {
        if (item.isNotBlank()) {
            platform.copyToClipboard(item)
            platform.showToast("Copied to clipboard")
        }
    }

    fun clearInput() {
        _inputText.value = ""
        _generatedHashtags.value = emptyList()
    }
}
