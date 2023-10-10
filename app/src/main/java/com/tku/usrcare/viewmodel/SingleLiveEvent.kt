package com.tku.usrcare.viewmodel

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

class SingleLiveEvent<T> : MutableLiveData<T>() {
    private val pending = MutableLiveData<Boolean>()
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner, Observer<T> { t ->
            if (pending.value == true) {
                pending.value = false
                observer.onChanged(t)
            }
        })
    }
    override fun setValue(value: T?) {
        pending.value = true
        super.setValue(value)
    }
}