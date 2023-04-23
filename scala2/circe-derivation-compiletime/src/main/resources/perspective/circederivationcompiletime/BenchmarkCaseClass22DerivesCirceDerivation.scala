package perspective.circederivationcompiletime

import io.circe._
object BenchmarkCaseClass22DerivesCirceDerivation {
  import io.circe.derivation._

  implicit val encoder: Encoder[BenchmarkCaseClass22] =   deriveEncoder
  implicit val decoder: Decoder[BenchmarkCaseClass22] =   deriveDecoder
}
