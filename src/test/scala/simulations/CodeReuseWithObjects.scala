package simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class CodeReuseWithObjects extends Simulation {

<<<<<<< HEAD
  val httpConf = http.baseUrl("http://localhost:8080/app/")
    .header("Accept", "application/json")


  def getAllVideoGames() = {
    repeat(3) {
      exec(http("Get all video games - 1st call")
        .get("videogames")
=======
  val httpConf = http.baseUrl("http://localhost:5000")
    .header("Accept", "application/json")

//  val scn = scenario("S01_Employee_Access_Scenario")
//
//    .exec(http("T01_GET_Employee_Details_All")
//      .get("/api/v1/employees")
//      .check(jsonPath("$.data[0].employee_name").is("Tiger Nixon"))
//      .check(jsonPath("$.data[0].id").saveAs("C_EmpId")))
//
//    .exec { session => println(session); session}
//
//    .exec(http("T02_GET_Employee_Details_Specific")
//      .get("/api/v1/employee/${C_EmpId}")
//      .check(jsonPath("$.data.employee_name").is("Tiger Nixon"))
//      .check(jsonPath("$.data.id").saveAs("C_EmpId"))
//      .check(bodyString.saveAs("responseBody")))

    //.exec { session => println(session("responseBody").as[String]); session}

  def getAllBooks() = {
    repeat(3) {
      exec(http("T01_GET_Books_All")
        .get("/api/v1/books")
>>>>>>> test
        .check(status.is(200)))
    }
  }

<<<<<<< HEAD
  def getSpecificVideoGame() = {
    repeat(5) {
      exec(http("Get specific game")
        .get("videogames/1")
        .check(status.in(200 to 210)))
    }
  }

  val scn = scenario("Code reuse")
      .exec(getAllVideoGames())
      .pause(5)
      .exec(getSpecificVideoGame())
      .pause(5)
      .exec(getAllVideoGames())

  setUp(
    scn.inject(atOnceUsers(1))
=======
  def getSpecificBook() = {
    repeat(3){
      exec(http("T02_GET_Book_Specific")
        .get("/api/v1/book/1")
        .check(status.is(200)))
    }
  }

  val scn2 = scenario("Books API Test")
      .exec(getAllBooks())
      .pause(5)
      .exec(getSpecificBook())



  setUp(
    scn2.inject(atOnceUsers(1))
>>>>>>> test
  ).protocols(httpConf)

}
