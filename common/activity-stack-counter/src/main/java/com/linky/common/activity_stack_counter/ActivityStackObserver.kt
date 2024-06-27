package com.linky.common.activity_stack_counter

import android.app.Activity
import android.app.Application
import android.os.Bundle
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ActivityStackObserver @Inject constructor() : Application.ActivityLifecycleCallbacks {

    @Volatile
    var count = 0
        private set

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        count++
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityResumed(activity: Activity) {
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
        count--
    }

}