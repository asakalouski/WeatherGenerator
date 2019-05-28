package com.cba.weather.data

import java.time.LocalDateTime

case class WeatherEvent(stationId:String, eventData: EventData)

case class EventData(label: String, position:Position, time:LocalDateTime,
                     conditions: WeatherCondition, temperature: Double, pressure: Double, humidity: Double)




