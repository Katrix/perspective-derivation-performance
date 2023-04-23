package perspective.circederivationcompiletime

import io.circe._
object BenchmarkCaseClass23DerivesCirceDerivation {
  import io.circe.derivation._

  implicit val encoder: Encoder[BenchmarkCaseClass23] =   deriveEncoder
  implicit val decoder: Decoder[BenchmarkCaseClass23] =   deriveDecoder
}
