package com.gscotrim.mate

import org.springframework.boot.fromApplication
import org.springframework.boot.with


fun main(args: Array<String>) {
	fromApplication<MateApplication>().with(TestcontainersConfiguration::class).run(*args)
}
