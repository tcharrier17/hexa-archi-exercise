package core.ports.outcoming

import play.api.mvc.{Action, AnyContent, BaseController}

trait CommonHttp extends BaseController {
  def sendBadRequest(message: String): Action[AnyContent]

  def sendUnauthorize(message: String): Action[AnyContent]

  def sendError(): Action[AnyContent]
}