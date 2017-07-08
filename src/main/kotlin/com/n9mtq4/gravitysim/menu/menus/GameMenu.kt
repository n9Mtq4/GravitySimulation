package com.n9mtq4.gravitysim.menu.menus

import com.n9mtq4.gravitysim.*
import com.n9mtq4.gravitysim.menu.Menu
import com.n9mtq4.gravitysim.menu.MenuManager
import java.awt.Color
import java.awt.Graphics
import java.util.*

/**
 * Created by will on 6/6/17 at 8:32 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
class GameMenu(menuManager: MenuManager) : Menu(menuManager) {
	
	val threadPool = ThreadPool(THREADS)
	var bodies = Collections.synchronizedList(ArrayList<Body>())
	
	var renderBodies = ArrayList<Body>()
	
	private val tickSingleThreadPool = ThreadPool(1)
	
	var ticking = false
	
	override fun onPush() {
		
		super.onPush()
		
		bodies.clear()
		
		for (i in 1..NUM_PARTICLES) {
			bodies.add(Body())
		}
		
		bodies.add(Body(1000.0).apply { x = 500.0; y = 500.0; vx = 0.0; vy = 0.0 })
		
		threadPool.start()
		tickSingleThreadPool.start()
		
	}
	
	override fun pixelDraw(screen: Screen) {
		
		
	}
	
	override fun draw(g: Graphics) {
		
		super.draw(g)
		
		g.color = Color.BLACK
		
		renderBodies.forEach { 
			val radius = it.radius
			// the corner value of the circle
			val cx = it.x - radius
			val cy = it.y - radius
			g.fillOval(cx.toInt(), cy.toInt(), radius * 2, radius * 2)
		}
		
	}
	
	override fun tick() {
		
		if (ticking) return
		
		tickSingleThreadPool.execute {
			ticking = true
			val before = System.currentTimeMillis()
			processCycle(threadPool, bodies)
//			processCycleSingleThread(threadPool, bodies)
			val after = System.currentTimeMillis()
			if (PRINT_CYCLE_TIMES) println((after - before) / 1000.0)
			ticking = false
		}
//		tickSingleThreadPool.execute { ticking = true; processCycleSingleThread(threadPool, bodies); ticking = false }
		
		renderBodies.clear()
		bodies.forEach { renderBodies.add(it) }
		
	}
	
}
