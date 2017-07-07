package com.n9mtq4.gravitysim

import java.awt.Font
import java.awt.font.FontRenderContext

/**
 * Created by will on 7/7/2017 at 12:17 AM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
fun Font.getWidth(str: String, fontRenderContext: FontRenderContext) =
		createGlyphVector(fontRenderContext, str).visualBounds.width.toInt()
fun Font.getHeight(str: String, fontRenderContext: FontRenderContext) =
		createGlyphVector(fontRenderContext, str).visualBounds.height.toInt()
