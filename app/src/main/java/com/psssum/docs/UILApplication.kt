package com.psssum.share

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.psssum.market.utils.L
import com.vk.api.sdk.utils.VKUtils.getCertificateFingerprint


class UILApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val fingerprints =
            getCertificateFingerprint(this, this.packageName)
        L.d("fingerprint = " + (fingerprints?.get(0) ?: ""))

    }
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }


}