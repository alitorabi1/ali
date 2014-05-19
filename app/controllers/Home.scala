package controllers

import play.api._
import play.api.mvc._
import utils.security.Secured

object Home extends Controller with Secured {

  def index = IsAuthenticated { user => request =>
      Ok(views.html.home())
  }
}