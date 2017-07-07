package com.n9mtq4.gravitysim

import com.n9mtq4.kotlin.extlib.math.minValueOf
import com.n9mtq4.kotlin.extlib.math.pow
import java.util.*

/**
 * Created by will on 7/6/2017 at 9:39 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
//const val BIG_G = 6.674e-11
const val BIG_G = 1e-4

/**
 * Gravity formula
 * */
fun calcForce(m1: Double, m2: Double, r: Double) = BIG_G * m1 * m2 / r / r

fun processCycle(threadPool: ThreadPool, bodies: ArrayList<Body>) {
	
	val toRemove = ArrayList<Body>()
	
	for (i in 1..bodies.size - 1) {
		
		threadPool.execute {
			
			for (i1 in 0..i - 1) {
				
				if (updateBodies(bodies[i], bodies[i1])) toRemove.add(bodies[i1])
				
			}
			
		}
		
	}
	
	bodies.removeAll(toRemove)
	
	// global tick
	bodies.forEach {
		it.x += it.vx
		it.y += it.vy
	}
	
}

/**
 * Processes the simulation for the two particles
 * returns true if b2 should be removed after running
 * */
fun updateBodies(b1: Body, b2: Body): Boolean {
	
	// get distance
	val distance = b1.distanceTo(b2)
	
	// check to see if they collided, join them if they did
	if (distance < (b1.radius + b2.radius - 2) minValueOf 1) { // 2 is just a pixel offset of a collision
		// return true if it should remove b2 from the list at the end of this cycle
		b1.mass += b2.mass
		return true
	}
	
	// attraction force between them
	val force = calcForce(b1.mass, b2.mass, distance)
	
	// calculate force vector components
	// TODO: find way to simplify. May be similar triangles, so just compare ratios?
	val xd = b2.x - b1.x
	val yd = b2.y - b1.y
	
	val angle = Math.atan2(yd, xd)
	
	val fx = force * Math.cos(angle)
	val fy = force * Math.sin(angle)
	
	// calculate acceleration
	val ax1 = fx / b1.mass
	val ay1 = fy / b1.mass
	val ax2 = fx / b2.mass
	val ay2 = fy / b2.mass
	
	// update velocities
	b1.vx += ax1
	b1.vy += ay1
	b2.vx += ax2
	b2.vy += ay2
	
	return false
	
}


class Body(var mass: Double = RANDOM.nextDouble() * 1000) {
	
	var x: Double = RANDOM.nextDouble() * 100
	var y: Double = RANDOM.nextDouble() * 100
	var vx: Double = 0.0
	var vy: Double = 0.0
	
}
fun Body.distanceTo(other: Body) = Math.sqrt((this.x - other.x).pow(2) + (this.y - other.y).pow(2))
val Body.radius: Int
	get() = (Math.log(mass).toInt() / 10) minValueOf 1
