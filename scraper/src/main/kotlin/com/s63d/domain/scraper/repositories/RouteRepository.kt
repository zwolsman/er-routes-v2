package com.s63d.domain.scraper.repositories

import com.s63d.domain.database.RoutesItem
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface RouteRepository : MongoRepository<RoutesItem, ObjectId>