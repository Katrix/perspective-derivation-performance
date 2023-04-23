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

    implicit def hnilDecoder: Shapeless2DeriveDecoder[HNil] = new Shapeless2DeriveDecoder[HNil] {
      override def apply(c: HCursor): Result[HNil] = Right(HNil)
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

  def productDecoder[A, Repr](
      implicit gen: LabelledGeneric.Aux[A, Repr],
      hlistDecoder: Shapeless2DeriveDecoder[Repr]
  ): Decoder[A] = (c: HCursor) => hlistDecoder(c).map(gen.from)

  def productEncoder[A, Repr](
      implicit gen: LabelledGeneric.Aux[A, Repr],
      hlistEncoder: Shapeless2DeriveEncoderParts[Repr]
  ): Encoder[A] = (a: A) => Json.obj(hlistEncoder.parts(gen.to(a)): _*)

  trait DerivedEncoder[A] {
    def encoder: Encoder[A]
  }

  object DerivedEncoder {
    implicit def derived[A, Repr](
        implicit gen: LabelledGeneric.Aux[A, Repr],
        encoders: Shapeless2DeriveEncoderParts[Repr]
    ): DerivedEncoder[A] = new DerivedEncoder[A] {
      override def encoder: Encoder[A] = productEncoder[A, Repr]
    }
  }

  trait DerivedDecoder[A] {
    def decoder: Decoder[A]
  }

  object DerivedDecoder {
    implicit def derived[A, Repr](
        implicit gen: LabelledGeneric.Aux[A, Repr],
        decoders: Shapeless2DeriveDecoder[Repr]
    ): DerivedDecoder[A] = new DerivedDecoder[A] {
      override def decoder: Decoder[A] = productDecoder[A, Repr]
    }
  }

  def deriveEncoder[A](implicit derivedEncoder: DerivedEncoder[A]): Encoder[A] = derivedEncoder.encoder
  def deriveDecoder[A](implicit derivedDecoder: DerivedDecoder[A]): Decoder[A] = derivedDecoder.decoder
}
