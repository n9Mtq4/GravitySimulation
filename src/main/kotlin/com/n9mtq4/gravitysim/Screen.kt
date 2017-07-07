package com.n9mtq4.gravitysim

/**
 * Created by will on 7/7/2017 at 12:35 AM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
class Screen(val width: Int, val height: Int) {
	
	val pixels = IntArray(width * height)
	
	fun clear() {
		for (i in 0..pixels.size - 1) {
			pixels[i] = 0x000000 //black
		}
	}
	
}
