package com.cba.weather.climate

import java.awt.image.BufferedImage

import com.cba.weather.data.Location
import javax.imageio.ImageIO

import scala.math.{pow, sqrt}
import scala.util.Try

object ElevationCalculator {

  private val GeoGridWidth = 21600
  private val GeoGridHeight = 10800
  private val WhiteRGB = (255, 255, 255)
  private val HighestElevation = 8848
  private val MaxColorDistance = sqrt( pow(WhiteRGB._1, 2) + pow(WhiteRGB._2, 2) + pow(WhiteRGB._3, 2))

  val img: BufferedImage = ImageIO.read(getClass.getResource("/gebco.png"))

  private[this] def getRGB(x: Int, y: Int): Option[(Int, Int, Int)] = Try(img.getRGB(x, y))
    .map(rgb => {
      val blue = rgb & 255
      val green = (rgb >> 8) & 255
      val red = (rgb >> 16) & 255
      (red, green, blue)
    }).toOption

  //Decimal degrees, as with latitude and longitude, the values are bounded by ±90° and ±180° respectively.
  def approximateElevation(loc:Location): Option[Int] = {
    val y = (90d - loc.latitude) * GeoGridHeight / 180
    val x = (180d + loc.longitude) * GeoGridWidth / 360
    getRGB(x.intValue(), y.intValue()).map({case (red, green, blue) => {
      val colorDistance = sqrt(pow(WhiteRGB._1 - red, 2) + pow(WhiteRGB._2 - green, 2) + pow(WhiteRGB._3 - blue, 2))
      val elevation = HighestElevation - HighestElevation.doubleValue() * colorDistance / MaxColorDistance
      elevation.intValue()
    }})
  }
}
