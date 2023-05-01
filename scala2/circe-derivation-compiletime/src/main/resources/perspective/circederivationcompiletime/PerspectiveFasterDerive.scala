package perspective.circederivation

import io.circe.Decoder.Result
import io.circe._
import perspective._
import perspective.derivation._
import perspective.syntax.all._

object PerspectiveFasterDerive {

  def productDecoder[A, Gen[_[_]]](
      implicit gen: HKDProductGeneric.Aux[A, Gen],
      decoders: Gen[Decoder]
  ): Decoder[A] = new Decoder[A] {
    import gen.implicits._

    private val names     = gen.names.indexKC
    private val decodersF = decoders.indexKC

    override def apply(cursor: HCursor): Decoder.Result[A] =
      gen
        .tabulateTraverseKEither[DecodingFailure, Id](new (gen.Index ~>: Decoder.Result) {
          override def apply[Z](i: gen.Index[Z]): Result[Z] =
            decodersF(i).tryDecode(cursor.downField(names(i)))
        }) match {
        case Right(value) => Right(gen.from(value))
        case Left(e)      => Left(e)
      }
  }

  def productEncoder[A, Gen[_[_]]](
      implicit gen: HKDProductGeneric.Aux[A, Gen],
      encoders: Gen[Encoder]
  ): Encoder[A] = new Encoder[A] {
    import gen.implicits._

    private val names     = gen.names.indexKC
    private val encodersF = encoders.indexKC

    override def apply(a: A): Json = {
      val list = gen.tabulateFoldLeft(Nil: List[(String, Json)]) { acc =>
        new (gen.Index ~>: ({type L[X] = List[(String, Json)]})#L) {
          override def apply[Z](i: gen.Index[Z]): Const[List[(String, Json)], Z] =
            (names(i), encodersF(i)(gen.productElementId(a, i))) :: acc
        }
      }

      Json.obj(list: _*)
    }
  }

  def sumDecoder[A, Gen[_[_]]](
      implicit gen: HKDSumGeneric.Aux[A, Gen],
      decoders: Gen[Decoder]
  ): Decoder[A] = new Decoder[A] {

    import gen.implicits._

    private val decodersF      = decoders.indexKC
    private val nameToIndexMap = gen.nameToIndexMap

    override def apply(c: HCursor): Decoder.Result[A] = c
      .downField("$type")
      .as[String]
      .flatMap(tpe => nameToIndexMap.get(tpe).toRight(DecodingFailure(s"No type named $tpe found", c.history)))
      .flatMap { i =>
        val decoder = decodersF(i)
        decoder(c).orElse(decoder.tryDecode(c.downField("$value")))
      }
  }

  def sumEncoder[A, Gen[_[_]]](
      implicit gen: HKDSumGeneric.Aux[A, Gen],
      encoders: Gen[Encoder]
  ): Encoder[A] = new Encoder[A] {

    import gen.implicits._

    private val encodersF = encoders.indexKC
    private val namesF    = gen.names.indexKC

    override def apply(a: A): Json = {
      val idx      = gen.indexOf(a)
      val nameJson = Json.fromString(namesF(idx))
      val encoder  = encodersF(idx)
      val json     = encoder(a)

      json.arrayOrObject(
        Json.obj("$type" -> nameJson, "$value" -> json),
        _ => Json.obj("$type" -> nameJson, "$value" -> json),
        obj => Json.fromJsonObject(obj.add("$type", nameJson))
      )
    }
  }
}
