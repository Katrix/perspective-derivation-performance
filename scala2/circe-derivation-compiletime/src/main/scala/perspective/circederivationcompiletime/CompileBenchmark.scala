package perspective.circederivationcompiletime

import java.io.File
import java.nio.file.{Files, Paths}
import java.text.MessageFormat
import java.util.concurrent.TimeUnit

import org.openjdk.jmh.annotations._
import org.openjdk.jmh.profile.AsyncProfiler

trait BaseBenchmarkDriver {
  def extraArgs: String
  def extras: List[String]  = if (extraArgs != null && extraArgs != "") extraArgs.split('|').toList else Nil
  def allArgs: List[String] = extras ++ sourceFiles
  def depsClasspath: String
  def tempDir: File
  def sourceFiles: List[String]
  def isResident: Boolean = false
  def compileProfiled(): Unit = {
    val profiler: AsyncProfiler.JavaApi =
      try {
        AsyncProfiler.JavaApi.getInstance()
      } catch {
        case _: LinkageError => null
      }
    if (profiler != null) profiler.filterThread(Thread.currentThread(), true)
    try {
      compileImpl()
    } finally {
      if (profiler != null) profiler.filterThread(Thread.currentThread(), false)
    }
  }
  def compileImpl(): Unit
}

@State(Scope.Benchmark)
@BenchmarkMode(Array(Mode.AverageTime))
@OutputTimeUnit(TimeUnit.MILLISECONDS)
//@Warmup(iterations = 1, time = 10, timeUnit = TimeUnit.SECONDS)
//@Measurement(iterations = 1, time = 10, timeUnit = TimeUnit.SECONDS)
//@Fork(value = 1, jvmArgs = Array("-Xms2G", "-Xmx2G", "-Xss2M"))
@Warmup(iterations = 10, time = 10, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 10, time = 10, timeUnit = TimeUnit.SECONDS)
@Fork(value = 3, jvmArgs = Array("-Xms2G", "-Xmx2G", "-Xss2M"))
class CompileBenchmark extends BenchmarkDriver {
  @Param(value = Array(""))
  var extraArgs: String = _

  @Param(value = Array("false"))
  var resident: Boolean = false

  override def isResident = resident

  @Param(Array(""))
  var depsClasspath: String = _

  @Param(Array(""))
  var sourceFilesStr: String = _

  @Param(Array())
  var sourceRoot: String = _

  @Param(Array())
  var typeSize: Int = _

  @Param(Array())
  var genType: String = _

  @Param(Array())
  var sourcePattern: String = _

  lazy val sourceFiles: List[String] = {
    if (sourceFilesStr.nonEmpty)
      sourceFilesStr.split(File.pathSeparator).map(f => Paths.get(f).toAbsolutePath.normalize.toString).toList
    else if (typeSize != 0 && genType != null && sourceRoot != null) {
      List(
        Paths
          .get(sourceRoot)
          .resolve(MessageFormat.format(sourcePattern, typeSize, genType) + ".scala")
          .toAbsolutePath
          .toString
      )
    } else {
      sys.error("Need to specify either sourceFilesStr or typeSize, genType, sourceRoot and sourcePattern")
    }
  }

  var tempDir: File = null

  // Executed once per fork
  @Setup(Level.Trial) def initTemp(): Unit = {
    val tempRootPath = Paths.get("compileOutput")
    tempRootPath.toFile.mkdirs()
    val tempFile = File.createTempFile("output", "", tempRootPath.toFile)
    tempFile.delete()
    tempFile.mkdir()
    tempDir = tempFile
  }

  @TearDown(Level.Trial) def clearTemp(): Unit =
    BenchmarkUtils.deleteRecursive(tempDir.toPath)

  @Benchmark
  def compile(): Unit = compileProfiled()
}
