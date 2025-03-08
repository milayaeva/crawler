package model

case class TitleResult(success: Boolean, value: String)
object TitleResult {
  def success(title: String): TitleResult = TitleResult(true, title)
  def failure(error: String): TitleResult =  TitleResult(false, error)
}