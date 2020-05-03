package it.adami.api.user.http.routes

import cats.data.NonEmptyChain
import cats.effect.IO
import it.adami.api.user.http.json.{ErrorItem, ErrorsResponse}
import it.adami.api.user.validation.DomainValidation
import org.http4s.HttpRoutes

trait BaseRoutes {

  def routes: HttpRoutes[IO]

  def getErrorsResponse(items: NonEmptyChain[DomainValidation]): ErrorsResponse = {
    val list =
      items.toNonEmptyList.toList.map(item => ErrorItem(Some(item.field), item.errorMessage))
    ErrorsResponse(list)
  }

}
