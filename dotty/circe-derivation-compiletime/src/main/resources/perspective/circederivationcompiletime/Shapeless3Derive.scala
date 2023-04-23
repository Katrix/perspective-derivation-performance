package perspective.circederivation

import io.circe.Decoder.Result
import io.circe.{Decoder, DecodingFailure, Encoder, HCursor, Json}
import shapeless3.deriving.*

object Shapeless3Derive {

  def deriveDecoder[A](using inst: K0.ProductInstances[Decoder, A], labelling: Labelling[A]): Decoder[A] = new Decoder[A] {
    override def apply(c: HCursor): Result[A] = {
      val fieldNames = labelling.elemLabels.iterator
      val ret = inst.unfold[Option[DecodingFailure]](None)([t] => (acc: Option[DecodingFailure], decoder: Decoder[t]) => {
        if acc.isDefined then (acc, None)
        else
          val res = decoder.tryDecode(c.downField(fieldNames.next))
          (res.left.toOption, res.toOption)
      })

      ret match {
        case (Some(e), None) => Left(e)
        case (None, Some(r)) => Right(r)
        case _ => sys.error("impossible")
      }
    }
  }

  def deriveEncoder[A](using inst: K0.ProductInstances[Encoder, A], labelling: Labelling[A]): Encoder[A] = new Encoder[A] {
    override def apply(a: A): Json = {
      val fieldNames = labelling.elemLabels.iterator
      Json.obj(inst.foldLeft[List[(String, Json)]](a)(Nil)([t] => (acc: List[(String, Json)], encoder: Encoder[t], field: t) => (fieldNames.next, encoder(field)) :: acc): _*)
    }
  }
}
