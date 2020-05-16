package it.adami.user.api.test.performance.util

object StringUtil {
  def getSessionValue(key: String): String = s"$${key}"
}
