package perspective.circederivationcompiletime

import io.circe._
object BenchmarkCaseClass5DerivesCirceGeneric {
  import io.circe.generic.semiauto._

  implicit val encoder: Encoder[BenchmarkCaseClass5] =   deriveEncoder
  implicit val decoder: Decoder[BenchmarkCaseClass5] =   deriveDecoder
}
