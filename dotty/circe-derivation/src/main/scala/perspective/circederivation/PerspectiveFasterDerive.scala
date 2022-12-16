package perspective.circederivation

import cats.Id
import io.circe.*
import perspective.*
import perspective.derivation.*

object PerspectiveFasterDerive {

  def productDecoder[A, Gen[_[_]]](
      using gen: HKDProductGeneric.Aux[A, Gen],
      decoders: Gen[Decoder]
  ): Decoder[A] = new Decoder[A] {
    private val names = gen.names

    override def apply(cursor: HCursor): Decoder.Result[A] = {
      import gen.given

      gen.tabulateTraverseKEither[DecodingFailure, Id](
        [Z] => (i: gen.Index[Z]) => decoders.indexK(i).tryDecode(cursor.downField(names.indexK(i)))
      ) match {
        case Right(value) => Right(gen.from(value))
        case Left(l)      => Left(l)
      }
    }
  }

  def productEncoder[A, Gen[_[_]]](
      using gen: HKDProductGeneric.Aux[A, Gen],
      encoders: Gen[Encoder]
  ): Encoder[A] = new Encoder[A] {
    private val names = gen.names

    override def apply(a: A): Json = {
      import gen.given

      val list = gen.tabulateFoldLeft(Nil: List[(String, Json)]) { acc =>
        [Z] => (i: gen.Index[Z]) => (names.indexK(i), encoders.indexK(i)(a.productElementId(i))) :: acc
      }

      Json.obj(list: _*)
    }
  }

  inline def deriveEncoder[A](using gen: HKDProductGeneric[A]): Encoder[A] = {
    val encoders = scala.compiletime.summonInline[gen.Gen[Encoder]]
    productEncoder[A, gen.Gen](using gen, encoders)
  }

  inline def deriveDecoder[A](using gen: HKDProductGeneric[A]): Decoder[A] = {
    val decoders = scala.compiletime.summonInline[gen.Gen[Decoder]]
    productDecoder[A, gen.Gen](using gen, decoders)
  }
}
