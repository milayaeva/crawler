import cats.effect.{IO, IOApp}
import core.App

object Main extends IOApp.Simple {
  def run: IO[Unit] = {
    App.run[IO]
  }
}