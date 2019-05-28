package com.cba.weather.output

import java.time.format.DateTimeFormatter

import com.cba.weather.data.EventData

object ConsoleExtensions {

  val printEventsToConsole:(Iterable[EventData]) => Unit = (events => {
    print("\033[H\033[2J")
    events.foreach(eventData => println(eventData.toConsoleString))
  })

  implicit class ConsoleEventDataExtension (eventData: EventData) {
    def toConsoleString = {
      val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
      f"${eventData.label}|${eventData.position.latitude},${eventData.position.longitude},${eventData.position.elevation}|${eventData.time.format(formatter)}|${eventData.conditions}|${eventData.temperature}%1.2f|${eventData.pressure}%1.2f|${eventData.humidity}%1.2f"
    }
  }
}
