package perspective.circederivationcompiletime

import io.circe._
object BenchmarkCaseClass10DerivesCirceGeneric {
  import io.circe.generic.semiauto._

  implicit val encoder: Encoder[BenchmarkCaseClass10] =   deriveEncoder
  implicit val decoder: Decoder[BenchmarkCaseClass10] =   deriveDecoder
}
