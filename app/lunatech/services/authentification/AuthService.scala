package lunatech.services.authentification

import lunatech.entities.UserEntity
import lunatech.resources.Role
import lunatech.services.cache.SyncCacheService
import lunatech.services.ports.UsersRepository

import java.util.Base64
import javax.inject.Inject

class AuthService @Inject()(cache: SyncCacheService, usersRepository: UsersRepository) {
  def authenticate(authHeader: String): Option[UserEntity] = {
    val encodedCredentials = authHeader.replaceFirst("Basic ", "")
    val decodedCredentials = new String(Base64.getDecoder.decode(encodedCredentials))
    val Array(providedUsername, providedPassword) = decodedCredentials.split(":", 2)

    // Vérifier l'utilisateur (via la base de données par exemple)
    val user: Option[UserEntity] = usersRepository.verifyUser(providedUsername, providedPassword)

    user match {
      case Some(value) =>
        // Si l'utilisateur est trouvé, stocker ses informations dans le cache
        if (Role.LISTROLE.contains(value.role)) cache.set("userRole", value.role) else throw new Error("Role error")
        cache.set("username", value.username)
        println("User connected")
        Some(value)
      case None => None
    }
  }

  def getUserRoleFromCache(): Option[String] = {
    cache.get[String]("userRole")
  }

  def getUsernameFromCache(): Option[String] = {
    cache.get[String]("username")
  }
}
