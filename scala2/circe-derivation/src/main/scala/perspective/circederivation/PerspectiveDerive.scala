package perspective.circederivation

import io.circe._
import perspective._
import perspective.derivation._
import perspective.syntax.all._

object PerspectiveDerive {

  def productDecoder[A, Gen[_[_]]](
      implicit gen: HKDProductGeneric.Aux[A, Gen],
      decoders: Gen[Decoder]
  ): Decoder[A] = new Decoder[A] {

    import gen.implicits._

    private val rep       = representable
    private val names     = gen.names.indexKC
    private val decodersF = decoders.indexKC

    override def apply(cursor: HCursor): Decoder.Result[A] =
      rep.indices
        .traverseIdK[Decoder.Result](Lambda[gen.Index ~>: Decoder.Result] { i =>
          decodersF(i).tryDecode(cursor.downField(names(i)))
        })
        .map(gen.from)
  }

  def productEncoder[A, Gen[_[_]]](
      implicit gen: HKDProductGeneric.Aux[A, Gen],
      encoders: Gen[Encoder]
  ): Encoder[A] = new Encoder[A] {
    import gen.implicits._

    private val rep = representable
    private val names = gen.names.indexKC
    private val encodersF = encoders.indexKC

    override def apply(a: A): Json = {
      val values = rep.indexK(gen.to(a))

      val list = rep.indices.foldLeftKC(Nil: List[(String, Json)]) { acc =>
        Lambda[gen.Index ~>: Const[List[(String, Json)], *]] { i =>
          (names(i), encodersF(i)(values(i))) :: acc
        }
      }

      Json.obj(list: _*)
    }
  }

  trait DerivedEncoder[A] {
    def encoder: Encoder[A]
  }
  object DerivedEncoder {
    implicit def derived[A, Gen[_[_]]](
        implicit gen: HKDProductGeneric.Aux[A, Gen],
        encoders: Gen[Encoder]
    ): DerivedEncoder[A] = new DerivedEncoder[A] {
      override def encoder: Encoder[A] = productEncoder[A, Gen]
    }
  }

  trait DerivedDecoder[A] {
    def decoder: Decoder[A]
  }

  object DerivedDecoder {
    implicit def derived[A, Gen[_[_]]](
        implicit gen: HKDProductGeneric.Aux[A, Gen],
        decoders: Gen[Decoder]
    ): DerivedDecoder[A] = new DerivedDecoder[A] {
      override def decoder: Decoder[A] = productDecoder[A, Gen]
    }
  }

  def deriveEncoder[A](implicit derivedEncoder: DerivedEncoder[A]): Encoder[A] = derivedEncoder.encoder
  def deriveDecoder[A](implicit derivedDecoder: DerivedDecoder[A]): Decoder[A] = derivedDecoder.decoder
}
