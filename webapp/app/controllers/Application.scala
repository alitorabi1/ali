package controllers

/**
 * Created by chota_don on 15-03-01.
 */

import play.api.libs.json
import play.api.mvc._
import play.api.libs.json.{Json, JsValue, __}

object Application extends Controller{



  def returnUrl(text:JsValue):JsValue ={
    val str=(text \ "url").as[String]
    val obj=new UrlPreview()
    obj.returnDescription(str)
    val jsonObj:JsValue=Json.obj(
      "title" ->obj.returnTitle(),
      "description"->obj.returnDescription(),
      "img"->obj.returnImage()
    )

    return jsonObj

  }

  def index=Action{  implicit request =>
    Ok(views.html.Postpage.render())
  }

}
