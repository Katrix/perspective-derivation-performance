package perspective.circederivation

import io.circe.Decoder.Result
import io.circe.{Decoder, Encoder, HCursor, Json}
import shapeless._
import shapeless.labelled._
import shapeless.tag.@@

object Shapeless2Derive {

  trait Shapeless2DeriveDecoder[A] extends Decoder[A]
  object Shapeless2DeriveDecoder {
    implicit def consDecoder[H, K <: String, T <: HList](
        implicit hDecoder: Decoder[H],
        tDecoder: Shapeless2DeriveDecoder[T],
        key: ValueOf[K]
    ): Shapeless2DeriveDecoder[FieldType[Symbol @@ K, H] :: T] =
      new Shapeless2DeriveDecoder[FieldType[Symbol @@ K, H] :: T] {
        override def apply(c: HCursor): Result[FieldType[Symbol @@ K, H] :: T] =
          for {
            h <- c.get(key.value)(hDecoder)
            t <- tDecoder(c)
          } yield ::(field[Symbol @@ K](h), t)
      }

    implicit def cconsDecoder[H, K <: String, T <: Coproduct](
        implicit hDecoder: Decoder[H],
        tDecoder: Shapeless2DeriveDecoder[T],
        key: ValueOf[K]
    ): Shapeless2DeriveDecoder[FieldType[Symbol @@ K, H] :+: T] =
      new Shapeless2DeriveDecoder[FieldType[Symbol @@ K, H] :+: T] {
        override def apply(c: HCursor): Result[FieldType[Symbol @@ K, H] :+: T] = c
          .downField("$type")
          .as[String]
          .flatMap { tpe =>
            if (tpe == key.value)
              hDecoder(c)
                .orElse(hDecoder.tryDecode(c.downField("$value")))
                .map(h => Inl[FieldType[Symbol @@ K, H], T](field[Symbol @@ K](h)))
            else tDecoder(c).map(t => Inr[FieldType[Symbol @@ K, H], T](t))
          }
      }

    implicit def hnilDecoder: Shapeless2DeriveDecoder[HNil] = new Shapeless2DeriveDecoder[HNil] {
      override def apply(c: HCursor): Result[HNil] = Right(HNil)
    }

    implicit def cnilDecoder: Shapeless2DeriveDecoder[CNil] = new Shapeless2DeriveDecoder[CNil] {
      override def apply(c: HCursor): Result[CNil] = sys.error("impossible")
    }
  }

  trait Shapeless2DeriveEncoderParts[A] {
    def parts(a: A): List[(String, Json)]
  }
  object Shapeless2DeriveEncoderParts {
    implicit def consEncoderParts[H, K <: String, T <: HList](
        implicit hEncoder: Encoder[H],
        tEncoder: Shapeless2DeriveEncoderParts[T],
        key: ValueOf[K]
    ): Shapeless2DeriveEncoderParts[FieldType[Symbol @@ K, H] :: T] =
      new Shapeless2DeriveEncoderParts[FieldType[Symbol @@ K, H] :: T] {
        override def parts(a: FieldType[Symbol @@ K, H] :: T): List[(String, Json)] =
          (key.value -> hEncoder(a.head)) :: tEncoder.parts(a.tail)
      }

    implicit def hnilEncoderParts: Shapeless2DeriveEncoderParts[HNil] = new Shapeless2DeriveEncoderParts[HNil] {
      override def parts(a: HNil): List[(String, Json)] = Nil
    }
  }

  trait Shapeless2DeriveEncoder[A] extends Encoder[A]
  object Shapeless2DeriveEncoder {
    implicit def cconsEncoder[H, K <: String, T <: Coproduct](
        implicit hEncoder: Encoder[H],
        tEncoder: Shapeless2DeriveEncoder[T],
        key: ValueOf[K]
    ): Shapeless2DeriveEncoder[FieldType[Symbol @@ K, H] :+: T] =
      new Shapeless2DeriveEncoder[FieldType[Symbol @@ K, H] :+: T] {
        override def apply(a: FieldType[Symbol @@ K, H] :+: T): Json = a match {
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
      }

    implicit def cnilEncoder: Shapeless2DeriveEncoder[CNil] = new Shapeless2DeriveEncoder[CNil] {
      override def apply(a: CNil): Json = a.impossible
    }
  }

  def adtDecoder[A, Repr](
      implicit gen: LabelledGeneric.Aux[A, Repr],
      reprEncoder: Shapeless2DeriveDecoder[Repr]
  ): Decoder[A] = (c: HCursor) => reprEncoder(c).map(gen.from)

  def productEncoder[A, Repr](
      implicit gen: LabelledGeneric.Aux[A, Repr],
      reprEncoder: Shapeless2DeriveEncoderParts[Repr]
  ): Encoder[A] = (a: A) => Json.obj(reprEncoder.parts(gen.to(a)): _*)

  def sumEncoder[A, Repr](
      implicit gen: LabelledGeneric.Aux[A, Repr],
      reprEncoder: Shapeless2DeriveEncoder[Repr]
  ): Encoder[A] = (a: A) => reprEncoder(gen.to(a))
}
