package com.n9mtq4.gravitysim

import java.util.*

/**
 * Created by will on 7/6/2017 at 9:48 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */

typealias Task = () -> Unit
typealias TaskList = ArrayDeque<Task>

class ThreadPool(val numThreads: Int) {
	
	val threadList: Array<Worker> = Array(numThreads) { i -> Worker(i) }
	var nextThread = 0
	
	fun execute(task: Task) {
		threadList[nextThread++].addTask(task)
		nextThread %= numThreads
	}
	
	fun start() {
		threadList.forEach {
			it.running = true
			it.start()
		}
	}
	
	fun stop() {
		threadList.forEach {
			it.running = false
			it.join()
		}
	}
	
	fun isDone(): Boolean {
		threadList.forEach { 
			if (it.taskPool.size > 0) return false
		}
		return true
	}
	
}

class Worker(id: Int) : Thread("Worker $id") {
	
	val taskPool: TaskList = TaskList()
	
	var running = false
	
	override fun run() {
		
		while (running) {
			
			if (taskPool.size > 0) {
//				taskPool.poll()?.invoke()
				taskPool.first?.invoke()
				taskPool.remove(taskPool.first)
			}else {
				Thread.sleep(1) // sleep if no tasks
			}
			
		}
		
	}
	
	fun addTask(task: Task) = taskPool.add(task)
	
}
