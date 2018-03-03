package com.s63d.domain.api

import org.scoutant.polyline.PolylineDecoder
import org.springframework.data.mongodb.core.geo.GeoJsonLineString
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon

data class RoutesItem(val summary: String = "",
                      val copyrights: String = "",
                      val legs: List<LegsItem>,
                      val bounds: Bounds,
                      val overviewPolyline: Polyline) {
    companion object {
        val decoder = PolylineDecoder()
    }
    fun toDatabaseModel() : com.s63d.domain.database.RoutesItem {
        val points = decoder.decode(overviewPolyline.points).map {
            GeoJsonPoint(it.lng, it.lat)
        }
        return com.s63d.domain.database.RoutesItem(summary,copyrights, legs.map(LegsItem::toDatabaseModel), GeoJsonPolygon(listOf(bounds.northeast.toGeoJsonPoint(), bounds.southwest.toGeoJsonPoint())), GeoJsonLineString(points))
    }
}