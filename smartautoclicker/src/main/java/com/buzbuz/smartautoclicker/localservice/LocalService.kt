/*
 * LocalService.kt
 *
 * Provides the local API for starting/stopping scenarios and executing Logic Key actions.
 * Updated: Supports Logic Key → Workflow execution mapping (trade, pre-trade, random).
 */

package com.buzbuz.smartautoclicker.localservice

import android.content.Context
import android.util.Log
import com.buzbuz.smartautoclicker.core.common.actions.AndroidActionExecutor
import com.buzbuz.smartautoclicker.core.domain.model.scenario.Scenario
import com.buzbuz.smartautoclicker.core.dumb.domain.model.DumbScenario
import com.buzbuz.smartautoclicker.feature.smart.debugging.domain.DebuggingRepository
import com.buzbuz.smartautoclicker.feature.revenue.IRevenueRepository
import com.buzbuz.smartautoclicker.core.settings.SettingsRepository
import com.buzbuz.smartautoclicker.core.common.overlays.manager.OverlayManager
import com.buzbuz.smartautoclicker.core.base.data.AppComponentsProvider
import com.buzbuz.smartautoclicker.core.processing.domain.DetectionRepository

class LocalService(
    private val context: Context,
    private val overlayManager: OverlayManager,
    private val appComponentsProvider: AppComponentsProvider,
    private val detectionRepository: DetectionRepository,
    private val dumbEngine: com.buzbuz.smartautoclicker.core.dumb.engine.DumbEngine,
    private val debugRepository: DebuggingRepository,
    private val revenueRepository: IRevenueRepository,
    private val settingsRepository: SettingsRepository,
    private val actionExecutor: AndroidActionExecutor,
    private val onStart: (Long, Boolean, android.app.Notification?) -> Unit,
    private val onStop: () -> Unit,
) {

    var isStarted: Boolean = false
        private set

    /** Called when a Dumb Scenario starts */
    fun startDumbScenario(dumbScenario: DumbScenario) {
        isStarted = true
        Log.i(TAG, "Starting Dumb Scenario: $dumbScenario")
        onStart.invoke(-1, false, null)
    }

    /** Called when a Smart Scenario starts */
    fun startSmartScenario(resultCode: Int, data: android.content.Intent, scenario: Scenario) {
        isStarted = true
        Log.i(TAG, "Starting Smart Scenario: ${scenario.name}")
        onStart.invoke(scenario.id, true, null)
    }

    /** Stops any running scenario */
    fun stop() {
        if (!isStarted) return
        Log.i(TAG, "Stopping LocalService scenario")
        isStarted = false
        onStop.invoke()
    }

    /** Clean up resources */
    fun release() {
        Log.i(TAG, "Releasing LocalService resources")
        isStarted = false
    }

    /**
     * Handle a Logic Key execution request.
     * This is triggered by a Click button that has a Logic Key assigned.
     */
    fun executeLogicKey(logicKey: String) {
        Log.i(TAG, "Executing Logic Key: $logicKey")

        when (logicKey) {
            // Pre-trade setup
            "/trigamt" -> runPreTradeWorkflow("Trigger Amount")
            "/incamt" -> runPreTradeWorkflow("Increase Amount")
            "/decamt" -> runPreTradeWorkflow("Decrease Amount")
            "/tftrig" -> runPreTradeWorkflow("Timeframe Trigger")

            // Currency search workflow
            "/cplist" -> runCurrencyWorkflow("Open Currency List")
            "/search" -> runCurrencyWorkflow("Search Currency")
            "/confcur" -> runCurrencyWorkflow("Confirm Currency")

            // Free/random human-like exploration
            "/free" -> runRandomizedWorkflow("Free Exploration")

            // Trade execution
            "/buy1" -> runTradeWorkflow("BUY")
            "/sell1" -> runTradeWorkflow("SELL")

            else -> {
                Log.w(TAG, "Unknown Logic Key: $logicKey")
            }
        }
    }

    /** Runs pre-trade workflow actions (e.g. amount, timeframe setup). */
    private fun runPreTradeWorkflow(action: String) {
        Log.d(TAG, "Running Pre-Trade Workflow: $action")
        actionExecutor.executeAction(action)
    }

    /** Runs currency-related workflow (list, search, confirm). */
    private fun runCurrencyWorkflow(action: String) {
        Log.d(TAG, "Running Currency Workflow: $action")
        actionExecutor.executeAction(action)
    }

    /** Runs randomized non-trade workflow (simulated human exploration). */
    private fun runRandomizedWorkflow(action: String) {
        Log.d(TAG, "Running Randomized Workflow: $action")
        actionExecutor.executeAction(action)
    }

    /** Runs trade workflow (/buy1, /sell1). */
    private fun runTradeWorkflow(direction: String) {
        Log.d(TAG, "Running Trade Workflow: $direction")
        actionExecutor.executeAction(direction)

        // ⚡ TODO: integrate martingale preparation + trade result detection
    }

    /** Forward key events to executor if needed */
    fun onKeyEvent(event: android.view.KeyEvent?): Boolean {
        return actionExecutor.onKeyEvent(event)
    }
}

private const val TAG = "LocalService"
)
