package simulations

import com.fasterxml.jackson.databind.PropertyName
import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._

class RunTimeParameters extends Simulation {

  private def getProperty(propertyName: String, defaultValue:String) = {
    Option(System.getenv(propertyName))
      .orElse(Option(System.getProperty(propertyName)))
      .getOrElse(defaultValue)
  }

  def userCount: Int = getProperty("USERS", "5").toInt
  def rampDuration: Int = getProperty("RAMP_DURATION", "10").toInt
  def testDuration: Int = getProperty("DURATION", "60").toInt

  before {
    println(s"Running tests with ${userCount} users")
    println(s"Ramping users over ${rampDuration} seconds")
    println(s"Total test duration ${testDuration} seconds")
  }


  val httpConf = http.baseUrl("http://localhost:5000")
    .header("Accept", "application/json")



  def getAllBooks() = {
    repeat(2) {
      exec(http("T01_GET_Books_All")
        .get("/api/v1/books")
        .check(status.is(200)))
    }
  }

  val scn1 = scenario("Books API Test")
    .forever() {
      exec(getAllBooks())
        .pause(2 seconds)
    }


  setUp(
    scn1.inject(
      nothingFor(5 seconds),
      rampUsers(userCount) during(rampDuration second))
    ).protocols(httpConf.inferHtmlResources()).maxDuration(testDuration seconds)

}
