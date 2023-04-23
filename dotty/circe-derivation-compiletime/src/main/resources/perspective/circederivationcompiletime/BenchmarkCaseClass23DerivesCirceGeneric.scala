package perspective.circederivationcompiletime

import io.circe._
object BenchmarkCaseClass23DerivesCirceGeneric {
  import io.circe.generic.semiauto._

  implicit val encoder: Encoder[BenchmarkCaseClass23] =   deriveEncoder
  implicit val decoder: Decoder[BenchmarkCaseClass23] =   deriveDecoder
}
