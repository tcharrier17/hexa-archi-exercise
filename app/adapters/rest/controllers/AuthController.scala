package adapters.rest.controllers

import application.services.authentification.AuthService
import play.api.mvc._

import javax.inject.Inject

class AuthController @Inject()(val controllerComponents: ControllerComponents, authService: AuthService) extends BaseController {

//  def connect(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
//    val body = request.body.asJson
//    val userJson: UserEntity = body.get.as[UserEntity]
//
//    if(authService.authenticate(userJson.username, userJson.password)) Ok.apply("Connected") else Ok("Wrong username or password")
//  }

}
