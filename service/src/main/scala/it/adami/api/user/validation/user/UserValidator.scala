package it.adami.api.user.validation.user

import cats.data.ValidatedNec
import it.adami.api.user.validation.{
  DomainValidation,
  InvalidEmail,
  InvalidGender,
  InvalidPassword,
  IsEmpty
}
import cats.implicits._

trait UserValidator {

  private def checkIfStringIsEmpty(string: String): Boolean = string.isEmpty

  type ValidationResult[A] = ValidatedNec[DomainValidation, A]

  def validateFirstName(firstName: String): ValidationResult[String] =
    if (checkIfStringIsEmpty(firstName)) IsEmpty("firstname").invalidNec else firstName.validNec

  def validateLastName(lastName: String): ValidationResult[String] =
    if (checkIfStringIsEmpty(lastName)) IsEmpty("lastname").invalidNec else lastName.validNec

  def validateBirthDate(birthDate: String): ValidationResult[String] = {
    if (checkIfStringIsEmpty(birthDate)) IsEmpty("dateOfBirth").invalidNec else birthDate.validNec
  }

  def validateGender(gender: String): ValidationResult[String] = {
    val genderUp = gender.toUpperCase
    if (genderUp == "MALE" || genderUp == "FEMALE") genderUp.validNec else InvalidGender.invalidNec
  }

  def validateEmail(email: String): ValidationResult[String] =
    if (checkIfStringIsEmpty(email)) InvalidEmail.invalidNec else email.validNec

  def validatePassword(password: String): ValidationResult[String] =
    if (checkIfStringIsEmpty(password)) InvalidPassword.invalidNec else password.validNec

}
