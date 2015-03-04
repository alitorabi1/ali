package controllers

import play.api.mvc.Action
import models._
import play.api._
import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._
import play.mvc.Security
import play.api.mvc._

/**
 * Created by sahar on 02/03/15.
 */
object Application extends Controller {
  /** Serves the index page, see views/index.scala.html */
  def index = Action {
    Ok(views.html.index.render())
  }
}
