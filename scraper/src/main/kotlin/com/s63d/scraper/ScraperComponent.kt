package com.s63d.scraper

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.jackson.jacksonDeserializerOf
import com.github.kittinunf.fuel.jackson.mapper
import com.github.kittinunf.result.Result
import com.s63d.domain.api.MapsResponse
import com.s63d.domain.api.RoutesItem
import com.s63d.scraper.repositories.RouteRepository
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class ScraperComponent (val routeRepository: RouteRepository) {
    companion object {
        const val API_KEY = "AIzaSyAuNGqrWQIRmNpBF7uhbZ1koVxVWQZmRNY"
        const val URL = "https://maps.googleapis.com/maps/api/directions/json"
        init {
            FuelManager.instance.baseParams = listOf("mode" to "driving","key" to API_KEY)
            mapper.propertyNamingStrategy = PropertyNamingStrategy.SNAKE_CASE
        }
    }

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Scheduled(fixedRate = 5000)
    fun scrapeRoute() {
        val from = cities.random()
        val to = cities.random()

        logger.info("Calculating trip from $from to $to")
        Fuel.get(URL, listOf("origin" to from, "destination" to to)).responseObject(jacksonDeserializerOf<MapsResponse>()) { _, response, result ->


            if (result is Result.Failure) {
                logger.error("Response: ${response.responseMessage} (${response.statusCode})", result.error)
                return@responseObject
            }
            val result = (result as Result.Success).value
            logger.info("Response: ${response.responseMessage} (${response.statusCode})")
            logger.info("Found ${result.routes.size} route${if(result.routes.size == 1) "" else "s"}")
            result.routes.map(RoutesItem::toDatabaseModel).forEach {
                val entity = routeRepository.save(it)
                logger.info("Saved route with id ${entity.id.toHexString()}")
            }
        }
    }
}