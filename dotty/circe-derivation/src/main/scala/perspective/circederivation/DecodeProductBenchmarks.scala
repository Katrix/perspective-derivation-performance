package perspective.circederivation

import java.util.concurrent.TimeUnit

import org.openjdk.jmh.annotations.*

@BenchmarkMode(Array(Mode.AverageTime))
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Fork(value = 3, jvmArgs = Array("-Xms2G", "-Xmx2G", "-Xss2M"))
@Warmup(iterations = 10, time = 10, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 10, time = 10, timeUnit = TimeUnit.SECONDS)
class DecodeProductBenchmarks {

  @Benchmark
  def decodeCirceGeneric(state: CirceGenericProductDefs.DecodeData): AnyRef =
    CirceGenericProductDefs.decode(state.size, state.getData)

  @Benchmark
  def decodeHandwritten(state: HandwrittenProductDefs.DecodeData): AnyRef =
    HandwrittenProductDefs.decode(state.size, state.getData)

  @Benchmark
  def decodeMagnolia(state: MagnoliaProductDefs.DecodeData): AnyRef =
    MagnoliaProductDefs.decode(state.size, state.getData)

  @Benchmark
  def decodeShapeless2(state: Shapeless2ProductDefs.DecodeData): AnyRef =
    Shapeless2ProductDefs.decode(state.size, state.getData)

  @Benchmark
  def decodeShapeless3(state: Shapeless3ProductDefs.DecodeData): AnyRef =
    Shapeless3ProductDefs.decode(state.size, state.getData)

  @Benchmark
  def decodePerspective(state: PerspectiveProductDefs.DecodeData): AnyRef =
    PerspectiveProductDefs.decode(state.size, state.getData)

  @Benchmark
  def decodePerspectiveFaster(state: PerspectiveFasterProductDefs.DecodeData): AnyRef =
    PerspectiveFasterProductDefs.decode(state.size, state.getData)

  @Benchmark
  def decodePerspectiveInline(state: PerspectiveInlineProductDefs.DecodeData): AnyRef =
    PerspectiveInlineProductDefs.decode(state.size, state.getData)

  @Benchmark
  def decodePerspectiveUnrolling(state: PerspectiveUnrollingProductDefs.DecodeData): AnyRef =
    PerspectiveUnrollingProductDefs.decode(state.size, state.getData)
}
