package primary_adapters.rest.authentification

import com.google.inject.Inject
import secondary_adapters.mongodb.repository.MongoUsersRepository

/**
 * This object it's the class than call the repository service to verify if the user exist
 */
class UsersService @Inject() (usersMongoAccess: MongoUsersRepository) {
  def verifyUser(user: User) = ???
}
