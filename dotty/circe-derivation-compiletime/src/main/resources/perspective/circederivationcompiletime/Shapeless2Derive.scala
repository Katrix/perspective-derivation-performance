package perspective.circederivation

import scala.compiletime.summonInline

import io.circe.Decoder.Result
import io.circe.{Decoder, DecodingFailure, Encoder, HCursor, Json}
import shapeless2._
import shapeless2.labelled._

object Shapeless2Derive {

  trait Shapeless2DeriveDecoder[A] extends Decoder[A]
  trait Shapeless2DeriveEncoderParts[A] {
    def parts(a: A): List[(String, Json)]
  }

  def productDecoder[A, Repr](
      using gen: LabelledGeneric.Aux[A, Repr],
      hlistDecoder: Shapeless2DeriveDecoder[Repr]
  ): Decoder[A] = (c: HCursor) => hlistDecoder(c).map(gen.from)

  def productEncoder[A, Repr](
      using gen: LabelledGeneric.Aux[A, Repr],
      hlistEncoder: Shapeless2DeriveEncoderParts[Repr]
  ): Encoder[A] = (a: A) => Json.obj(hlistEncoder.parts(gen.to(a)): _*)

  given [H, K <: String, T <: HList](
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

  given [H, K <: String, T <: HList](
      using hEncoder: Encoder[H],
      tEncoder: Shapeless2DeriveEncoderParts[T],
      key: ValueOf[K]
  ): Shapeless2DeriveEncoderParts[FieldType[K, H] :: T] with {
    override def parts(a: FieldType[K, H] :: T): List[(String, Json)] =
      (key.value -> hEncoder(a.head)) :: tEncoder.parts(a.tail)
  }

  given Shapeless2DeriveDecoder[HNil] with {
    override def apply(c: HCursor): Result[HNil] = Right(HNil)
  }

  given Shapeless2DeriveEncoderParts[HNil] with {
    override def parts(a: HNil): List[(String, Json)] = Nil
  }

  inline def deriveDecoder[A](using gen: LabelledGeneric[A]): Decoder[A] =
    val decoder = summonInline[Shapeless2DeriveDecoder[gen.Repr]]
    productDecoder(using gen, decoder)

  inline def deriveEncoder[A](using gen: LabelledGeneric[A]): Encoder[A] =
    val decoder = summonInline[Shapeless2DeriveEncoderParts[gen.Repr]]
    productEncoder(using gen, decoder)
}
