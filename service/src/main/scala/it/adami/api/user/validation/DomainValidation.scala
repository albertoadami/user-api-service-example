package it.adami.api.user.validation

sealed trait DomainValidation {
  def errorMessage: String
  def field: String
}

case class IsEmpty(field: String) extends DomainValidation {
  override def errorMessage: String = s"$field cannot be empty"
}

case object InvalidEmail extends DomainValidation {
  override def field: String = "email"
  override def errorMessage: String = "The email is invalid"
}

case object InvalidPassword extends DomainValidation {
  override def field: String = "password"

  override def errorMessage: String = "The password must be at least 8 characters long"
}

case object InvalidGender extends DomainValidation {
  override def field: String = "gender"

  override def errorMessage: String = "The gender can be only MALE or FEMALE"
}
