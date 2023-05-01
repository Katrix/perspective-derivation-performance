package perspective.circederivation

import java.util.concurrent.TimeUnit

import io.circe.Json
import org.openjdk.jmh.annotations.*

@BenchmarkMode(Array(Mode.AverageTime))
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Fork(value = 3, jvmArgs = Array("-Xms2G", "-Xmx2G", "-Xss2M"))
@Warmup(iterations = 10, time = 10, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 10, time = 10, timeUnit = TimeUnit.SECONDS)
class EncodeProductBenchmarks {

  @Benchmark
  def encodeGeneric(state: CirceGenericProductDefs.EncodeData): Json =
    CirceGenericProductDefs.encode(state.size, state.getData)

  @Benchmark
  def encodeHandwritten(state: HandwrittenProductDefs.EncodeData): Json =
    HandwrittenProductDefs.encode(state.size, state.getData)

  @Benchmark
  def encodeMagnolia(state: MagnoliaProductDefs.EncodeData): AnyRef =
    MagnoliaProductDefs.encode(state.size, state.getData)

  @Benchmark
  def encodeShapeless2(state: Shapeless2ProductDefs.EncodeData): Json =
    Shapeless2ProductDefs.encode(state.size, state.getData)

  @Benchmark
  def encodeShapeless3(state: Shapeless3ProductDefs.EncodeData): Json =
    Shapeless3ProductDefs.encode(state.size, state.getData)

  @Benchmark
  def encodePerspective(state: PerspectiveProductDefs.EncodeData): Json =
    PerspectiveProductDefs.encode(state.size, state.getData)

  @Benchmark
  def encodePerspectiveFaster(state: PerspectiveFasterProductDefs.EncodeData): Json =
    PerspectiveFasterProductDefs.encode(state.size, state.getData)

  @Benchmark
  def encodePerspectiveInline(state: PerspectiveInlineProductDefs.EncodeData): Json =
    PerspectiveInlineProductDefs.encode(state.size, state.getData)

  @Benchmark
  def encodePerspectiveUnrolling(state: PerspectiveUnrollingProductDefs.EncodeData): Json =
    PerspectiveUnrollingProductDefs.encode(state.size, state.getData)
}
