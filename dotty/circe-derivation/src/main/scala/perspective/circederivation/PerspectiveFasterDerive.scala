package perspective.circederivation

import cats.Id
import io.circe.*
import perspective.*
import perspective.derivation.*

object PerspectiveFasterDerive {

  def productDecoder[A](
      using gen: HKDProductGeneric[A],
      decoders: gen.Gen[Decoder]
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

  def productEncoder[A](
      using gen: HKDProductGeneric[A],
      encoders: gen.Gen[Encoder]
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

  def sumDecoder[A](
      using gen: HKDSumGeneric[A],
      decoders: gen.Gen[Decoder]
  ): Decoder[A] = new Decoder[A] {

    import gen.given

    override def apply(c: HCursor): Decoder.Result[A] = c
      .downField("$type")
      .as[String]
      .flatMap(tpe => gen.stringToName(tpe).toRight(DecodingFailure(s"No type named $tpe found", c.history)))
      .flatMap { name =>
        val i       = gen.nameToIndex(name)
        val decoder = decoders.indexK(i)
        decoder(c).orElse(decoder.tryDecode(c.downField("$value")))
      }
  }

  def sumEncoder[A](
      using gen: HKDSumGeneric[A],
      encoders: gen.Gen[Encoder]
  ): Encoder[A] = new Encoder[A] {

    import gen.given

    private val names = gen.names

    override def apply(a: A): Json = {
      val casting  = gen.indexOfACasting(a)
      val nameJson = Json.fromString(names.indexK(casting.index))
      val encoder  = encoders.indexK(casting.index)
      val json     = encoder(casting.value)

      json.arrayOrObject(
        Json.obj("$type" -> nameJson, "$value" -> json),
        _ => Json.obj("$type" -> nameJson, "$value" -> json),
        obj => Json.fromJsonObject(obj.add("$type", nameJson))
      )

    }
  }
}
