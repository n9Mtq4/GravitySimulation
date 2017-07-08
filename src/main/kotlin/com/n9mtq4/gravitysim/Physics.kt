package com.n9mtq4.gravitysim

import com.n9mtq4.kotlin.extlib.ignore
import com.n9mtq4.kotlin.extlib.math.minValueOf
import com.n9mtq4.kotlin.extlib.math.pow
import java.util.*

/**
 * Created by will on 7/6/2017 at 9:39 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
//const val BIG_G = 6.674e-11
const val BIG_G = 0.01

/**
 * Gravity formula
 * */
fun calcForce(m1: Double, m2: Double, r: Double) = BIG_G * m1 * m2 / (r * r)

fun processCycle(threadPool: ThreadPool, bodies: MutableList<Body>) {
	
	val toRemove = Collections.synchronizedList(ArrayList<Body>())
	
/*	for (t in 0..threadPool.numThreads - 1) {
		
		threadPool.execute { worker ->
			
			// kotlin's for loops with steps are too slow
			var i = 0
			while (i < bodies.size - 1) {
				
//				if (worker.poolId == 0) print("$i,")
				
				for (i1 in 0..i - 1) {
					
					ignore {
						if (updateBodies(bodies[i], bodies[i1])) {
							toRemove.add(bodies[i1])
						}
					}
					
				}
				
				i += threadPool.numThreads
				
			}
			
//			if (worker.poolId == 0) println("DONE")
			
		}
		
	}*/
	for (i in 1..bodies.size - 1) {
		
		threadPool.execute {
			
			for (i1 in 0..i - 1) { // TODO: changed to 2 cause of bug should be 1 find the bug
				
				ignore {
					if (updateBodies(bodies[i], bodies[i1])) {
						toRemove.add(bodies[i1])
					}
				}
				
			}
			
		}
		
	}
	
	while (!threadPool.isDone()) {
		Thread.sleep(1)
	}
	
	bodies.removeAll(toRemove)
	
	// global tick
	bodies.forEach {
		it.x += it.vx
		it.y += it.vy
	}
	
}

fun processCycleSingleThread(threadPool: ThreadPool, bodies: MutableList<Body>) {
	
	val toRemove = ArrayList<Body>()
	
	for (i in 1..bodies.size - 1) {
		
		for (i1 in 0..i - 1) { // TODO: changed to 2 cause of bug should be 1 find the bug
			
			ignore {
				if (updateBodies(bodies[i], bodies[i1])) {
					toRemove.add(bodies[i1])
				}
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
	
	val xd = b2.x - b1.x
	val yd = b2.y - b1.y
	
	// check to see if they collided, join them if they did
	if (distance < (b1.radius + b2.radius) + 2) { // 2 is just a pixel offset of a collision
		// return true if it should remove b2 from the list at the end of this cycle
		b1.mass += b2.mass
		val massRatio = (b2.mass / b1.mass)
		b1.vx += (b2.vx - b1.vx) * massRatio
		b1.vy += (b2.vy - b1.vy) * massRatio
		b1.x += xd * massRatio
		b1.y += yd * massRatio
		return true
	}
	
	// attraction force between them
	val force = calcForce(b1.mass, b2.mass, distance)
	
	// calculate force vector components
	// TODO: find way to simplify. May be similar triangles, so just compare ratios?
	
	val angle = Math.atan2(yd, xd)
	
	val fx = force * Math.cos(angle)
	val fy = force * Math.sin(angle)
	
	// calculate acceleration
	val ax1 = fx / b1.mass
	val ay1 = fy / b1.mass
	val ax2 = -fx / b2.mass
	val ay2 = -fy / b2.mass
	
	// update velocities
	b1.vx += ax1
	b1.vy += ay1
	b2.vx += ax2
	b2.vy += ay2
	
	return false
	
}


class Body(var mass: Double = RANDOM.nextDouble() * 20.0) {
	
	var x: Double = 50 + RANDOM.nextDouble() * 900
	var y: Double = 50 + RANDOM.nextDouble() * 900
	var vx: Double = (RANDOM.nextDouble() - 0.5) * 5.0
	var vy: Double = (RANDOM.nextDouble() - 0.5) * 5.0
	
	init {
		
		val dx = x - WINDOW_WIDTH / 2
		val dy = y - WINDOW_HEIGHT / 2
		val vAngle = Math.atan2(dy, dx) + Math.PI / 2.0
		val vMag = RANDOM.nextDouble() * Math.sqrt(Math.sqrt(dx * dx + dy * dy)) * 0.04
		vx = vMag * Math.cos(vAngle)
		vy = vMag * Math.sin(vAngle)
		
	}
	
}
fun Body.distanceTo(other: Body) = Math.sqrt((this.x - other.x).pow(2) + (this.y - other.y).pow(2))
val Body.radius: Int
	get() = (Math.log(mass).toInt()) minValueOf 1
