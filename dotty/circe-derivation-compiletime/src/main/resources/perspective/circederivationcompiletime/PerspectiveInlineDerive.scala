package perspective.circederivation

import scala.compiletime.{erasedValue, summonFrom, summonInline}

import io.circe.*
import io.circe.Decoder.Result
import perspective.*
import perspective.derivation.*

object PerspectiveInlineDerive {

  inline given productDecoder[A](using gen: InlineHKDProductGeneric[A]): Decoder[A] = new Decoder[A] {
    private val decoders = gen.summonInstances[Decoder]
    private val names    = gen.names

    override def apply(cursor: HCursor): Decoder.Result[A] =
      gen.tabulateTraverseIdK(i => decoders.indexK(i).tryDecode(cursor.downField(names.indexK(i)))) match {
        case Right(value) => Right(gen.from(value))
        case Left(e)      => Left(e)
      }
  }

  inline given productEncoder[A](using gen: InlineHKDProductGeneric[A]): Encoder[A] = new Encoder[A] {
    private val encoders = gen.summonInstances[Encoder]
    private val names    = gen.names

    override def apply(a: A): Json = {
      val list = gen.tabulateFoldLeft(Nil: List[(String, Json)]) { (acc, i) =>
        (names.indexK(i), encoders.indexK(i)(a.productElementId(i))) :: acc
      }

      Json.obj(list: _*)
    }
  }

  inline def sumDecoder[A](using gen: InlineHKDSumGeneric[A]): Decoder[A] = new Decoder[A] {
    private val decoders = gen.summonInstances[Decoder]

    override def apply(c: HCursor): Decoder.Result[A] = c
      .downField("$type")
      .as[String]
      .flatMap(tpe => gen.stringToName(tpe).toRight(DecodingFailure(s"No type named $tpe found", c.history)))
      .flatMap { name =>
        val i: gen.Index  = gen.nameToIndex(name)
        val decoder = decoders.indexK(i)
        decoder(c).orElse(decoder.tryDecode(c.downField("$value")))
      }
  }

  inline def sumEncoder[A](using gen: InlineHKDSumGeneric[A]): Encoder[A] = new Encoder[A] {
    private val names    = gen.names
    private val encoders = gen.summonInstances[Encoder]

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
