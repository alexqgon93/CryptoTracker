package com.cryptotracker

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Process entry point for CryptoTracker.
 *
 * It carries no logic of its own: the annotation makes Hilt generate the
 * application-level dependency graph that the rest of the app injects from.
 */
@HiltAndroidApp
class CryptoTrackerApplication : Application()
