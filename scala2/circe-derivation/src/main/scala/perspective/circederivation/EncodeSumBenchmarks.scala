package perspective.circederivation

import io.circe.Json
import org.openjdk.jmh.annotations._

import java.util.concurrent.TimeUnit

@BenchmarkMode(Array(Mode.AverageTime))
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Fork(value = 3, jvmArgs = Array("-Xms2G", "-Xmx2G", "-Xss2M"))
@Warmup(iterations = 10, time = 10, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 10, time = 10, timeUnit = TimeUnit.SECONDS)
class EncodeSumBenchmarks {

  @Benchmark
  def encodeCirceDerivation(state: CirceDerivationSumDefs.EncodeData): Json =
    CirceDerivationSumDefs.encode(state.size, state.getData)

  @Benchmark
  def encodeCirceGeneric(state: CirceGenericSumDefs.EncodeData): Json =
    CirceGenericSumDefs.encode(state.size, state.getData)

  @Benchmark
  def encodeHandwritten(state: HandwrittenSumDefs.EncodeData): Json =
    HandwrittenSumDefs.encode(state.size, state.getData)

  @Benchmark
  def encodeMagnolia(state: MagnoliaSumDefs.EncodeData): AnyRef =
    MagnoliaSumDefs.encode(state.size, state.getData)

  @Benchmark
  def encodeShapeless2(state: Shapeless2SumDefs.EncodeData): Json =
    Shapeless2SumDefs.encode(state.size, state.getData)

  @Benchmark
  def encodePerspectiveFaster(state: PerspectiveFasterSumDefs.EncodeData): Json =
    PerspectiveFasterSumDefs.encode(state.size, state.getData)
}
