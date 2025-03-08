package service

import cats.effect.{Async, Resource}
import org.http4s.client.Client
import org.http4s.ember.client.EmberClientBuilder
import org.http4s.client.middleware.FollowRedirect

object HttpClient {
  def make[F[_] : Async]: Resource[F, Client[F]] =
    EmberClientBuilder.default[F].build.map(FollowRedirect(3))
}
 
