package perspective.circederivation

import scala.language.experimental.macros

import io.circe.*
import io.circe.Decoder.Result
import magnolia1.*

object MagnoliaDerive {

  object DeriveDecoder extends Derivation[Decoder] {

    def split[T](ctx: SealedTrait[Decoder, T]): Decoder[T] = new Decoder[T] {
      override def apply(c: HCursor): Result[T] =
        c.downField("$type")
          .as[String]
          .flatMap { tpe =>
            ctx.subtypes
              .collectFirst {
                case sub if tpe == sub.typeInfo.short =>
                  sub.typeclass(c).orElse(sub.typeclass.tryDecode(c.downField("$value")))
              }
              .toRight(DecodingFailure(s"No type named $tpe found", c.history))
              .flatten
          }
    }

    def join[T](ctx: CaseClass[Decoder, T]): Decoder[T] = new Decoder[T] {
      override def apply(c: HCursor): Result[T] =
        ctx.constructEither(p => p.typeclass.tryDecode(c.downField(p.label))).left.map(_.head)
    }
  }

  object DeriveEncoder extends Derivation[Encoder] {

    def split[T](ctx: SealedTrait[Encoder, T]): Encoder[T] = new Encoder[T] {
      override def apply(a: T): Json = ctx.choose(a) { sub =>
        val json    = sub.typeclass(sub.value)
        val tpeJson = Json.fromString(sub.typeInfo.short)
        json.arrayOrObject(
          Json.obj("$type" -> tpeJson, "$value" -> json),
          _ => Json.obj("$type" -> tpeJson, "$value" -> json),
          obj => Json.fromJsonObject(obj.add("$type", tpeJson))
        )
      }
    }

    def join[T](ctx: CaseClass[Encoder, T]): Encoder[T] = new Encoder[T] {
      override def apply(a: T): Json = Json.fromJsonObject(
        JsonObject.fromIterable(ctx.parameters.view.map(p => (p.label, p.typeclass(p.deref(a)))))
      )
    }
  }
}
