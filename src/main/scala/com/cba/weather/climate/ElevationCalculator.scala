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

  /**
    * Takes x,y coordinates of the image pixel and extracts individual rgb components
    * @param x
    * @param y
    * @return option tuple with individual rgb components
    */
  private[this] def getRGB(x: Int, y: Int): Option[(Int, Int, Int)] = Try(img.getRGB(x, y))
    .map(rgb => {
      val blue = rgb & 255
      val green = (rgb >> 8) & 255
      val red = (rgb >> 16) & 255
      (red, green, blue)
    }).toOption

  /**
    * Calculates approximate elevation, maps geographic location to the point at NASA gebco.png image.
    * Then calculates Euclidean distance between the point RGB colour components and pure white colour (255, 255, 255)
    * the highest point on Earth (8848m). Having Euclidean distance calculates elevation using a simple proportion.
    *
    * @param location geographic location (latitude, longitude). The values are bounded by ±90° and ±180° respectively.
    * @return approximate elevation in meters
    */
  def approximateElevation(location:Location): Option[Int] = {
    val y = (90d - location.latitude) * GeoGridHeight / 180
    val x = (180d + location.longitude) * GeoGridWidth / 360
    getRGB(x.intValue(), y.intValue()).map({case (red, green, blue) => {
      val colorDistance = sqrt(pow(WhiteRGB._1 - red, 2) + pow(WhiteRGB._2 - green, 2) + pow(WhiteRGB._3 - blue, 2))
      val elevation = HighestElevation - HighestElevation.doubleValue() * colorDistance / MaxColorDistance
      elevation.intValue()
    }})
  }
}
