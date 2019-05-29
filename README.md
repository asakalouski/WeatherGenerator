# Table of Contents
* [Prerequisites](#prerequisites)
* [Overview](#overview)
* [Main Components](#maincomponents)
* [Running the App](#running-the-app)
* [Running Tests](#testing)

## Prerequisites
Running application requires [SBT](https://www.scala-sbt.org/). The latest version will do.
## Overview
![Screenshot](diagram.png)
The application is a toy simulator for the weather environment which evolves over time. It consists of the central component "Control Tower" which on timer interval sends messages to "Weather Stations" and instructs them to collect the various measurements such as temperature, pressure, humidity and general weather conditions. After that, each station packs data into the event body and notifies "Event Aggregator" component which aggregates events/message from multiple stations and displays continuously updated results on the console. Messaging and concurrency implemented with the help of Akka and thus each of the main components is a separate Akka actor.
## Main Components
* Control Tower
* Weather Station
* Event Aggregator
* Elevation Calculator
* Climate Analyzer
    1. Time
    2. Season
    3. Temperature
    4. Condition
    5. 


## Running the App
### Simulation
```
sbt clean run
```
Application will output weather information in a table-like format, the data will be continuously updated.    

```
Berlin|52.52,13.404,34|2010-12-02 20:08:00|Sunny|0.95|1027.49|104.65
[info] Caracas|10.5,-66.9166,1734|1983-05-22 23:17:00|Rain|31.55|888.25|100.00
[info] Brisbane|-27.470125,153.021,0|2010-10-25 06:39:00|Sunny|21.07|1299.23|45.53
[info] Vorkuta|67.49884,64.05253,208|2010-06-29 09:05:00|Sunny|7.92|1146.78|100.00
[info] Marrakesh|-31.669746,-7.973328,0|2011-02-23 20:34:00|Rain|28.16|1340.05|64.70
[info] Rio De Janeiro|-22.970722,-43.182365,0|2011-03-19 16:01:00|Sunny|26.78|1029.01|100.00
[info] Tokyo|35.652832,139.8394,0|2013-03-19 15:45:00|Sunny|35.28|1044.22|55.04
[info] Minsk|53.893,27.5674,277|2012-04-07 11:48:00|Sunny|8.73|1034.56|61.59
[info] Oslo|59.911491,10.757933,0|2018-04-16 15:11:00|Rain|8.60|925.90|65.47
[info] Melbourne|-37.814,144.96332,34|2019-10-09 21:15:00|Sunny|26.35|1245.69|100.00
[info] Saint Petersburg|59.93863,30.31413,0|2019-06-07 05:48:00|Rain|16.35|884.47|72.91
[info] Cairo|30.044,31.34,138|2002-03-05 03:38:00|Rain|20.16|903.54|100.00
[info] Naples|40.85631,14.24641,138|2005-03-27 16:42:00|Sunny|14.49|907.03|100.00
[info] Shenzhen|22.54554,114.0683,0|2012-01-23 00:32:00|Sunny|21.07|963.44|48.21
[info] Sydney|-33.920019,150.924127,0|2019-10-17 13:21:00|Rain|24.84|1154.70|55.96
``` 

### Testing
```
sbt clean test
```

After test completion, none failures is expected

```
[info] Done compiling.
[info] ClimateAnalyzerTest:
[info] ClimateAnalyzer
[info] - should identify season for location and time
[info] - should identify climate by location
[info] - should return temperature range for climate and season
[info] - should return average rainy days/per month in season
[info] - should randomly increment time
[info] - should generate random humidity for location and time
[info] - should generate random pressure for location
[info] - should generate approximate weather condition for location and time
[info] - should generate temperature with elevation adjust for location and time
[info] ElevationCalculatorTest:
[info] ControlTowerTest:
[info] Elevation calculator
[info] StationTest:
[info] - should compute approximate elevation
[info] - should return None for out of range latitude and longitude
[info] WeatherControlTower
[info] - must Generate periodically send Events to weather stations
[info] WeatherStation
[info] - must Generate Event with weather data after receiving CollectWeatherDataEvent
[info] Run completed in 7 seconds, 4 milliseconds.
[info] Total number of tests run: 13
[info] Suites: completed 4, aborted 0
[info] Tests: succeeded 13, failed 0, canceled 0, ignored 0, pending 0
[info] All tests passed.
[success] Total time: 30 s, completed 29 May 2019, 1:29:59 pm
```
