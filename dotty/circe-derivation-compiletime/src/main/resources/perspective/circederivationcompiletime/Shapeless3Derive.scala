package perspective.circederivation

import io.circe.Decoder.Result
import io.circe.*
import shapeless3.deriving.*

import scala.deriving.Mirror

object Shapeless3Derive {

  def deriveProductDecoder[A](using inst: K0.ProductInstances[Decoder, A], labelling: Labelling[A]): Decoder[A] =
    new Decoder[A] {
      override def apply(c: HCursor): Result[A] = {
        val fieldNames = labelling.elemLabels.iterator
        val ret = inst.unfold[Option[DecodingFailure]](None)(
          [t] =>
            (acc: Option[DecodingFailure], decoder: Decoder[t]) => {
              if acc.isDefined then (acc, None)
              else
                val res = decoder.tryDecode(c.downField(fieldNames.next))
                (res.left.toOption, res.toOption)
          }
        )

        ret match {
          case (Some(e), None) => Left(e)
          case (None, Some(r)) => Right(r)
          case _               => sys.error("impossible")
        }
      }
    }

  def deriveProductEncoder[A](using inst: K0.ProductInstances[Encoder, A], labelling: Labelling[A]): Encoder[A] =
    new Encoder[A] {
      override def apply(a: A): Json = {
        val fieldNames = labelling.elemLabels.iterator
        Json.obj(
          inst.foldLeft[List[(String, Json)]](a)(Nil)(
            [t] =>
              (acc: List[(String, Json)], encoder: Encoder[t], field: t) => (fieldNames.next, encoder(field)) :: acc
          ): _*
        )
      }
    }

  def deriveSumDecoder[A](using inst: K0.CoproductInstances[Decoder, A], labelling: Labelling[A]): Decoder[A] =
    new Decoder[A] {
      override def apply(c: HCursor): Result[A] = c.downField("$type").as[String].flatMap { tpe =>
        val idx = labelling.elemLabels.indexOf(tpe)
        if idx == -1 then Left(DecodingFailure(s"Did not find a type $tpe", c.history))
        else
          inst
            .project[Either[DecodingFailure, Unit]](idx)(Right(()))(
              [t] =>
                (acc: Either[DecodingFailure, Unit], decoder: Decoder[t]) =>
                  val res = decoder(c).orElse(decoder.tryDecode(c.downField("$value")))
                  (res.map(_ => ()), res.toOption)
            ) match
            case (Right(()), Some(v)) => Right(v)
            case (Left(e), None)      => Left(e)
            case _                    => Left(DecodingFailure("Unexpected combination of types", c.history))

      }
    }
    
  def deriveSumEncoder[A](using inst: K0.CoproductInstances[Encoder, A], m: Mirror.SumOf[A], labelling: Labelling[A]): Encoder[A] = new Encoder[A] {
    override def apply(a: A): Json =
      inst.fold(a) { [t] => (enc: Encoder[t], v: t) =>
        val json = enc(v)
        val nameJson = Json.fromString(labelling.elemLabels(m.ordinal(a)))

        json.arrayOrObject(
          Json.obj("$type" -> nameJson, "$value" -> json),
          _ => Json.obj("$type" -> nameJson, "$value" -> json),
          obj => Json.fromJsonObject(obj.add("$type", nameJson))
        )
      }
  }
}
