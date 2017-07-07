package com.n9mtq4.gravitysim.menu.menus

import com.n9mtq4.gravitysim.*
import com.n9mtq4.gravitysim.menu.Menu
import com.n9mtq4.gravitysim.menu.MenuManager
import java.awt.Graphics

/**
 * Created by will on 6/6/17 at 8:32 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
class GameMenu(menuManager: MenuManager) : Menu(menuManager) {
	
	val threadPool = ThreadPool(THREADS)
	val bodies = ArrayList<Body>()
	
	override fun onPush() {
		
		super.onPush()
		
		bodies.clear()
		
		for (i in 1..NUM_PARTICLES) {
			bodies.add(Body())
		}
		
		threadPool.start()
		
	}
	
	override fun draw(g: Graphics) {
		
		super.draw(g)
		
		g.drawString("Test", 80, 80)
		
	}
	
	override fun tick() {
		
		processCycle(threadPool, bodies)
		
	}
	
}
