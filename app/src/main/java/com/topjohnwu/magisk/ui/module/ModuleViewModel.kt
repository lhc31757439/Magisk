package com.topjohnwu.magisk.ui.module

import androidx.databinding.Bindable
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.viewModelScope
import com.topjohnwu.magisk.BR
import com.topjohnwu.magisk.R
import com.topjohnwu.magisk.arch.BaseViewModel
import com.topjohnwu.magisk.core.Info
import com.topjohnwu.magisk.core.model.module.LocalModule
import com.topjohnwu.magisk.core.model.module.OnlineModule
import com.topjohnwu.magisk.databinding.*
import com.topjohnwu.magisk.events.OpenReadmeEvent
import com.topjohnwu.magisk.events.SelectModuleEvent
import com.topjohnwu.magisk.events.SnackbarEvent
import com.topjohnwu.magisk.events.dialog.ModuleInstallDialog
import com.topjohnwu.magisk.ktx.addOnListChangedCallback
import com.topjohnwu.magisk.ktx.reboot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.tatarka.bindingcollectionadapter2.collections.MergeObservableList

class ModuleViewModel : BaseViewModel() {

    val bottomBarBarrierIds = intArrayOf(R.id.module_info, R.id.module_remove)

    @get:Bindable
    var isRemoteLoading = false
        set(value) = set(value, field, { field = it }, BR.remoteLoading)

    private val installSectionList = ObservableArrayList<RvItem>()
    private val itemsInstalled = diffListOf<LocalModuleRvItem>()
    private val sectionInstalled = SectionTitle(
        R.string.module_installed,
        R.string.reboot,
        R.drawable.ic_restart
    ).also { it.hasButton = false }

    val adapter = adapterOf<RvItem>()
    val items = MergeObservableList<RvItem>()
    val itemBinding = itemBindingOf<RvItem> {
        it.bindExtra(BR.viewModel, this)
    }

    init {
        itemsInstalled.addOnListChangedCallback(
            onItemRangeInserted = { _, _, _ ->
                if (installSectionList.isEmpty())
                    installSectionList.add(sectionInstalled)
            },
            onItemRangeRemoved = { list, _, _ ->
                if (list.isEmpty())
                    installSectionList.clear()
            }
        )

        if (Info.env.isActive) {
            items.insertItem(InstallModule)
                .insertList(installSectionList)
                .insertList(itemsInstalled)
        }
    }

    override fun refresh(): Job {
        return viewModelScope.launch {
            state = State.LOADING
            loadInstalled()
            state = State.LOADED
        }
    }

    private suspend fun loadInstalled() {
        val installed = LocalModule.installed().map { LocalModuleRvItem(it) }
        val diff = withContext(Dispatchers.Default) {
            itemsInstalled.calculateDiff(installed)
        }
        itemsInstalled.update(installed, diff)
    }

    fun forceRefresh() {
        itemsInstalled.clear()
        refresh()
    }

    fun updateActiveState() {
        sectionInstalled.hasButton = itemsInstalled.any { it.isModified }
    }

    fun sectionPressed(item: SectionTitle) = when (item) {
        sectionInstalled -> reboot()
        else -> Unit
    }

    // The following methods are not used, but kept for future integration

    fun downloadPressed(item: OnlineModule) =
        if (isConnected.get()) withExternalRW { ModuleInstallDialog(item).publish() }
        else { SnackbarEvent(R.string.no_connection).publish() }

    fun installPressed() = withExternalRW { SelectModuleEvent().publish() }

    fun infoPressed(item: OnlineModule) =
        if (isConnected.get()) OpenReadmeEvent(item).publish()
        else SnackbarEvent(R.string.no_connection).publish()

    fun infoPressed(item: LocalModuleRvItem) { infoPressed(item.online ?: return) }
}
