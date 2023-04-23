package perspective.circederivationcompiletime

import io.circe._
object BenchmarkCaseClass10DerivesPerspective {
  import perspective.circederivation.PerspectiveDerive

  implicit val encoder: Encoder[BenchmarkCaseClass10] =   PerspectiveDerive.deriveEncoder
  implicit val decoder: Decoder[BenchmarkCaseClass10] =   PerspectiveDerive.deriveDecoder
}
