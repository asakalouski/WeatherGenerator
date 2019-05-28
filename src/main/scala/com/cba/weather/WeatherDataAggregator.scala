package com.cba.weather

import akka.actor.Actor
import com.cba.weather.data.{EventData, Event}

class WeatherDataAggregator(output:(Iterable[EventData]) => Unit) extends Actor {

  var events:Map[String, EventData] = Map[String, EventData]()

  override def receive: Receive = {
    case Event(stationId, eventData) => {
      events = events + (stationId -> eventData)
      output(events.values)
    }
  }
}
