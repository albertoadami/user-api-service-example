package it.adami.api.user.http.json

case class ErrorsResponse(errors: List[ErrorItem])

object ErrorsResponse {
  def apply(error: ErrorItem): ErrorsResponse = new ErrorsResponse(List(error))
}

case class ErrorItem(field: Option[String] = None, errorDescription: String)
