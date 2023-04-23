package perspective.circederivationcompiletime

import io.circe._
object BenchmarkCaseClass1DerivesCirceDerivation {
  import io.circe.derivation._

  implicit val encoder: Encoder[BenchmarkCaseClass1] =   deriveEncoder
  implicit val decoder: Decoder[BenchmarkCaseClass1] =   deriveDecoder
}
