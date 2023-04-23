package perspective.circederivationcompiletime

import io.circe._
object BenchmarkCaseClass10DerivesCirceDerivation {
  import io.circe.derivation._

  implicit val encoder: Encoder[BenchmarkCaseClass10] =   deriveEncoder
  implicit val decoder: Decoder[BenchmarkCaseClass10] =   deriveDecoder
}
