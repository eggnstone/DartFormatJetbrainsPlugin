package dev.eggnstone.plugins.jetbrains.dartformat.config

import dev.eggnstone.plugins.jetbrains.dartformat.tools.Logger

class DartFormatConfigGetter
{
    companion object
    {
        fun get(): DartFormatConfigV2
        {
            Logger.logDebug("DartFormatConfigGetter.get()")

            assert(DartFormatPersistentStateComponentV1.instance != null)
            assert(DartFormatPersistentStateComponentV2.instance != null)

            Logger.logDebug("DartFormatPersistentStateComponentV1.instance!!.state.getLoadedTransient() " + DartFormatPersistentStateComponentV1.instance!!.state.getLoadedTransient())
            Logger.logDebug("DartFormatPersistentStateComponentV2.instance!!.state.getLoadedTransient() " + DartFormatPersistentStateComponentV2.instance!!.state.getLoadedTransient())

            if (DartFormatPersistentStateComponentV2.instance!!.state.getLoadedTransient() == true)
            {
                Logger.logDebug("DartFormatConfigGetter.get returning existing V2")
                return DartFormatPersistentStateComponentV2.instance!!.state
            }

            if (DartFormatPersistentStateComponentV1.instance!!.state.getLoadedTransient() == true)
            {
                Logger.logDebug("DartFormatConfigGetter.get returning existing V1")
                val oldConfig = DartFormatPersistentStateComponentV1.instance!!.state
                val config = DartFormatConfigUpdater.updateV1toV2(oldConfig)
                Logger.logDebug("Updated config from V1 to V2.")

                // Reset DartFormatPersistentStateComponentV1 so that it is saved empty / removed.
                DartFormatPersistentStateComponentV1.instance!!.clearState()

                DartFormatPersistentStateComponentV2.instance!!.loadState(config)
                return DartFormatPersistentStateComponentV2.instance!!.state
            }

            Logger.logDebug("DartFormatConfigGetter.get returning new V2")
            DartFormatPersistentStateComponentV2.instance!!.loadState(DartFormatConfigV2())
            return DartFormatPersistentStateComponentV2.instance!!.state
        }
    }
}
