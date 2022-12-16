package perspective.circederivation

import io.circe.Json
import org.openjdk.jmh.annotations._

import java.util.concurrent.TimeUnit

@BenchmarkMode(Array(Mode.AverageTime))
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Fork(1)
@Warmup(iterations = 3)
@Measurement(iterations = 5)
class EncodeBenchmarks {

  @Benchmark
  def encodeCirceDerivation(state: CirceDerivationDefs.EncodeData): Json = {
    val id = state.intId
    CirceDerivationDefs.encode(id, state.getData(id))
  }

  @Benchmark
  def encodeCirceGeneric(state: CirceGenericDefs.EncodeData): Json = {
    val id = state.intId
    CirceGenericDefs.encode(id, state.getData(id))
  }

  @Benchmark
  def encodeHandwritten(state: HandwrittenDefs.EncodeData): Json = {
    val id = state.intId
    HandwrittenDefs.encode(id, state.getData(id))
  }

  @Benchmark
  def encodeShapeless2(state: Shapeless2Defs.EncodeData): Json = {
    val id = state.intId
    Shapeless2Defs.encode(id, state.getData(id))
  }

  @Benchmark
  def encodePerspective(state: PerspectiveDefs.EncodeData): Json = {
    val id = state.intId
    PerspectiveDefs.encode(id, state.getData(id))
  }

  @Benchmark
  def encodePerspectiveFaster(state: PerspectiveFasterDefs.EncodeData): Json = {
    val id = state.intId
    PerspectiveFasterDefs.encode(id, state.getData(id))
  }
}
