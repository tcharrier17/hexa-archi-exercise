package primary_adapters.rest.authentification

import play.silhouette.api.Identity

case class User(username: String, password: String) extends Identity
