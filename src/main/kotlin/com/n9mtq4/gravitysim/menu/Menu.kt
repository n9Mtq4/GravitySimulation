package com.n9mtq4.gravitysim.menu

/**
 * Created by will on 7/7/2017 at 12:14 AM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
import com.n9mtq4.gravitysim.Screen
import com.n9mtq4.gravitysim.WINDOW_HEIGHT
import com.n9mtq4.gravitysim.WINDOW_SCALE
import com.n9mtq4.gravitysim.WINDOW_WIDTH
import com.n9mtq4.kotlin.extlib.ignore
import java.awt.Font
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.event.*
import java.awt.font.FontRenderContext

/**
 * Created by will on 5/29/17 at 7:01 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
open class Menu(val menuManager: MenuManager) : KeyListener, MouseListener, MouseMotionListener {
	
	companion object {
		val TITLE_FONT = Font(Font.SANS_SERIF, Font.BOLD, 40 * WINDOW_SCALE)
		val OPTION_FONT = Font(Font.SANS_SERIF, Font.PLAIN, 25 * WINDOW_SCALE)
		val SELECTED_OPTION_FONT = Font(Font.SANS_SERIF, Font.BOLD, 25 * WINDOW_SCALE)
	}
	
	open fun pixelDraw(screen: Screen) {
		
	}
	
	open fun draw(g: Graphics) {
		g.clearAll()
	}
	
	open fun tick() {
		
	}
	
	open fun onPop() {
		
	}
	
	open fun onPush() {
		
	}
	
	fun push() {
		menuManager.pushMenu(this)
	}
	
	fun pop() {
		menuManager.popMenu(this)
	}
	
	protected fun calcCenter(text: String, font: Font, fontRenderContext: FontRenderContext): Int {
		
		val width = font.createGlyphVector(fontRenderContext, text).visualBounds.width.toInt()
		val centerPos = WINDOW_WIDTH * WINDOW_SCALE
		
		return (centerPos - width) / 2
		
	}
	
	override fun keyTyped(e: KeyEvent?) {}
	override fun keyPressed(e: KeyEvent?) {
		ignore { if (e?.keyCode == KeyEvent.VK_ESCAPE) menuManager.popMenu(this) } // default will pop menu with esc
	}
	override fun keyReleased(e: KeyEvent?) {}
	
	override fun mouseReleased(e: MouseEvent?) {}
	override fun mouseEntered(e: MouseEvent?) {}
	override fun mouseClicked(e: MouseEvent?) {}
	override fun mouseExited(e: MouseEvent?) {}
	override fun mousePressed(e: MouseEvent?) {}
	
	override fun mouseMoved(e: MouseEvent?) {}
	override fun mouseDragged(e: MouseEvent?) {}
	
	fun Graphics.clearAll() {
		this.clearRect(0, 0, WINDOW_WIDTH * WINDOW_SCALE, WINDOW_HEIGHT * WINDOW_SCALE)
	}
	val Graphics.frc
		get() = (this as Graphics2D).fontRenderContext
	
}
