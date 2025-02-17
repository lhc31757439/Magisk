package com.topjohnwu.magisk.ui.install

import android.content.res.Resources
import android.net.Uri
import android.os.Build
import androidx.databinding.Bindable
import androidx.lifecycle.viewModelScope
import com.topjohnwu.magisk.BR
import com.topjohnwu.magisk.R
import com.topjohnwu.magisk.arch.BaseViewModel
import com.topjohnwu.magisk.core.Info
import com.topjohnwu.magisk.databinding.set
import com.topjohnwu.magisk.events.MagiskInstallFileEvent
import com.topjohnwu.magisk.events.dialog.SecondSlotWarningDialog
import com.topjohnwu.magisk.ui.flash.FlashFragment
import com.topjohnwu.superuser.Shell
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.IOException

class InstallViewModel(
    private val resources: Resources
) : BaseViewModel() {

    val isRooted = Shell.rootAccess()
    val skipOptions = Info.ramdisk && !Info.isFDE && Info.isSAR && !(!Info.vbmeta && Build.VERSION.SDK_INT >= 30)
    val noSecondSlot = !isRooted || !Info.isAB || Info.isEmulator

    @get:Bindable
    var step = if (Info.isEmulator || skipOptions) 1 else 0
        set(value) = set(value, field, { field = it }, BR.step)

    var _method = -1

    @get:Bindable
    var method
        get() = _method
        set(value) = set(value, _method, { _method = it }, BR.method) {
            when (it) {
                R.id.method_patch -> {
                    MagiskInstallFileEvent { uri -> data = uri }.publish()
                }
                R.id.method_inactive_slot -> {
                    SecondSlotWarningDialog().publish()
                }
            }
        }

    @get:Bindable
    var data: Uri? = null
        set(value) = set(value, field, { field = it }, BR.data)

    @get:Bindable
    var notes = ""
        set(value) = set(value, field, { field = it }, BR.notes)

    init {
        viewModelScope.launch {
            try {
                notes = resources.openRawResource(R.raw.changelog)
                    .bufferedReader().use { it.readText() }
            } catch (e: IOException) {
                Timber.e(e)
            }
        }
    }

    fun step(nextStep: Int) {
        step = nextStep
    }

    fun install() {
        when (method) {
            R.id.method_patch -> FlashFragment.patch(data!!).navigate()
            R.id.method_direct -> FlashFragment.flash(false).navigate()
            R.id.method_inactive_slot -> FlashFragment.flash(true).navigate()
            else -> error("Unknown value")
        }
        state = State.LOADING
    }
}
