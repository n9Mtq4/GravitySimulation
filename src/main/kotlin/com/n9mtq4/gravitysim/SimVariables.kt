package com.n9mtq4.gravitysim

import java.util.*

/**
 * Created by will on 7/7/2017 at 12:09 AM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */

val RANDOM = Random()

const val THREADS = 2 // TODO: dynamically get this based off current cpu
const val WINDOW_WIDTH = 1000
const val WINDOW_HEIGHT = 1000
const val WINDOW_SCALE = 1

const val DEBUG = true
const val PRINT_FPS = false
const val PRINT_CYCLE_TIMES = false
const val ANTIALIASING = true
const val FPS_CAP = true
const val TICKS_PER_SECOND = 60.0

const val NUM_PARTICLES = 1e3.toInt()
