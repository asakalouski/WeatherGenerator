package com.cba.weather

import akka.actor.{ActorSystem, Props}
import akka.testkit.TestKit
import com.cba.weather.WeatherControlTower.CollectWeatherDataEvent
import com.cba.weather.data.{Location, WeatherEvent}
import org.scalatest.{BeforeAndAfterAll, WordSpecLike}
import scala.concurrent.duration._

class WeatherStationTest extends TestKit(ActorSystem("testSystem")) with WordSpecLike with BeforeAndAfterAll {

  "WeatherStation" must {
    "Generate Event with weather data after receiving CollectWeatherDataEvent" in {
      val location = Location("Cape Town", -33.918861, 18.4233)
      implicit val eventsAggregator = testActor
      val stationActor = system.actorOf(Props(new WeatherStation(location)), "TestWeatherStation")
      stationActor ! CollectWeatherDataEvent
      expectMsgPF(max = 20.seconds) {
        case WeatherEvent(stationId, _) => {
          assert(stationId == "Cape Town")
        }
        case _ => fail("Unknown message type")
      }
    }
  }

  override protected def afterAll(): Unit = {
    system.terminate()
  }
}
