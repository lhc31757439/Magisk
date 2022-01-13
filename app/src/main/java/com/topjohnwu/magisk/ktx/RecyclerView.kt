package com.topjohnwu.magisk.ktx

import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.addInvalidateItemDecorationsObserver() {

    adapter?.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            invalidateItemDecorations()
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            invalidateItemDecorations()
        }

        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
            invalidateItemDecorations()
        }
    })
}
