package com.n9mtq4.gravitysim.menu

import com.n9mtq4.gravitysim.GameClass
import com.n9mtq4.gravitysim.Screen
import com.n9mtq4.gravitysim.menu.menus.GameMenu
import java.awt.Graphics
import java.awt.event.*
import java.lang.IllegalStateException
import java.util.*

/**
 * Created by will on 7/7/2017 at 12:13 AM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
class MenuManager(val gameClass: GameClass) : KeyListener, MouseListener, MouseMotionListener {
	
	private val menuStack: Stack<Menu> = Stack()
	
	var gameMenu = GameMenu(this)
	
	init {
//		menuStack.push(MainMenu(this))
		menuStack.push(gameMenu)
	}
	
	fun pixelDraw(screen: Screen) {
		menuStack.peek().pixelDraw(screen)
	}
	
	fun draw(g: Graphics) {
		menuStack.peek().draw(g)
	}
	
	fun tick() {
		menuStack.peek().tick()
	}
	
	fun pushMenu(menu: Menu) {
		menuStack.push(menu)
		menu.onPush()
	}
	
	@Throws(IllegalStateException::class)
	fun popMenu(menu: Menu) {
		if (menuStack.peek() != menu) throw IllegalStateException("Can't pop a menu that isn't yourself!")
		if (menuStack.size == 1) throw IllegalStateException("Can't pop the last menu!")
		menuStack.pop().onPop()
	}
	
	override fun keyTyped(e: KeyEvent?) = menuStack.peek().keyTyped(e)
	override fun keyPressed(e: KeyEvent?) = menuStack.peek().keyPressed(e)
	override fun keyReleased(e: KeyEvent?) = menuStack.peek().keyReleased(e)
	
	override fun mouseReleased(e: MouseEvent?) = menuStack.peek().mouseReleased(e)
	override fun mouseEntered(e: MouseEvent?) = menuStack.peek().mouseEntered(e)
	override fun mouseClicked(e: MouseEvent?) = menuStack.peek().mouseClicked(e)
	override fun mouseExited(e: MouseEvent?) = menuStack.peek().mouseExited(e)
	override fun mousePressed(e: MouseEvent?) = menuStack.peek().mousePressed(e)
	
	override fun mouseMoved(e: MouseEvent?) = menuStack.peek().mouseMoved(e)
	override fun mouseDragged(e: MouseEvent?) = menuStack.peek().mouseDragged(e)
	
}
