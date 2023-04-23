package perspective.circederivationcompiletime

import io.circe._
object BenchmarkCaseClass75DerivesCirceDerivation {
  import io.circe.derivation._

  implicit val encoder: Encoder[BenchmarkCaseClass75] =   deriveEncoder
  implicit val decoder: Decoder[BenchmarkCaseClass75] =   deriveDecoder
}
