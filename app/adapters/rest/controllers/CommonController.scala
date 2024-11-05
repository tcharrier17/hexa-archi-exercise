package adapters.rest.controllers

import application.ports.outcoming.CommonHttp
import play.api.mvc.{Action, AnyContent, ControllerComponents, Request}

import javax.inject.{Inject, Singleton}

@Singleton
class CommonController @Inject()(val controllerComponents: ControllerComponents) extends CommonHttp {
  override def sendBadRequest(message: String): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    BadRequest(message)
  }

  override def sendUnauthorize(message: String): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Unauthorized(message)
  }

  override def sendError(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    InternalServerError
  }
}
