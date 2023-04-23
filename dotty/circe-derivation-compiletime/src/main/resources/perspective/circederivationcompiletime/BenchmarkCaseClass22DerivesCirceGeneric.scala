package perspective.circederivationcompiletime

import io.circe._
object BenchmarkCaseClass22DerivesCirceGeneric {
  import io.circe.generic.semiauto._

  implicit val encoder: Encoder[BenchmarkCaseClass22] =   deriveEncoder
  implicit val decoder: Decoder[BenchmarkCaseClass22] =   deriveDecoder
}
