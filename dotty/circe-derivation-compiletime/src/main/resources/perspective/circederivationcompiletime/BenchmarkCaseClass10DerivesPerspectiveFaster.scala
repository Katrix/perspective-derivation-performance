package perspective.circederivationcompiletime

import io.circe._
object BenchmarkCaseClass10DerivesPerspectiveFaster {
  import perspective.circederivation.PerspectiveFasterDerive

  implicit val encoder: Encoder[BenchmarkCaseClass10] =   PerspectiveFasterDerive.deriveEncoder
  implicit val decoder: Decoder[BenchmarkCaseClass10] =   PerspectiveFasterDerive.deriveDecoder
}
