package perspective.circederivation

import org.openjdk.jmh.annotations._

import java.util.concurrent.TimeUnit

@BenchmarkMode(Array(Mode.AverageTime))
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Fork(value = 3, jvmArgs = Array("-Xms2G", "-Xmx2G", "-Xss2M"))
@Warmup(iterations = 10, time = 10, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 10, time = 10, timeUnit = TimeUnit.SECONDS)
class DecodeBenchmarks {

  @Benchmark
  def decodeCirceDerivation(state: CirceDerivationDefs.DecodeData): AnyRef = {
    val id = state.intId
    CirceDerivationDefs.decode(id, state.getData(id))
  }

  @Benchmark
  def decodeCirceGeneric(state: CirceGenericDefs.DecodeData): AnyRef = {
    val id = state.intId
    CirceGenericDefs.decode(id, state.getData(id))
  }

  @Benchmark
  def decodeHandwritten(state: HandwrittenDefs.DecodeData): AnyRef = {
    val id = state.intId
    HandwrittenDefs.decode(id, state.getData(id))
  }

  @Benchmark
  def decodeShapeless2(state: Shapeless2Defs.DecodeData): AnyRef = {
    val id = state.intId
    Shapeless2Defs.decode(id, state.getData(id))
  }

  @Benchmark
  def decodePerspective(state: PerspectiveDefs.DecodeData): AnyRef = {
    val id = state.intId
    PerspectiveDefs.decode(id, state.getData(id))
  }

  @Benchmark
  def decodePerspectiveFaster(state: PerspectiveFasterDefs.DecodeData): AnyRef = {
    val id = state.intId
    PerspectiveFasterDefs.decode(id, state.getData(id))
  }
}
