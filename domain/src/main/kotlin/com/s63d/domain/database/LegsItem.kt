package com.s63d.domain.database

import com.s63d.domain.api.Distance
import com.s63d.domain.api.Duration
import org.springframework.data.mongodb.core.geo.GeoJsonPoint

data class LegsItem(val duration: Duration,
                    val startLocation: GeoJsonPoint,
                    val distance: Distance,
                    val startAddress: String = "",
                    val endLocation: GeoJsonPoint,
                    val endAddress: String = "",
                    val steps: List<StepsItem>)