package it.adami.api.user.validation.user

import it.adami.api.user.http.json.ChangePasswordRequest
import cats.syntax.contravariantSemigroupal._

object ChangePasswordValidation extends UserValidator {

  def apply(req: ChangePasswordRequest): ValidationResult[ChangePasswordRequest] =
    (
      validatePassword(req.oldPassword),
      validatePassword(req.newPassword)
    ).mapN(ChangePasswordRequest.apply)

}
