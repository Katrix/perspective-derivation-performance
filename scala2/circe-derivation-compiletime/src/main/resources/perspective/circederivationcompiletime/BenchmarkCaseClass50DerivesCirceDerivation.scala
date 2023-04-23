package perspective.circederivationcompiletime

import io.circe._
object BenchmarkCaseClass50DerivesCirceDerivation {
  import io.circe.derivation._

  implicit val encoder: Encoder[BenchmarkCaseClass50] =   deriveEncoder
  implicit val decoder: Decoder[BenchmarkCaseClass50] =   deriveDecoder
}
