package controllers

/**
 * Created by chota_don on 15-03-01.
 */

import com.fasterxml.jackson.databind.JsonNode
import play.api.libs.json
import play.api.mvc._
import play.api.libs.json.{Reads, Json, JsValue, __}
import play.mvc.Controller
import com.fasterxml.jackson._

object Application extends Controller{

//  def returnUrl(text:String) = Action {
//    //val str=params.get("age")
//    //JsonNode json=request().body().asJson()
//    //val str = (text \ "url").as[String]
//    val obj = new UrlPreview()
//    obj.returnDescription(text)
////    val jsonObj = Json.toJson(
////      Map(
////        "title" -> obj.returnTitle(),
////        "description" -> obj.returnDescription(),
////        "img" -> obj.returnImage()
////      )
////    )
//        val jsonObj:JsValue=Json.obj(
//          "title" ->obj.returnTitle(),
//          "description"->obj.returnDescription(),
//          "img"->obj.returnImage()
//        )
//    Ok(jsonObj)
//
//  }

  def index=Action { implicit request =>
    Ok(views.html.Postpage)

  }


}
