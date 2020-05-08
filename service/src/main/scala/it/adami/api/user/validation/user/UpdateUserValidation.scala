package it.adami.api.user.validation.user

import it.adami.api.user.http.json.UpdateUserRequest
import cats.syntax.contravariantSemigroupal._

object UpdateUserValidation extends UserValidator {

  def apply(req: UpdateUserRequest): ValidationResult[UpdateUserRequest] =
    (
      validateFirstName(req.firstname),
      validateLastName(req.lastname),
      validateBirthDate(req.dateOfBirth),
      validateGender(req.gender)
    ).mapN(UpdateUserRequest.apply)

}
