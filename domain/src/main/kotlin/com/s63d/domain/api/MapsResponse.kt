package com.s63d.domain.api

data class MapsResponse(val routes: List<RoutesItem>,
                        val geocodedWaypoints: List<GeocodedWaypointsItem>?,
                        val status: String = "")