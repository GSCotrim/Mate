package com.gscotrim.mate

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MateApplication

fun main(args: Array<String>) {
	runApplication<MateApplication>(*args)
}
