package controllers

import play.api._
import play.api.mvc._
import utils.security.Secured

object Application extends Controller with Secured {

  def index = IsAuthenticated { user => request =>
    Redirect(routes.Home.index())
  }
}