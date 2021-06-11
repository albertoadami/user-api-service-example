package it.adami.user.api.test.performance.data

trait SimulationUser {
  def firstNameKey: String
  def lastNameKey: String
  def emailKey: String
  def passwordKey: String
  def genderKey: String
  def dateOfBirth: String
}

object SimulationUserImpl extends SimulationUser {
  override val firstNameKey: String = "firstNameKey"

  override val lastNameKey: String = "lastNameKey"

  override val emailKey: String = "emailKey"

  override val passwordKey: String = "passwordKey"

  override val genderKey: String = "genderKey"

  override val dateOfBirth: String = "dateOfBirth"
}
