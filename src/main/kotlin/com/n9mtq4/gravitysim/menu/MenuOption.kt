package com.n9mtq4.gravitysim.menu

import com.n9mtq4.gravitysim.getHeight
import com.n9mtq4.gravitysim.getWidth
import java.awt.Font
import java.awt.Rectangle
import java.awt.font.FontRenderContext

/**
 * Created by will on 5/29/17 at 8:48 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
class MenuOption(var optionName: String, var description: String = "", val callback: () -> Unit = {}) {
	
	var renderBounds: Rectangle? = null
	
	fun calcRenderBounds(xOff: Int, yOff: Int, frc: FontRenderContext, font: Font) {
		
		this.renderBounds = Rectangle(xOff, yOff, font.getWidth(optionName, frc), font.getHeight(optionName, frc))
		
	}
	
}
