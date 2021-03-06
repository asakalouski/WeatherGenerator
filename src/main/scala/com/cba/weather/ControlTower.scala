package com.cba.weather

import java.util.UUID

import akka.actor.{Actor, ActorContext, ActorRef, ActorSystem, Props, Timers}
import com.cba.weather.MonitoredLocations.Locations
import com.cba.weather.output.ConsoleExtensions._

import scala.concurrent.duration._
import scala.util.Random


class ControlTower(stationsBuilder:(ActorContext) => List[ActorRef], randomGenerator: () => Boolean = ControlTower.defaultRandomBooleanGenerator) extends Actor with Timers {
  import ControlTower.{CollectWeatherDataEvent, SignalGeneratorKey, TickEvent}

  timers.startPeriodicTimer(SignalGeneratorKey, TickEvent, 1.second)

  val weatherStations = stationsBuilder(context)

  override def receive: Receive = {
    case TickEvent => {
      weatherStations.filter(_ => randomGenerator()).foreach(s => s ! CollectWeatherDataEvent)
    }
  }
}

object ControlTower extends App {

  val defaultRandomBooleanGenerator = () => Random.nextBoolean()

  private case object SignalGeneratorKey
  private case object TickEvent
  case object CollectWeatherDataEvent

  val system = ActorSystem("WeatherGenerator")

  val stationsBuilder = (context: ActorContext) => {
    implicit val eventsAggregator = context.actorOf(Props(new EventAggregator(printEventsToConsole)), "EventDataAggregator")
    Locations.map({case (loc, time) => context.actorOf(Props(new Station(location = loc, localTime = time)),
      name = s"${loc.label.replaceAll("\\s", "-")}-${UUID.randomUUID()}")})
  }

  val controlTower: ActorRef = system.actorOf(Props(new ControlTower(stationsBuilder)), "ControlTower")

  sys.ShutdownHookThread {
    system.stop(controlTower)
    system.terminate()
  }
}
