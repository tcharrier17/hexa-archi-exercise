package core.resources

object Role extends Enumeration {
  val ADMIN: String = "ADMIN"
  val REGULAR: String = "REGULAR"

  val LISTROLE: List[String] = List(ADMIN, REGULAR)
}
