package com.buzbuz.smartautoclicker.localservice

import android.util.Log

object LocalServiceProvider {

    /** The instance of the [ILocalService], providing access for this service to the Activity. */
    var localServiceInstance: ILocalService? = null
        private set(value) {
            field = value
            localServiceCallback?.invoke(field)
        }

    /** Callback upon the availability of the [localServiceInstance]. */
    private var localServiceCallback: ((ILocalService?) -> Unit)? = null
        set(value) {
            field = value
            value?.invoke(localServiceInstance)
        }

    fun setLocalService(service: ILocalService?) {
        localServiceInstance = service
    }

    /**
     * Static method allowing an activity to register a callback in order to monitor the availability of the
     * [ILocalService]. If the service is already available upon registration, the callback will be immediately
     * called.
     *
     * @param stateCallback the object to be notified upon service availability.
     */
    fun getLocalService(stateCallback: ((ILocalService?) -> Unit)?) {
        localServiceCallback = stateCallback
    }

    fun isServiceStarted(): Boolean = localServiceInstance != null

    /**
     * Trigger a Logic Key action from the UI.
     *
     * @param key the logic key assigned to a button.
     */
    fun triggerLogicKey(key: String) {
        val service = localServiceInstance
        if (service == null) {
            Log.w(TAG, "No LocalService attached, cannot trigger key: $key")
            return
        }

        if (service is LocalService) {
            service.executeLogicKey(key)
        } else {
            Log.w(TAG, "Attached service is not LocalService, cannot execute logic key: $key")
        }
    }
}

private const val TAG = "LocalServiceProvider"
