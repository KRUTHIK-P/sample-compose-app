package com.example.samplecomposeapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/*
@HiltAndroidApp annotation tells Hilt to generate the necessary components to
handle dependency injection for the whole app.
 */
@HiltAndroidApp
class App : Application()