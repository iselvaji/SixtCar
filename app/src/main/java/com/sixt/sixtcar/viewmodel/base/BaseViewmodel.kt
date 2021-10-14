package com.sixt.sixtcar.viewmodel.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.sixt.sixtcar.model.repository.BaseRepo

open class BaseViewModel<T : BaseRepo>(application: Application, private val repo: T) : AndroidViewModel(
    application
) {
    val isLoading = repo.isLoading
    val error = repo.error

    protected fun getRepository(): T {
        return repo
    }
}