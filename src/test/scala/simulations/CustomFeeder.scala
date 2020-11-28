package simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class CustomFeeder extends Simulation {

  val httpConf = http.baseUrl("http://localhost:5000")
    .header("Accept", "application/json")

  //configure the csv feeder
  val csvFeeder = csv("data/books_data.csv").circular

  val idNumber = (1 to 10).iterator

  val customFeeder = Iterator.continually(Map("P_id" -> idNumber.next()))


  def getAllBooks() = {
    repeat(3) {
      exec(http("T01_GET_Books_All")
        .get("/api/v1/books")
        .check(status.is(200)))
    }
  }

  def getSpecificBook() = {
    repeat(10) {
      feed(customFeeder)
      .exec(http("T02_GET_Book_Specific")
        .get("/api/v1/book/${P_id}")
        .check(status.is(200)))
    }
  }



  val scn1 = scenario("Books API Test")
    .exec(getAllBooks())
    .pause(5)
    .exec(getSpecificBook())
    .pause(5)


  setUp(
    scn1.inject(atOnceUsers(1))
  ).protocols(httpConf)

}
