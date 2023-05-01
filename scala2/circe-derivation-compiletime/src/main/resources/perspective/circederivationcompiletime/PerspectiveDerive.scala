package perspective.circederivation

import io.circe.Decoder.Result
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
        .traverseIdK[Decoder.Result](new (gen.Index ~>: Decoder.Result) {
          override def apply[Z](i: gen.Index[Z]): Result[Z] = decodersF(i).tryDecode(cursor.downField(names(i)))
        })
        .map(gen.from)
  }

  def productEncoder[A, Gen[_[_]]](
      implicit gen: HKDProductGeneric.Aux[A, Gen],
      encoders: Gen[Encoder]
  ): Encoder[A] = new Encoder[A] {
    import gen.implicits._

    private val rep       = representable
    private val names     = gen.names.indexKC
    private val encodersF = encoders.indexKC

    override def apply(a: A): Json = {
      val values = rep.indexK(gen.to(a))

      val list = rep.indices.foldLeftKC(Nil: List[(String, Json)]) { acc =>
        new (gen.Index ~>: ({type L[X] = List[(String, Json)]})#L) {
          override def apply[Z](i: gen.Index[Z]): Const[List[(String, Json)], Z] =
            (names(i), encodersF(i)(values(i))) :: acc
        }
      }

      Json.obj(list: _*)
    }
  }
}
