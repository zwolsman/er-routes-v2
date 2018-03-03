package com.s63d.simulator

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.result.Result
import com.s63d.domain.database.RoutesItem
import com.s63d.simulator.repositories.RouteRepository
import com.s63d.simulator.utils.*
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import kotlinx.coroutines.experimental.*
import org.springframework.data.geo.Point
import java.util.*
import kotlin.math.roundToInt

@Component
class SimulatorComponent(private val routeRepository: RouteRepository) : CommandLineRunner {
    companion object {
        //Config
        const val DESTINATION_URL = "http://localhost:8080/api/location"
        const val SLEEP_TIME: Long = 5_000
        const val REAL_LIFE_TIME = 30

        const val SIMULATIONS = 10000
    }

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun run(vararg args: String?) {
        logger.info("Loading routes..")
        val routes = routeRepository.findAll()

        launch {
            (0..SIMULATIONS).forEach {
                async {
                    SimulatorTask(routes.random(), it).run() }
                }
            delay(Random().nextInt(SLEEP_TIME.toInt()))
        }

        logger.info("Finished loading ${routes.size} routes")
        Thread.currentThread().join()
    }


    inner class SimulatorTask(private val route: RoutesItem, private val id:Int = 1) {
        private val logger = LoggerFactory.getLogger(this::class.java)
        private var durationleft = REAL_LIFE_TIME
        lateinit var currentLocation: Point

        suspend fun run() = launch {
            val steps = route.legs[0].steps
            logger.info("Starting route, duration: ${route.legs[0].duration.text}, distance: ${route.legs[0].distance.text}")
            currentLocation = steps[0].polyline.coordinates[0]

            for (step in steps) {
                val stepDuraton = step.duration.value

                if (stepDuraton <= durationleft) {
                    durationleft -= stepDuraton
                    currentLocation = step.endLocation
                    postLocation()
                    delay(SLEEP_TIME)
                } else {
                    val metersASecond: Int = step.distance.value / step.duration.value

                    val destinations = mutableListOf<Point>()
                    destinations.addAll(step.polyline.coordinates.filterNot { it == currentLocation })
                    while (destinations.isNotEmpty()) {
                        val dest = destinations[0]
                        val dist = distance(currentLocation, dest)
                        val timeTaken = (dist / metersASecond).roundToInt()

                        if (timeTaken <= durationleft) {
                            durationleft -= timeTaken
                            currentLocation = dest
                            destinations.removeAt(0)
                        } else {
                            val allowedDistance = durationleft * metersASecond
                            val normalized = (dest - currentLocation) / dist.toFloat()
                            currentLocation += (normalized * allowedDistance.toFloat())
                            durationleft = 0
                        }

                        if (durationleft == 0) {
                            durationleft = REAL_LIFE_TIME
                            postLocation()
                            delay(SLEEP_TIME)
                        }
                    }
                }
            }
            logger.info("Finished route")
        }

        private fun postLocation() {
            val req = Fuel.post(DESTINATION_URL, listOf("x" to currentLocation.x, "y" to currentLocation.y, "id" to id))
            req.response { request, response, result ->
                if (result is Result.Failure) {
                    println(result.error)
                }
            }
        }
    }

    private fun <E> List<E>.random(): E = if (size == 0) throw UnsupportedOperationException() else get(Random().nextInt(size))

}