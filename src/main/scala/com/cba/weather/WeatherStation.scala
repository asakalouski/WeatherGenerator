package com.cba.weather

import java.time.LocalDateTime
import java.util.UUID

import akka.actor.{Actor, ActorRef}
import com.cba.weather.WeatherControlTower.CollectWeatherDataEvent
import com.cba.weather.climate.ClimateAnalyzer._
import com.cba.weather.data.{EventData, Location, Position, WeatherEvent}
import com.cba.weather.climate.ElevationCalculator._

class WeatherStation(val location: Location, localTime: LocalDateTime = LocalDateTime.now())
                    (implicit eventsAggregator: ActorRef) extends Actor {

  val stationId = UUID.randomUUID().toString
  var time = localTime

  override def receive: Receive = {
    case CollectWeatherDataEvent => {
      time = randomTimeIncrement(time)
      for (
        elevation <- approximateElevation(location);
        condition <- approximateCondition(location, time);
        pressure <- pressure(location);
        humidity <- humidity(location, time);
        temp <- temperatureWithElevationAdjust(location, time)
      ) yield (eventsAggregator ! WeatherEvent(stationId = stationId, eventData = EventData(label = location.label,
        position = Position(location.latitude, location.longitude, elevation),
        time = time,
        temperature = temp,
        conditions = condition,
        pressure = pressure,
        humidity = humidity
      )))
    }
  }
}
