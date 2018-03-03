package com.s63d.simulator.repositories

import com.s63d.domain.database.RoutesItem
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query

interface RouteRepository : MongoRepository<RoutesItem, ObjectId>