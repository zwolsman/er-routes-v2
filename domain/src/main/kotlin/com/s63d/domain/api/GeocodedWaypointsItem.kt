package com.s63d.domain.api

data class GeocodedWaypointsItem(val types: List<String>?,
                                 val geocoderStatus: String = "",
                                 val placeId: String = "")