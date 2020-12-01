package simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._

class Assessment_Siumlation extends Simulation {

  val httpConf = http.baseUrl("http://python.52.15.219.198.xip.io")
    .header("Accept", "application/json")

  //configure the csv feeder0
  val csvFeeder = csv("data/books_data.csv").circular

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



  def getAllBooks() = {
    repeat(2) {
      exec(http("T01_GET_Books_All")
        .get("/api/v1/books")
        .check(status.is(200)))
    }
  }

  def getSpecificBook() = {
    repeat(2) {
      exec(http("T02_GET_Book_Specific")
        .get("/api/v1/book/1")
        .check(status.is(200)))
    }
  }

  def addOrUpdateBook() = {
    repeat(2) {
      feed(csvFeeder)
        .exec(http("T03_POST_ADD_UPDATE_Book")
          .post("/api/v1/books")
          .header("Content-Type", "application/json")
          .body(StringBody("""{
        "id": "${P_id}",
        "title": "${P_title}",
        "author": "${P_author}",
        "first_sentence": "${P_first_sentence}",
        "published": "${P_published}"}"""
          )).asJson
          .check(status.is(201)))
    }
  }

  val scn1 = scenario("Books API Test")
    .forever() {
      exec(getAllBooks())
        .pause(5)
        .exec(getSpecificBook())
        .pause(5)
        .exec(addOrUpdateBook())
    }


  setUp(
    scn1.inject(
      nothingFor(5 seconds),
      rampUsers(userCount) during(rampDuration second))
  ).protocols(httpConf.inferHtmlResources()).maxDuration(testDuration seconds)

}
