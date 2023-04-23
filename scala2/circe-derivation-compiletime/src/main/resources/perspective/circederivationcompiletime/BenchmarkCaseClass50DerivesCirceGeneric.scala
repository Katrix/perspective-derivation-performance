package perspective.circederivationcompiletime

import io.circe._
object BenchmarkCaseClass50DerivesCirceGeneric {
  import io.circe.generic.semiauto._

  implicit val encoder: Encoder[BenchmarkCaseClass50] =   deriveEncoder
  implicit val decoder: Decoder[BenchmarkCaseClass50] =   deriveDecoder
}
