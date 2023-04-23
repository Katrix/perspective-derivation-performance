package perspective.circederivationcompiletime

import io.circe._
object BenchmarkCaseClass1DerivesCirceGeneric {
  import io.circe.generic.semiauto._

  implicit val encoder: Encoder[BenchmarkCaseClass1] =   deriveEncoder
  implicit val decoder: Decoder[BenchmarkCaseClass1] =   deriveDecoder
}
