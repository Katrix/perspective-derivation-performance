package perspective.circederivation

import org.openjdk.jmh.annotations.*

import java.util.concurrent.TimeUnit

@BenchmarkMode(Array(Mode.AverageTime))
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Fork(value = 3, jvmArgs = Array("-Xms2G", "-Xmx2G", "-Xss2M"))
@Warmup(iterations = 10, time = 10, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 10, time = 10, timeUnit = TimeUnit.SECONDS)
class DecodeSumBenchmarks {

  @Benchmark
  def decodeCirceGeneric(state: CirceGenericSumDefs.DecodeData): AnyRef =
    CirceGenericSumDefs.decode(state.size, state.getData)

  @Benchmark
  def decodeHandwritten(state: HandwrittenSumDefs.DecodeData): AnyRef =
    HandwrittenSumDefs.decode(state.size, state.getData)

  @Benchmark
  def decodeMagnolia(state: MagnoliaSumDefs.DecodeData): AnyRef =
    MagnoliaSumDefs.decode(state.size, state.getData)

  @Benchmark
  def decodeShapeless2(state: Shapeless2SumDefs.DecodeData): AnyRef =
    Shapeless2SumDefs.decode(state.size, state.getData)

  @Benchmark
  def decodeShapeless3(state: Shapeless3SumDefs.DecodeData): AnyRef =
    Shapeless3SumDefs.decode(state.size, state.getData)

  @Benchmark
  def decodePerspectiveFaster(state: PerspectiveFasterSumDefs.DecodeData): AnyRef =
    PerspectiveFasterSumDefs.decode(state.size, state.getData)

  @Benchmark
  def decodePerspectiveInline(state: PerspectiveInlineSumDefs.DecodeData): AnyRef =
    PerspectiveInlineSumDefs.decode(state.size, state.getData)
}
