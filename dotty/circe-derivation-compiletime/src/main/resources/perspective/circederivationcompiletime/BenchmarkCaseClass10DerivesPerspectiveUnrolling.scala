package perspective.circederivationcompiletime

import io.circe._
object BenchmarkCaseClass10DerivesPerspectiveUnrolling {
  import perspective.circederivation.PerspectiveUnrollingDerive

  implicit val encoder: Encoder[BenchmarkCaseClass10] =   PerspectiveUnrollingDerive.deriveEncoder
  implicit val decoder: Decoder[BenchmarkCaseClass10] =   PerspectiveUnrollingDerive.deriveDecoder
}
