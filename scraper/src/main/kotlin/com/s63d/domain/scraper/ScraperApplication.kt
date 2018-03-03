package com.s63d.domain.scraper

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ScraperApplication {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            runApplication<ScraperApplication>(*args)
        }
    }
}