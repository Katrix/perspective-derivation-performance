package perspective.circederivationcompiletime

import io.circe._
object BenchmarkCaseClass75DerivesCirceGeneric {
  import io.circe.generic.semiauto._

  implicit val encoder: Encoder[BenchmarkCaseClass75] =   deriveEncoder
  implicit val decoder: Decoder[BenchmarkCaseClass75] =   deriveDecoder
}
