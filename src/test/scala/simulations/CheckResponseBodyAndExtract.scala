package simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._

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

  setUp(
    scn.inject(atOnceUsers(1))
  ).protocols(httpConf)

}
