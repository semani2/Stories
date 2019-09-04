package com.se.stories

import android.os.Bundle
import androidx.test.runner.AndroidJUnitRunner
import com.linkedin.android.testbutler.TestButler

class StoriesTestRunner : AndroidJUnitRunner() {
    override fun onStart() {
        TestButler.setup(targetContext)
        super.onStart()
    }

    override fun finish(resultCode: Int, results: Bundle) {
        TestButler.teardown(targetContext)
        super.finish(resultCode, results)
    }
}
