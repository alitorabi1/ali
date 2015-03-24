package models

/**
 * Created by manny on 20/03/15.
 */

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

class Models {}

case class Login(id: Option[Long] = None, password: String)

object Login{

  def findById(id: Long): Option[String] = {

  }
}
