package perspective.circederivation

import perspective.circederivation.BenchmarkDefs.GenField

import io.circe.Json
import scala.util.Random

trait BenchmarkDefs {

  def encode(intId: Int, obj: AnyRef): Json

  def decode(intId: Int, json: Json): AnyRef

  def genField[A](rand: Random)(implicit genField: GenField[A]): A = genField.genField(rand)
}
object BenchmarkDefs {

  trait GenField[A] {
    def genField(rand: Random): A
  }
  object GenField {
    def gen[A](rand: Random)(implicit genField: GenField[A]): A = genField.genField(rand)

    implicit val genIntField: GenField[Int]         = (rand: Random) => rand.nextInt()
    implicit val genDoubleField: GenField[Double]   = (rand: Random) => rand.nextDouble()
    implicit val genStringField: GenField[String]   = (rand: Random) => rand.nextString(rand.nextInt(10))
    implicit val genBooleanField: GenField[Boolean] = (rand: Random) => rand.nextBoolean()
    implicit val genJsonField: GenField[Json] = (rand: Random) =>
      Json.obj(List.fill(rand.nextInt(10))((gen[String](rand), Json.fromString(gen[String](rand)))): _*)
  }
}
