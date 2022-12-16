package perspective.circederivation

import cats.Id
import io.circe.*
import io.circe.Decoder.Result
import perspective.*
import perspective.derivation.*

object PerspectiveInlineDerive {

  inline given productDecoder[A, Gen[_[_]]](
      using gen: InlineHKDProductGeneric.Aux[A, Gen]
  ): Decoder[A] = new Decoder[A] {
    private val decoders = gen.summonInstances[Decoder]
    private val names    = gen.names

    override def apply(cursor: HCursor): Decoder.Result[A] =
      gen.tabulateTraverseIdK(i => decoders.indexK(i).tryDecode(cursor.downField(names.indexK(i)))) match {
        case Right(value) => Right(gen.from(value))
        case Left(e)      => Left(e)
      }
  }

  inline given productEncoder[A, Gen[_[_]]](
      using gen: InlineHKDProductGeneric.Aux[A, Gen]
  ): Encoder[A] = new Encoder[A] {
    private val encoders = gen.summonInstances[Encoder]
    private val names    = gen.names

    override def apply(a: A): Json = {
      val list = gen.tabulateFoldLeft(Nil: List[(String, Json)]) { (acc, i) =>
        (names.indexK(i), encoders.indexK(i)(a.productElementId(i))) :: acc
      }

      Json.obj(list: _*)
    }
  }

  inline def deriveEncoder[A](implicit gen: InlineHKDProductGeneric[A]): Encoder[A] = productEncoder[A, gen.Gen](using gen)
  inline def deriveDecoder[A](implicit gen: InlineHKDProductGeneric[A]): Decoder[A] = productDecoder[A, gen.Gen](using gen)
}
