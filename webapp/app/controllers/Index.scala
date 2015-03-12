package controllers

import play.api.mvc.{Action, Controller}

object Index extends Controller {
  def index = Action {
    Ok(views.html.index())
  }
}
