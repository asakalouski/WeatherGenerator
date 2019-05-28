package com.cba.weather.data

import java.time.LocalDateTime

case class Event(stationId:String, eventData: EventData)

case class EventData(label: String, position:Position, time:LocalDateTime,
                     conditions: Condition, temperature: Double, pressure: Double, humidity: Double)




