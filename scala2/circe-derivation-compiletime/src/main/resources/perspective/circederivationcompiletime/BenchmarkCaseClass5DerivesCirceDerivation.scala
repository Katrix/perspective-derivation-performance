package perspective.circederivationcompiletime

import io.circe._
object BenchmarkCaseClass5DerivesCirceDerivation {
  import io.circe.derivation._

  implicit val encoder: Encoder[BenchmarkCaseClass5] =   deriveEncoder
  implicit val decoder: Decoder[BenchmarkCaseClass5] =   deriveDecoder
}
