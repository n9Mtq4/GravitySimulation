package com.n9mtq4.gravitysim.menu.menus

import com.n9mtq4.gravitysim.WINDOW_HEIGHT
import com.n9mtq4.gravitysim.WINDOW_SCALE
import com.n9mtq4.gravitysim.getHeight
import com.n9mtq4.gravitysim.menu.Menu
import com.n9mtq4.gravitysim.menu.MenuManager
import com.n9mtq4.gravitysim.menu.MenuOption
import java.awt.Graphics
import java.awt.event.KeyEvent
import java.awt.event.KeyEvent.*
import java.awt.event.MouseEvent

/**
 * Created by will on 5/29/17 at 7:54 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
class MainMenu(menuManager: MenuManager) : Menu(menuManager) {
	
	companion object {
		const val GAME_TITLE = "Will's Gravity Game"
		const val FONT_SPACING = 5 * WINDOW_SCALE
		const val CHOICE_YOFFSET = 150 * WINDOW_SCALE
	}
	
	var updateOptionBounds = true
	
	var selectedIndex = 0
	
	val options = listOf(
			MenuOption("Play Game", "Starts the game") {  }
	)
	
	override fun draw(g: Graphics) {
		super.draw(g)
		
		g.clearAll()
		
		// title
		g.font = TITLE_FONT
		val x = calcCenter(GAME_TITLE, g.font, g.frc)
		g.drawString(GAME_TITLE, x, g.font.getHeight(GAME_TITLE, g.frc) + 20)
		
		// options
		options.forEachIndexed { index, option ->
			
			val text = option.optionName
			
			g.font = if (selectedIndex == index) SELECTED_OPTION_FONT else OPTION_FONT
			
			val fontCenterX = calcCenter(text, g.font, g.frc)
			val fontHeight = g.font.getHeight(text, g.frc)
			val fontYPos = (fontHeight + FONT_SPACING) * index + CHOICE_YOFFSET
			
			if (updateOptionBounds) option.calcRenderBounds(fontCenterX, fontYPos - fontHeight, g.frc, g.font)
			
			g.drawString(text, fontCenterX, fontYPos)
			
		}
		
		if (updateOptionBounds) updateOptionBounds = false
		
		// show description
		g.font = OPTION_FONT
		val selectedOption = options[selectedIndex]
		val selectedOptionText = selectedOption.description
		g.drawString(selectedOptionText, 0, WINDOW_HEIGHT * WINDOW_SCALE - FONT_SPACING)
		
	}
	
	override fun keyPressed(e: KeyEvent?) {
		// no super, since shouldn't be able to pop the main menu
		when (e?.keyCode) {
			VK_DOWN, VK_S -> {
				selectedIndex++
				if (selectedIndex <= -1) selectedIndex = options.size - 1
				selectedIndex %= options.size
			}
			VK_UP, VK_W -> {
				selectedIndex--
				if (selectedIndex <= -1) selectedIndex = options.size - 1
				selectedIndex %= options.size
			}
			VK_ENTER, VK_E -> runMenuOption()
		}
	}
	
	private fun runMenuOption() = options[selectedIndex].callback()
	
	/**
	 * Search the option list to find a possible menu option that the mouse is hovering over.
	 * */
	private fun getSelectedOptionFromMouse(x: Int, y: Int) = options.filter { it.renderBounds?.contains(x, y) ?: false }.firstOrNull()
	
	private fun setSelectedOptionFromMouse(x: Int, y: Int) {
		getSelectedOptionFromMouse(x, y)?.let { this.selectedIndex = options.indexOf(it) }
	}
	
	override fun mouseClicked(e: MouseEvent?) {
		
		super.mouseClicked(e)
		
		e?.let { event ->
			getSelectedOptionFromMouse(event.x, event.y)?.let { _ ->
				// only run the menu option if the mouse is clicked over a menu option
				runMenuOption()
			}
		}
		
	}
	
	override fun mouseMoved(e: MouseEvent?) {
		
		super.mouseMoved(e)
		
		e?.let {
			setSelectedOptionFromMouse(it.x, it.y)
		}
		
	}
	
}
