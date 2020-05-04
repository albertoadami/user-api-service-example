package it.adami.api.user.http.json

case class ErrorsResponse(errors: List[ErrorItem])
case class ErrorItem(field: Option[String], errorDescription: String)
