package perspective.circederivation

import scala.compiletime.{erasedValue, summonFrom, summonInline}

import io.circe.*
import perspective.*
import perspective.derivation.*

object PerspectiveDerive {

  def productDecoder[A](
      using gen: HKDProductGeneric[A],
      decoders: gen.Gen[Decoder]
  ): Decoder[A] = new Decoder[A] {
    private val names = gen.names

    override def apply(cursor: HCursor): Decoder.Result[A] = {
      import gen.given

      gen.representable.indicesK
        .traverseIdK(
          [Z] => (i: gen.Index[Z]) => decoders.indexK(i).tryDecode(cursor.downField(names.indexK(i)))
        )
        .map(gen.from)
    }
  }

  def productEncoder[A](
      using gen: HKDProductGeneric[A],
      encoders: gen.Gen[Encoder]
  ): Encoder[A] = new Encoder[A] {
    private val names = gen.names

    override def apply(a: A): Json = {
      import gen.given

      val values = gen.to(a)

      val list = gen.representable.indicesK.foldLeftK(Nil: List[(String, Json)]) { acc =>
        [Z] => (i: gen.Index[Z]) => (names.indexK(i), encoders.indexK(i)(values.indexK(i))) :: acc
      }

      Json.obj(list: _*)
    }
  }
}
