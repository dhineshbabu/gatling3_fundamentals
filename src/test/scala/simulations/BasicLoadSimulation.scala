package simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class BasicLoadSimulation extends Simulation {

  val httpConf = http.baseUrl("http://localhost:5000")
    .header("Accept", "application/json")

  //configure the csv feeder
  val csvFeeder = csv("data/books_data.csv").circular


  def getAllBooks() = {
    repeat(3) {
      exec(http("T01_GET_Books_All")
        .get("/api/v1/books")
        .check(status.is(200)))
    }
  }

  def getSpecificBook() = {
    repeat(3) {
      exec(http("T02_GET_Book_Specific")
        .get("/api/v1/book/1")
        .check(status.is(200)))
    }
  }

  def addOrUpdateBook() = {
    repeat(10) {
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
    .exec(getAllBooks())
    .pause(5)
    .exec(getSpecificBook())
    .pause(5)
    .exec(addOrUpdateBook())



  setUp(
    scn1.inject(
      nothingFor(5),
      atOnceUsers(5),
      rampUsers(10) during(10)
  ).protocols(httpConf.inferHtmlResources()))

}
