package com.s63d.simulator

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SimulatorApplication {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            runApplication<SimulatorApplication>(*args)
        }
    }
}