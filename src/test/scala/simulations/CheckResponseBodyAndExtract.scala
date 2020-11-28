package simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._

<<<<<<< HEAD
class CheckResponseBodyAndExtract extends Simulation {

  val httpConf = http.baseUrl("http://localhost:8080/app/")
    .header("Accept", "application/json")

  val scn = scenario("Check JSON Path")

      .exec(http("Get specific game")
      .get("videogames/1")
      .check(jsonPath("$.name").is("Resident Evil 4")))

      .exec(http("Get all video games")
      .get("videogames")
      .check(jsonPath("$[1].id").saveAs("gameId")))
      .exec { session => println(session); session}

      .exec(http("Get specific game")
      .get("videogames/${gameId}")
      .check(jsonPath("$.name").is("Gran Turismo 3"))
      .check(bodyString.saveAs("responseBody")))
        .exec { session => println(session("responseBody").as[String]); session}

=======
class CheckResponseBodyAndExtract extends Simulation{

  val httpConf = http.baseUrl("http://dummy.restapiexample.com")
    .header("Accept", "application/json")

  val scn = scenario("S01_Employee_Access_Scenario")

    .exec(http("T01_GET_Employee_Details_All")
      .get("/api/v1/employees")
      .check(jsonPath("$.data[0].employee_name").is("Tiger Nixon"))
      .check(jsonPath("$.data[0].id").saveAs("C_EmpId")))
      .pause(5)

    .exec { session => println(session); session}

    .exec(http("T02_GET_Employee_Details_Specific")
      .get("/api/v1/employee/${C_EmpId}")
      .check(jsonPath("$.data.employee_name").is("Tiger Nixon"))
      .check(jsonPath("$.data.id").saveAs("C_EmpId"))
      .check(bodyString.saveAs("responseBody")))
      .pause(5)

    .exec { session => println(session("responseBody").as[String]); session}
>>>>>>> test

  setUp(
    scn.inject(atOnceUsers(1))
  ).protocols(httpConf)

}
