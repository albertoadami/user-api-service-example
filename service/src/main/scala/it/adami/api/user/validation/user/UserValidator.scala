package it.adami.api.user.validation.user

import cats.data.ValidatedNec
import it.adami.api.user.validation.{DomainValidation, InvalidEmail, InvalidGender, InvalidPassword, IsEmpty}
import cats.implicits._
import it.adami.api.user.util.StringUtils

trait UserValidator {

  private def checkIfStringIsEmpty(string: String): Boolean = string.isEmpty

  type ValidationResult[A] = ValidatedNec[DomainValidation, A]

  def validateFirstName(firstName: String): ValidationResult[String] =
    if (checkIfStringIsEmpty(firstName)) IsEmpty("firstname").invalidNec else firstName.validNec

  def validateLastName(lastName: String): ValidationResult[String] =
    if (checkIfStringIsEmpty(lastName)) IsEmpty("lastname").invalidNec else lastName.validNec

  def validateBirthDate(birthDate: String): ValidationResult[String] = {
    StringUtils.parseDateTimeFromString(birthDate).fold(_ =>IsEmpty("dateOfBirth").invalidNec, _ => birthDate.validNec)
  }

  def validateGender(gender: String): ValidationResult[String] = {
    val genderUp = gender.toUpperCase
    if (genderUp == "MALE" || genderUp == "FEMALE") genderUp.validNec else InvalidGender.invalidNec
  }

  def validateEmail(email: String): ValidationResult[String] =
      if(StringUtils.isValidEmail(email)) email.validNec else InvalidEmail.invalidNec

  def validatePassword(password: String): ValidationResult[String] =
    if (checkIfStringIsEmpty(password) & password.length >= 8) InvalidPassword.invalidNec else password.validNec

}
