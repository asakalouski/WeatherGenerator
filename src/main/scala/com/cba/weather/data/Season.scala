package com.cba.weather.data

sealed trait Season

object Season {
  case object Winter extends Season
  case object Spring extends Season
  case object Summer extends Season
  case object Autumn extends Season
}


