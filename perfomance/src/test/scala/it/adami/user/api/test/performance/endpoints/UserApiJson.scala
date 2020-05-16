package it.adami.user.api.test.performance.endpoints

object UserApiJson {

  def signUp(firstname: String, lastname: String, email: String, password: String): String =
    s"""
      |{
      |	"firstname" : "${firstname}",
      |	"lastname": "$lastname",
      |	"email" : "$email",
      |	"password": "$password",
      |	"gender": "MALE",
      |	"dateOfBirth": "29-05-1992"
      |}
      |""".stripMargin

}
