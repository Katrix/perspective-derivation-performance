package perspective.circederivation

import cats.Id
import io.circe.*
import io.circe.Decoder.Result
import perspective.*
import perspective.derivation.*

object PerspectiveUnrollingDerive {

  inline given productDecoder[A, Gen[_[_]]](
      using gen: InlineHKDProductGeneric.Aux[A, Gen]
  ): Decoder[A] = new Decoder[A] {
    private val decoders = gen.summonInstances[Decoder]
    private val names    = gen.names

    override def apply(cursor: HCursor): Decoder.Result[A] =
      gen.tabulateTraverseK[Decoder.Result, Id](
        i => decoders.indexK(i).tryDecode(cursor.downField(names.indexK(i))),
        unrolling = true
      ) match {
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
      val list = gen.tabulateFoldLeft(Nil: List[(String, Json)], unrolling = true) { (acc, i) =>
        val json = gen.lateInlineMatch {
          a.productElementIdExact(i) match {
            case p: Byte    => Json.fromInt(p)
            case p: Short   => Json.fromInt(p)
            case p: Int     => Json.fromInt(p)
            case p: Long    => Json.fromLong(p)
            case p: Float   => Json.fromFloatOrString(p)
            case p: Double  => Json.fromDoubleOrString(p)
            case p: Boolean => Json.fromBoolean(p)
            case p: Char    => Json.fromString(Character.toString(p))
            case p: String  => Json.fromString(p)
            case other      => encoders.indexK(i)(other)
          }
        }

        (names.indexK(i), json) :: acc
      }

      Json.obj(list: _*)
    }
  }

  inline def deriveEncoder[A](implicit gen: InlineHKDProductGeneric[A]): Encoder[A] =
    productEncoder[A, gen.Gen](using gen)
  inline def deriveDecoder[A](implicit gen: InlineHKDProductGeneric[A]): Decoder[A] =
    productDecoder[A, gen.Gen](using gen)
}
