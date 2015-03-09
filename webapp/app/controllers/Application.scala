
package controllers

import play.api.mvc._

/** Application controller, handles authentication */
object Application extends Controller {

  /** Serves the index page, see views/index.scala.html */
  def index = Action {
    Ok(views.html.index())
  }

}