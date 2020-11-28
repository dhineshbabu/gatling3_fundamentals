package simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._

<<<<<<< HEAD
import scala.concurrent.duration._

class BasicLoadSimulation extends Simulation {

  val httpConf = http.baseUrl("http://localhost:8080/app/")
    .header("Accept", "application/json")

  def getAllVideoGames() = {
    exec(
      http("Get all video games")
        .get("videogames")
        .check(status.is(200))
    )
  }

  def getSpecificGame() = {
    exec(
      http("Get Specific Game")
        .get("videogames/2")
        .check(status.is(200))
    )
  }

  val scn = scenario("Basic Load Simulation")
    .exec(getAllVideoGames())
    .pause(5)
    .exec(getSpecificGame())
    .pause(5)
    .exec(getAllVideoGames())

  val scn2 = scenario("Basic Load Simulation 2")
    .exec(getAllVideoGames())
    .pause(5)
    .exec(getSpecificGame())
    .pause(5)
    .exec(getAllVideoGames())

  setUp(
    scn.inject(
      nothingFor(5 seconds),
      atOnceUsers(5),
      rampUsers(10) during (10 seconds)
    ).protocols(httpConf.inferHtmlResources()),
    scn2.inject(
      atOnceUsers(500)
    ).protocols(httpConf)
  )
=======
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
>>>>>>> test

}
