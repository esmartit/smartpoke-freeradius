package com.esmartit.freeradiusservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FreeradiusServiceApplication

fun main(args: Array<String>) {
    runApplication<FreeradiusServiceApplication>(*args)
}
