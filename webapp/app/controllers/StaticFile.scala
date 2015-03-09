
package controllers

import play.api.mvc._

object StaticFile extends Controller {

  def html(file: String) = Action {
    var f = new File(file)

    if (f.exists())
      Ok(scala.io.Source.fromFile(f.getCanonicalPath()).mkString).as("text/html");
    else
      NotFound
  }

}