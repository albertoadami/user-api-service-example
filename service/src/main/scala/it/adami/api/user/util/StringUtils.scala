package it.adami.api.user.util

import java.util.Date
import java.text.SimpleDateFormat

import scala.util.Try

/**
 * Utility class for working with string transformation from JSON requests
 */
object StringUtils {

  def parseDateTimeFromString(date: String): Try[Date] = {
    val dateForm = new SimpleDateFormat("dd-MM-yyyy")
    Try {
      dateForm.parse(date)
    }
  }

  def getDateFromString(date: String): Date = {
    val dateForm = new SimpleDateFormat("dd-MM-yyyy")
    dateForm.parse(date)
  }

  def isValidEmail(email: String): Boolean = {
    val ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$"
    val p = java.util.regex.Pattern.compile(ePattern)
    val m = p.matcher(email)
    m.matches()
  }

}
