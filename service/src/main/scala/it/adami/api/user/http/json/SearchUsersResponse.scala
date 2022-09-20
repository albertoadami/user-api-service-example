package it.adami.api.user.http.json

case class SearchUserItem(
    id: Int,
    firstName: String,
    lastName: String,
    email: String,
    dateOfBirth: String,
    gender: String,
    creationDate: String
)

case class SearchUsersResponse(items: Seq[SearchUserItem])
