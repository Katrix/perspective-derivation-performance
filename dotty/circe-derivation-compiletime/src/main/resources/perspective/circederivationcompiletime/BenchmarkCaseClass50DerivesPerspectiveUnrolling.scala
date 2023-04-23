package perspective.circederivationcompiletime

import io.circe._
object BenchmarkCaseClass50DerivesPerspectiveUnrolling {
  import perspective.circederivation.PerspectiveUnrollingDerive

  implicit val encoder: Encoder[BenchmarkCaseClass50] =   PerspectiveUnrollingDerive.deriveEncoder
  implicit val decoder: Decoder[BenchmarkCaseClass50] =   PerspectiveUnrollingDerive.deriveDecoder
}
