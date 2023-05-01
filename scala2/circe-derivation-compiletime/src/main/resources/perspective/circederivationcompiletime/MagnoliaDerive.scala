package perspective.circederivation

import io.circe.Decoder.Result
import io.circe._
import magnolia1._

import scala.language.experimental.macros

object MagnoliaDerive {

  object DeriveDecoder {
    type Typeclass[T] = Decoder[T]

    def split[T](ctx: SealedTrait[Decoder, T]): Decoder[T] = new Decoder[T] {
      override def apply(c: HCursor): Result[T] =
        c.downField("$type")
          .as[String]
          .flatMap { tpe =>
            ctx.subtypes
              .collectFirst {
                case sub if tpe == sub.typeName.short =>
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

    implicit def derived[T]: Decoder[T] = macro Magnolia.gen[T]
  }

  object DeriveEncoder {
    type Typeclass[T] = Encoder[T]

    def split[T](ctx: SealedTrait[Encoder, T]): Encoder[T] = new Encoder[T] {
      override def apply(a: T): Json = ctx.split(a) { sub =>
        val json    = sub.typeclass(sub.cast(a))
        val tpeJson = Json.fromString(sub.typeName.short)
        json.arrayOrObject(
          Json.obj("$type" -> tpeJson, "$value" -> json),
          _ => Json.obj("$type" -> tpeJson, "$value" -> json),
          obj => Json.fromJsonObject(obj.add("$type", tpeJson))
        )
      }
    }

    def join[T](ctx: CaseClass[Encoder, T]): Encoder[T] = new Encoder[T] {
      override def apply(a: T): Json = Json.fromJsonObject(
        JsonObject.fromIterable(ctx.parameters.view.map(p => (p.label, p.typeclass(p.dereference(a)))))
      )
    }

    implicit def derived[T]: Encoder[T] = macro Magnolia.gen[T]
  }

}
