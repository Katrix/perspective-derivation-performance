package perspective.circederivation

import org.openjdk.jmh.annotations.*

import java.util.concurrent.TimeUnit
import io.circe.Json

@BenchmarkMode(Array(Mode.AverageTime))
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Fork(1)
@Warmup(iterations = 3)
@Measurement(iterations = 3)
class EncodeBenchmarks {

  @Benchmark
  def encodeGeneric(state: CirceGenericDefs.EncodeData): Json = {
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
  def encodeShapeless3(state: Shapeless3Defs.EncodeData): Json = {
    val id = state.intId
    Shapeless3Defs.encode(id, state.getData(id))
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

  @Benchmark
  def encodePerspectiveInline(state: PerspectiveInlineDefs.EncodeData): Json = {
    val id = state.intId
    PerspectiveInlineDefs.encode(id, state.getData(id))
  }

  @Benchmark
  def encodePerspectiveUnrolling(state: PerspectiveUnrollingDefs.EncodeData): Json = {
    val id = state.intId
    PerspectiveUnrollingDefs.encode(id, state.getData(id))
  }
}
