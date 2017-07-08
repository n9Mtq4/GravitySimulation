package com.n9mtq4.gravitysim

import com.n9mtq4.gravitysim.menu.MenuManager
import java.awt.*
import java.awt.Font.BOLD
import java.awt.image.BufferedImage
import java.awt.image.DataBufferInt


/**
 * Created by will on 5/25/17 at 9:48 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
class GameClass : Canvas(), Runnable {
	
	var running = false
	var fps = 0
	lateinit var thread: Thread
	
	private val image = BufferedImage(WINDOW_WIDTH, WINDOW_HEIGHT, BufferedImage.TYPE_INT_RGB)
	private val pixels = (image.raster.dataBuffer as DataBufferInt).data
	
	val screen: Screen
	
	var menuManager = MenuManager(this)
	
	init {
		
		preferredSize = Dimension(WINDOW_WIDTH * WINDOW_SCALE, WINDOW_HEIGHT * WINDOW_SCALE)
		
		this.screen = Screen(WINDOW_WIDTH * WINDOW_SCALE, WINDOW_HEIGHT * WINDOW_SCALE)
		
		addKeyListener(menuManager)
		addMouseListener(menuManager)
		addMouseMotionListener(menuManager)
		
	}
	
	fun start() {
		if (running) return
		this.running = true
		thread = Thread(this, "Game Tick and Render Thread")
		thread.start()
	}
	
	fun stop() {
		if (!running) return
		this.running = false
		thread.join()
	}
	
	fun render() {
		
		val bs = this.bufferStrategy
		if (bs == null) {
			createBufferStrategy(3)
			return
		}
		val g = bs.drawGraphics
		
		if (ANTIALIASING) {
			g as Graphics2D
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
			g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE)
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC)
			g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)
		}
		
		// render to the screen
		menuManager.pixelDraw(screen)
		
		// render the screen to the graphics
		screen.clear()
		for (i in 0..pixels.size - 1) {
			pixels[i] = screen.pixels[i]
		}
		g.drawImage(image, 0, 0, width, height, null)
		
		// render to the graphics
		menuManager.draw(g)
		
		if (DEBUG) {
			g.color = Color.RED
			g.font = Font("Verdana", BOLD, 24)
			g.drawString("$fps fps", 0, 20)
			g.font = Font("Verdana", Font.BOLD, 12)
		}
		
		// render the graphics to the actual computer screen
		g.dispose()
		bs.show()
		
	}
	
	fun tick() {
		
		menuManager.tick()
		
	}
	
	override fun run() {
		
		var frames = 0
		var unprocessedSeconds = 0.0
		var previousTime = System.nanoTime()
		val clockSpeed = 1 / TICKS_PER_SECOND
		var tickCount = 0
		var ticked = false
		
		requestFocus()
		requestFocusInWindow()
		tick()
		
		while (running) {
			
//			game loop
			val currentTime = System.nanoTime()
			val passedTime = currentTime - previousTime
			previousTime = currentTime
			unprocessedSeconds += passedTime / 1000000000.0
			var ticksInCycle = 0
			
			while (unprocessedSeconds > clockSpeed) {
				
				ticksInCycle++
				
				tick()
				
				unprocessedSeconds -= clockSpeed
				ticked = true
				tickCount++
				if (tickCount % TICKS_PER_SECOND.toInt() == 0) {
					
					if (PRINT_FPS) println("$tickCount ups, $frames fps")
					previousTime = System.nanoTime()
					fps = frames
					frames = 0
					tickCount = 0
					
				}
				
			}
			
			// if (ticksInCycle > 1) println("More ticks: $ticksInCycle")
			
			if (ticked) {
				
				render()
				frames++
				ticked = false
				
			}
			
			if (!FPS_CAP) {
				render()
				frames++
			}else {
				// sleep thread for required time, based off how long the last cycle took
				val sleepTime = (clockSpeed * 1000 - 1.0 * (passedTime / 1000000.0)).toLong()
				if (sleepTime > 0) Thread.sleep(sleepTime) // if sleepTime is negative, we are behind and must catch up
				// else if (sleepTime < 0) println("Too low: $sleepTime")
			}
			
		}
		
	}
	
}
