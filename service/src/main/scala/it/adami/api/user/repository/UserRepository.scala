package it.adami.api.user.repository

import cats.effect.IO
import doobie.util.transactor.Transactor.Aux
import it.adami.api.user.domain.User
import doobie._
import doobie.implicits._
import doobie.util.ExecutionContexts
import cats._
import cats.data._
import cats.effect._
import cats.implicits._

trait UserRepository {}
