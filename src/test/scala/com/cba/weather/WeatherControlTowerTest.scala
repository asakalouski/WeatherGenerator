package com.cba.weather

import akka.actor.{ActorContext, ActorSystem, Props}
import akka.testkit.TestKit
import com.cba.weather.WeatherControlTower.CollectWeatherDataEvent
import org.scalatest.{BeforeAndAfterAll, WordSpecLike}

class WeatherControlTowerTest extends TestKit(ActorSystem("testSystem")) with WordSpecLike with BeforeAndAfterAll  {

  "WeatherControlTower" must {
    "Generate periodically send Events to weather stations" in {
      val randomGenerator = () => true
      val stationsBuilder = (_: ActorContext) => List(testActor)
      val towerActor = system.actorOf(Props(new WeatherControlTower(stationsBuilder, randomGenerator)), "WeatherControlTower")
      expectMsg(CollectWeatherDataEvent)
    }
  }

  override protected def afterAll(): Unit = {
    system.terminate()
  }
}
