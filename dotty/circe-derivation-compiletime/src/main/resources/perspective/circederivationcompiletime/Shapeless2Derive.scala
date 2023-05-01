package perspective.circederivation

import io.circe.Decoder.Result
import io.circe.*
import shapeless2.*
import shapeless2.labelled.*

import scala.compiletime.summonInline
import scala.deriving.Mirror

object Shapeless2Derive {

  trait Shapeless2DeriveDecoder[A] extends Decoder[A]
  object Shapeless2DeriveDecoder:
    given consDecoder[H, K <: String, T <: HList](
        using hDecoder: Decoder[H],
        tDecoder: Shapeless2DeriveDecoder[T],
        key: ValueOf[K]
    ): Shapeless2DeriveDecoder[FieldType[K, H] :: T] with {
      override def apply(c: HCursor): Result[FieldType[K, H] :: T] =
        for
          h <- c.get(key.value)(hDecoder)
          t <- tDecoder(c)
        yield ::(field[K](h), t)
    }

    given Shapeless2DeriveDecoder[HNil] with {
      override def apply(c: HCursor): Result[HNil] = Right(HNil)
    }

    given cconsDecoder[H, K <: String, T <: Coproduct](
        using hDecoder: Decoder[H],
        tDecoder: Shapeless2DeriveDecoder[T],
        key: ValueOf[K]
    ): Shapeless2DeriveDecoder[FieldType[K, H] :+: T] with {
      override def apply(c: HCursor): Result[FieldType[K, H] :+: T] = c
        .downField("$type")
        .as[String]
        .flatMap { tpe =>
          if (tpe == key.value)
            hDecoder(c)
              .orElse(hDecoder.tryDecode(c.downField("$value")))
              .map(h => Inl[FieldType[K, H], T](field[K](h)))
          else tDecoder(c).map(t => Inr[FieldType[K, H], T](t))
        }
    }

    given Shapeless2DeriveDecoder[CNil] with {
      override def apply(c: HCursor): Result[CNil] = sys.error("impossible")
    }

  trait Shapeless2DeriveEncoder[A] extends Encoder[A]
  object Shapeless2DeriveEncoder:
    given [H, K <: String, T <: Coproduct](
        using hEncoder: Encoder[H],
        tEncoder: Shapeless2DeriveEncoder[T],
        key: ValueOf[K]
    ): Shapeless2DeriveEncoder[FieldType[K, H] :+: T] with {
      override def apply(a: FieldType[K, H] :+: T): Json = a match
        case Inl(head) =>
          val json    = hEncoder(head)
          val nameStr = Json.fromString(key.value)

          json.arrayOrObject(
            Json.obj("$type" -> nameStr, "$value" -> json),
            _ => Json.obj("$type" -> nameStr, "$value" -> json),
            obj => Json.fromJsonObject(obj.add("$type", nameStr))
          )
        case Inr(tail) => tEncoder(tail)
    }

    given Shapeless2DeriveEncoder[CNil] with {
      override def apply(a: CNil): Json = a.impossible
    }

  trait Shapeless2DeriveEncoderParts[A]:
    def parts(a: A): List[(String, Json)]

  object Shapeless2DeriveEncoderParts:
    given [H, K <: String, T <: HList](
        using hEncoder: Encoder[H],
        tEncoder: Shapeless2DeriveEncoderParts[T],
        key: ValueOf[K]
    ): Shapeless2DeriveEncoderParts[FieldType[K, H] :: T] with {
      override def parts(a: FieldType[K, H] :: T): List[(String, Json)] =
        (key.value -> hEncoder(a.head)) :: tEncoder.parts(a.tail)
    }

    given Shapeless2DeriveEncoderParts[HNil] with {
      override def parts(a: HNil): List[(String, Json)] = Nil
    }

  def adtDecoder[A](
      using gen: LabelledGeneric[A],
      decoder: Shapeless2DeriveDecoder[gen.Repr]
  ): Shapeless2DeriveDecoder[A] =
    (c: HCursor) => decoder(c).map(gen.from)

  def productEncoder[A](
      using gen: LabelledGeneric[A],
      hlistEncoder: Shapeless2DeriveEncoderParts[gen.Repr]
  ): Shapeless2DeriveEncoder[A] = (a: A) => Json.obj(hlistEncoder.parts(gen.to(a)): _*)

  def sumEncoder[A](
      using gen: LabelledGeneric[A],
      hlistEncoder: Shapeless2DeriveEncoder[gen.Repr]
  ): Shapeless2DeriveEncoder[A] = (a: A) => hlistEncoder(gen.to(a))
}
