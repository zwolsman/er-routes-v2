package com.s63d.domain.scraper

import com.s63d.domain.scraper.repositories.RouteRepository
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class ScraperComponent (val routeRepository: RouteRepository) {
    companion object {
        const val API_KEY = "AIzaSyAuNGqrWQIRmNpBF7uhbZ1koVxVWQZmRNY"
        const val URL = "https://maps.googleapis.com/maps/api/directions/json"
    }

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Scheduled(fixedRate = 5000)
    fun scrapeRoute() {

    }
}