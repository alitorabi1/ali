package controllers

/**
 * Created by chota_don on 15-03-01.
 */
import play.api.mvc._

object Application extends Controller{
  def index=Action{  implicit request =>
    Ok(views.html.Postpage.render())
  }

}
