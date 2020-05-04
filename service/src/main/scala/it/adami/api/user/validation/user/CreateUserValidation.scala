package it.adami.api.user.validation.user

import it.adami.api.user.http.json.CreateUserRequest
import cats.syntax.contravariantSemigroupal._

object CreateUserValidation extends UserValidator {

  def apply(req: CreateUserRequest): ValidationResult[CreateUserRequest] = {
    (
      validateFirstName(req.firstname),
      validateLastName(req.lastname),
      validateEmail(req.email),
      validatePassword(req.password),
      validateBirthDate(req.dateOfBirth),
      validateGender(req.gender)
    ).mapN(CreateUserRequest.apply)
  }

}
