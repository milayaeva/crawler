package core

import cats.effect.{Async, Resource}
import org.http4s.client.Client
import org.http4s.client.middleware.FollowRedirect
import org.http4s.ember.client.EmberClientBuilder

object HttpClient {
  def make[F[_] : Async]: Resource[F, Client[F]] =
    EmberClientBuilder.default[F].build.map(FollowRedirect(3))
}
 
