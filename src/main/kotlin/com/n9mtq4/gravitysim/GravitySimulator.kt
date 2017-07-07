package com.n9mtq4.gravitysim

import javax.swing.JFrame

/**
 * Created by will on 7/7/2017 at 12:07 AM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
fun main(args: Array<String>) {
	
	Game()
	
}

class Game {
	
	private val frame: JFrame
	private val gameClass: GameClass
	
	init {
		
		frame = JFrame("Will's Gravity Simulator")
		frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
		
		gameClass = GameClass()
		
		frame.add(gameClass)
		
		frame.isResizable = false
		frame.isVisible = true
		frame.pack()
		
		gameClass.start()
		
	}
	
}
