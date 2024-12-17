package primary_adapters.rest.authentification

import core.domain.models.UserEntity
import core.ports.UsersRepository

import java.util.Base64
import javax.inject.Inject

class AuthService @Inject()(usersRepository: UsersRepository) {
  def authenticate(authHeader: String): Option[UserEntity] = {
    val encodedCredentials = authHeader.replaceFirst("Basic ", "")
    val decodedCredentials = new String(Base64.getDecoder.decode(encodedCredentials))
    val Array(providedUsername, providedPassword) = decodedCredentials.split(":", 2)

    // Vérifier l'utilisateur (via la base de données par exemple)
    val user = usersRepository.getUserByUsername(providedUsername)

    user match {
      case Some(value) => Some(value)
      case None => None
    }
  }
}
