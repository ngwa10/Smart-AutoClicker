/*
 * Copyright (C) 2024 Kevin Buzeau
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.buzbuz.smartautoclicker.feature.smart.config.ui.action.click

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Refactored ViewModel for Logic Key-only click action.
 * Only "logicKey" is stored and validated.
 */
class ClickViewModel : ViewModel() {

    // State for the logic key input
    private val _logicKey = MutableStateFlow<String?>(null)
    val logicKey: StateFlow<String?> = _logicKey

    // Validation error for logic key
    private val _logicKeyError = MutableStateFlow<String?>(null)
    val logicKeyError: StateFlow<String?> = _logicKeyError

    // Whether the action is valid (logic key is not empty)
    private val _isValidAction = MutableStateFlow(false)
    val isValidAction: StateFlow<Boolean> = _isValidAction

    // Track if currently editing
    private val _isEditingAction = MutableStateFlow(true)
    val isEditingAction: StateFlow<Boolean> = _isEditingAction

    // Tracks unsaved changes
    private var initialLogicKey: String? = null

    /**
     * Set the logic key value and validate.
     */
    fun setLogicKey(value: String) {
        _logicKey.value = value
        _isValidAction.value = !value.isNullOrBlank()
        _logicKeyError.value = if (value.isNullOrBlank()) "Logic key required" else null
    }

    /**
     * Check if there are unsaved modifications to the logic key.
     */
    fun hasUnsavedModifications(): Boolean {
        return _logicKey.value != initialLogicKey
    }

    /**
     * Save the logic key (to be implemented as needed).
     */
    fun saveLastConfig() {
        // Save logic key, e.g., to repository or model if needed
        initialLogicKey = _logicKey.value
        // TODO: Add save logic here if storing logic key in a database or model
    }

    /**
     * Monitor save button views for analytics/accessibility (optional).
     */
    fun monitorViews(saveButton: android.view.View) {
        // Implement if needed
    }

    /**
     * Stop monitoring views (optional).
     */
    fun stopViewMonitoring() {
        // Cleanup if needed
    }
}
