package perspective.circederivationcompiletime

import java.io.{BufferedWriter, File, FileOutputStream, FileWriter}
import java.nio.file.Paths
import java.text.MessageFormat
import java.util.concurrent.TimeUnit

import scala.io.Source
import scala.jdk.CollectionConverters.*
import scala.util.Using

import dotty.tools.FatalError
import dotty.tools.dotc.*
import dotty.tools.dotc.core.Contexts.Context
import dotty.tools.io.AbstractFile
import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.results.RunResult
import org.openjdk.jmh.results.format.*
import org.openjdk.jmh.runner.Runner
import org.openjdk.jmh.runner.options.OptionsBuilder
import org.openjdk.jmh.runner.options.TimeValue
import reporting.*

@State(Scope.Benchmark)
@BenchmarkMode(Array(Mode.AverageTime))
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 5, time = 10, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 10, timeUnit = TimeUnit.SECONDS)
@Fork(value = 1, jvmArgs = Array("-Xms2G", "-Xmx2G", "-Xss2M"))
//@Warmup(iterations = 10, time = 10, timeUnit = TimeUnit.SECONDS)
//@Measurement(iterations = 10, time = 10, timeUnit = TimeUnit.SECONDS)
//@Fork(value = 3, jvmArgs = Array("-Xms2G", "-Xmx2G", "-Xss2M"))
class CompileBenchmark extends Driver {

  @Param(value = Array(""))
  var extraArgs: String = _

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
    val res = List(
      Option.when(sourceFilesStr != null && sourceFilesStr.nonEmpty)(
        sourceFilesStr.split(File.pathSeparator).map(f => Paths.get(f).toAbsolutePath.normalize.toString).toList
      ),
      Option.when(typeSize != 0 && genType != null && sourceRoot != null && sourcePattern != null)(
        List(
          Paths
            .get(sourceRoot)
            .resolve(MessageFormat.format(sourcePattern, typeSize, genType) + ".scala")
            .toAbsolutePath
            .toString
        )
      )
    ).flatten.flatten.distinct

    if (res.isEmpty) {
      sys.error("Need to specify either sourceFilesStr or typeSize, genType, sourceRoot and sourcePattern")
    }

    res
  }

  def extras: List[String] = if (extraArgs != null && extraArgs != "") extraArgs.split('|').toList else Nil

  def allArgs: List[String] =
    extras
      ++ Option.when(depsClasspath != null && depsClasspath.nonEmpty)(depsClasspath).toList.flatMap(cp => List("-cp", cp))
      ++ List("-d", tempDir.getAbsolutePath)
      ++ sourceFiles

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

  // override to avoid printing summary information
  override def doCompile(compiler: Compiler, files: List[AbstractFile])(implicit ctx: Context): Reporter =
    if (files.nonEmpty)
      try {
        val run = compiler.newRun
        run.compile(files)
        ctx.reporter
      } catch {
        case ex: FatalError =>
          report.error(ex.getMessage) // signals that we should fail compilation.
          ctx.reporter
      }
    else ctx.reporter

  @Benchmark
  def compile(): Unit = {
    val res = process(allArgs.toArray)
    if (res.hasErrors) throw new Exception("compilation failed")
  }
}
object Main {
  def main(args: Array[String]): Unit = {
    val bm = new CompileBenchmark
    bm.depsClasspath =
      """D:\DevProjects\Stable\perspective-derivation-performance\dotty\circe-derivation-compiletime\src\main\resources;C:\Users\katri\AppData\Local\Coursier\Cache\v1\https\repo1.maven.org\maven2\io\circe\circe-core_3\0.14.3\circe-core_3-0.14.3.jar;C:\Users\katri\AppData\Local\Coursier\Cache\v1\https\repo1.maven.org\maven2\io\circe\circe-generic_3\0.14.3\circe-generic_3-0.14.3.jar;C:\Users\katri\.ivy2\local\net.katsstuff\perspective-derivation_3\0.2.0-SNAPSHOT\jars\perspective-derivation_3.jar;C:\Users\katri\AppData\Local\Coursier\Cache\v1\https\repo1.maven.org\maven2\org\typelevel\shapeless3-deriving_3\3.0.1\shapeless3-deriving_3-3.0.1.jar;C:\Users\katri\AppData\Local\Coursier\Cache\v1\https\repo1.maven.org\maven2\io\circe\circe-numbers_3\0.14.3\circe-numbers_3-0.14.3.jar;C:\Users\katri\AppData\Local\Coursier\Cache\v1\https\repo1.maven.org\maven2\org\scala-lang\scala3-library_3\3.2.0\scala3-library_3-3.2.0.jar;C:\Users\katri\AppData\Local\Coursier\Cache\v1\https\repo1.maven.org\maven2\org\typelevel\cats-core_3\2.8.0\cats-core_3-2.8.0.jar;C:\Users\katri\.ivy2\local\net.katsstuff\perspective_3\0.2.0-SNAPSHOT\jars\perspective_3.jar;C:\Users\katri\AppData\Local\Coursier\Cache\v1\https\repo1.maven.org\maven2\org\scala-lang\scala-library\2.13.8\scala-library-2.13.8.jar;C:\Users\katri\AppData\Local\Coursier\Cache\v1\https\repo1.maven.org\maven2\org\typelevel\cats-kernel_3\2.8.0\cats-kernel_3-2.8.0.jar;D:\DevProjects\Stable\perspective-derivation-performance\shapeless2\target\scala-3.2.0\perspective-shapeless2_3-0.1.0.jar"""

    bm.sourceRoot =
      """D:\DevProjects\Stable\perspective-derivation-performance\dotty\circe-derivation-compiletime\src\main\resources\perspective\circederivationcompiletime"""
    bm.typeSize = 50
    bm.genType = "PerspectiveInline"
    bm.sourcePattern = "BenchmarkCaseClass{0}Derives{1}"

    //bm.sourceFilesStr = """D:\DevProjects\Stable\perspective-derivation-performance\dotty\circe-derivation-compiletime\src\main\resources\perspective\circederivationcompiletime\PerspectiveInlineDerive.scala"""

    println("Foo")

    try {
      bm.initTemp()
      println("Bar")
      bm.compile()
      println("Baz")
    } finally {
      bm.clearTemp()
    }

    println("Bin")
  }
}
