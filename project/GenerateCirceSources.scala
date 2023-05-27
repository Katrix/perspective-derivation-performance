import java.io.File
import java.nio.file.{Files, Path}

import scala.jdk.CollectionConverters.*

object GenerateCirceSources {

  sealed trait ADT {
    def size: Int
  }
  case class CaseClass(tpeName: String, fields: Seq[(String, String)], extend: Option[String]) extends ADT {
    override def size: Int = fields.length

    def construct(mkProductField: (String, String) => String): String =
      s"$tpeName(${fields.map(mkProductField.tupled).mkString(", ")})"

    def constructRandom(rand: String): String = construct((_, t) => s"genField[$t]($rand)")
  }

  case class SealedHierarchy(tpeName: String, cases: Seq[CaseClass]) extends ADT {
    override def size: Int = cases.length

    def ordinals: Seq[Int] = Seq.tabulate(cases.length)(identity)

    def construct(ordinal: Int, mkProductField: (String, String) => String): String = {
      val cc = cases(ordinal)
      cc.copy(tpeName = s"$tpeName.${cc.tpeName}").construct(mkProductField)
    }

    def constructRandom(ordinal: Int, rand: String): String = {
      val cc = cases(ordinal)
      cc.copy(tpeName = s"$tpeName.${cc.tpeName}").constructRandom(rand)
    }
  }

  sealed abstract class GenType(val name: String) {
    def imports: String = this match {
      case GenType.CirceDerivation => "import io.circe.derivation._"
      case GenType.CirceGeneric    => "import io.circe.generic.semiauto._"

      case GenType.Handwritten => "import io.circe.syntax._"
      case GenType.Shapeless2  => "import perspective.circederivation.Shapeless2Derive"
      case GenType.Shapeless3  => "import perspective.circederivation.Shapeless3Derive"

      case GenType.Magnolia => "import perspective.circederivation.MagnoliaDerive"

      case GenType.Perspective          => "import perspective.circederivation.PerspectiveDerive"
      case GenType.PerspectiveFaster    => "import perspective.circederivation.PerspectiveFasterDerive"
      case GenType.PerspectiveInline    => "import perspective.circederivation.PerspectiveInlineDerive"
      case GenType.PerspectiveUnrolling => "import perspective.circederivation.PerspectiveUnrollingDerive"
    }

    def encoderType: String = this match {
      case GenType.CirceDerivation => "Encoder.AsObject"
      case _                       => "Encoder"
    }

    def deriveProductEncoder(caseClass: CaseClass): String = {
      import caseClass._
      this match {
        case GenType.CirceDerivation => "deriveEncoder"
        case GenType.CirceGeneric    => "deriveEncoder"

        case GenType.Handwritten =>
          s"""|new Encoder[$tpeName] {
              |  override def apply(a: $tpeName): Json = {
              |    Json.obj(
              |      ${fields.map(t => s""""${t._1}" := a.${t._1}.asJson""").mkString(", ")}
              |    )
              |  }
              |}
              |""".stripMargin
        case GenType.Shapeless2 => s"Shapeless2Derive.productEncoder"
        case GenType.Shapeless3 => s"Shapeless3Derive.deriveProductEncoder"

        case GenType.Magnolia => s"MagnoliaDerive.DeriveEncoder.derived"

        case GenType.Perspective          => s"PerspectiveDerive.productEncoder"
        case GenType.PerspectiveFaster    => s"PerspectiveFasterDerive.productEncoder"
        case GenType.PerspectiveInline    => s"PerspectiveInlineDerive.productEncoder"
        case GenType.PerspectiveUnrolling => s"PerspectiveUnrollingDerive.productEncoder"
      }
    }

    def deriveProductDecoder(caseClass: CaseClass): String = {
      import caseClass._
      this match {
        case GenType.CirceDerivation => "deriveDecoder"
        case GenType.CirceGeneric    => "deriveDecoder"

        case GenType.Handwritten =>
          s"""|new Decoder[$tpeName] {
              |  override def apply(cursor: HCursor): Decoder.Result[$tpeName] = {
              |    for {
              |      ${indent(fields.map(t => s"""${t._1} <- cursor.get[${t._2}]("${t._1}")""").mkString("\n"), 3)}
              |    } yield $tpeName(${fields.map(_._1).mkString(", ")})
              |  }
              |}
              |""".stripMargin
        case GenType.Shapeless2 => s"Shapeless2Derive.adtDecoder"
        case GenType.Shapeless3 => s"Shapeless3Derive.deriveProductDecoder"

        case GenType.Magnolia => "MagnoliaDerive.DeriveDecoder.derived"

        case GenType.Perspective          => s"PerspectiveDerive.productDecoder"
        case GenType.PerspectiveFaster    => s"PerspectiveFasterDerive.productDecoder"
        case GenType.PerspectiveInline    => s"PerspectiveInlineDerive.productDecoder"
        case GenType.PerspectiveUnrolling => s"PerspectiveUnrollingDerive.productDecoder"
      }
    }

    def deriveSumEncoder(sealedHierarchy: SealedHierarchy): String = {
      import sealedHierarchy._
      this match {
        case GenType.CirceDerivation => "deriveEncoder"
        case GenType.CirceGeneric    => "deriveEncoder"

        case GenType.Handwritten =>
          def sealedCase(c: CaseClass): String =
            s"""case f: $tpeName.${c.tpeName} => mergeInType(f.asJson, "${c.tpeName}")"""

          s"""|new Encoder[$tpeName] {
              |  def mergeInType(json: Json, tpe: String): Json = {
              |    val nameJson = Json.fromString(tpe)
              |    json.arrayOrObject(
              |      Json.obj("$$type" -> nameJson, "$$value" -> json),
              |      _ => Json.obj("$$type" -> nameJson, "$$value" -> json),
              |      obj => Json.fromJsonObject(obj.add("$$type", nameJson))
              |    )
              |  }
              |
              |  override def apply(a: $tpeName): Json = a match {
              |    ${indent(sealedHierarchy.cases.map(sealedCase).mkString("\n"), 2)}
              |  }
              |}
              |""".stripMargin
        case GenType.Shapeless2 => s"Shapeless2Derive.sumEncoder"
        case GenType.Shapeless3 => s"Shapeless3Derive.deriveSumEncoder"

        case GenType.Magnolia => s"MagnoliaDerive.DeriveEncoder.derived"

        case GenType.Perspective          => s"PerspectiveDerive.sumEncoder"
        case GenType.PerspectiveFaster    => s"PerspectiveFasterDerive.sumEncoder"
        case GenType.PerspectiveInline    => s"PerspectiveInlineDerive.sumEncoder"
        case GenType.PerspectiveUnrolling => s"PerspectiveUnrollingDerive.sumEncoder"
      }
    }

    def deriveSumDecoder(sealedHierarchy: SealedHierarchy): String = {
      import sealedHierarchy._
      this match {
        case GenType.CirceDerivation => "deriveDecoder"
        case GenType.CirceGeneric    => "deriveDecoder"

        case GenType.Handwritten =>
          def sealedCase(c: CaseClass): String =
            s"""case "${c.tpeName}" => c.as[${c.tpeName}].orElse(c.get[${c.tpeName}]("$$value")) """

          s"""|new Decoder[$tpeName] {
              |  override def apply(c: HCursor): Decoder.Result[$tpeName] = c.downField("$$type").as[String].flatMap {
              |    ${indent(sealedHierarchy.cases.map(sealedCase).mkString("\n"), 2)}
              |    case tpe => Left(DecodingFailure(s"Found no type named $$tpe", c.history))
              |  }
              |}
              |""".stripMargin
        case GenType.Shapeless2 => s"Shapeless2Derive.adtDecoder"
        case GenType.Shapeless3 => s"Shapeless3Derive.deriveSumDecoder"

        case GenType.Magnolia => "MagnoliaDerive.DeriveDecoder.derived"

        case GenType.Perspective          => s"PerspectiveDerive.sumDecoder"
        case GenType.PerspectiveFaster    => s"PerspectiveFasterDerive.sumDecoder"
        case GenType.PerspectiveInline    => s"PerspectiveInlineDerive.sumDecoder"
        case GenType.PerspectiveUnrolling => s"PerspectiveUnrollingDerive.sumDecoder"
      }
    }
  }
  object GenType {
    case object CirceDerivation extends GenType("circeDerivation")
    case object CirceGeneric    extends GenType("circeGeneric")

    case object Handwritten extends GenType("handwritten")
    case object Shapeless2  extends GenType("shapeless2")
    case object Shapeless3  extends GenType("shapeless3")

    case object Magnolia extends GenType("magnolia")

    case object Perspective          extends GenType("perspective")
    case object PerspectiveFaster    extends GenType("perspectiveFaster")
    case object PerspectiveInline    extends GenType("perspectiveInline")
    case object PerspectiveUnrolling extends GenType("perspectiveUnrolling")

    def productValues(isScala3: Boolean): Seq[GenType] = {
      val global = Seq(CirceGeneric, Handwritten, Magnolia, Shapeless2, Perspective, PerspectiveFaster)

      if (isScala3) global ++ Seq(Shapeless3, PerspectiveInline, PerspectiveUnrolling)
      else global ++ Seq(CirceDerivation)
    }

    def sumValues(isScala3: Boolean): Seq[GenType] =
      productValues(isScala3).filter(tpe => tpe != Perspective && tpe != PerspectiveUnrolling)
  }

  def indent(string: String, i: Int = 1): String =
    string.linesIterator.mkString("\n" + "  " * i)

  def generateProductDerives(caseClass: CaseClass, objName: String, genTpe: GenType): String =
    s"""|object $objName {
        |  ${genTpe.imports}
        |
        |  implicit val encoder: ${genTpe.encoderType}[${caseClass.tpeName}] = ${indent(
         genTpe.deriveProductEncoder(caseClass)
       )}
        |  implicit val decoder: Decoder[${caseClass.tpeName}] = ${indent(genTpe.deriveProductDecoder(caseClass))}
        |}""".stripMargin

  def generateCaseClass(caseClass: CaseClass): String =
    s"case class ${caseClass.tpeName}(${caseClass.fields.map(t => s"${t._1}: ${t._2}").mkString(", ")})${caseClass.extend
        .fold("")(e => s" extends $e")}"

  def generateCaseClassAndDerives(genTpe: GenType, caseClass: CaseClass): String =
    s"""|${generateCaseClass(caseClass)}
        |${generateProductDerives(caseClass, caseClass.tpeName, genTpe)}""".stripMargin

  def generateSealedHierarchyAndDerives(genType: GenType, hierarchy: SealedHierarchy): String =
    s"""|sealed trait ${hierarchy.tpeName}
        |object ${hierarchy.tpeName} {
        |  ${genType.imports}
        |  
        |  ${indent(hierarchy.cases.map(generateCaseClassAndDerives(genType, _)).mkString("\n\n"))}
        |  
        |  implicit val encoder: Encoder[${hierarchy.tpeName}] = ${indent(genType.deriveSumEncoder(hierarchy))}
        |  implicit val decoder: Decoder[${hierarchy.tpeName}] = ${indent(genType.deriveSumDecoder(hierarchy))}
        |}""".stripMargin

  def compile(
      path: Path,
      outputPath: Path,
      classPath: Seq[File],
      isScala3: Boolean,
      compilers: xsbti.compile.Compilers
  ): Unit = {
    val scalaInstance = compilers.scalac().scalaInstance()

    val compilerJar = scalaInstance.compilerJars()(0)

    val args = if (sys.props("os.name").toLowerCase.contains("win")) {
      List("cmd", "/C")
    } else {
      List("bash", "-c")
    }

    println(s"Compiling $path")

    sys.process
      .Process(
        args ++ List(
          "java",
          "-Xss8m",
          "-cp",
          scalaInstance.allJars().toSeq.map(_.getAbsoluteFile).mkString(File.pathSeparator),
          if (isScala3) "dotty.tools.dotc.Main" else "scala.tools.nsc.Main",
          path.toAbsolutePath.toString
        ),
        outputPath.toFile,
		"CLASSPATH" -> classPath.map(_.getAbsoluteFile).mkString(File.pathSeparator)
      )
      .!!
  }

  def generateCompiletimeFiles(
      rootPath: Path,
      packageStr: Seq[String],
      caseClasses: Seq[CaseClass],
      isScala3: Boolean,
      compileCp: Seq[File],
      compileFiles: Seq[String],
      compiler: xsbti.compile.Compilers
  ): Unit = {
    val packagePath = packageStr.foldLeft(rootPath)(_.resolve(_))
    val packageLoc  = packageStr.mkString(".")

    compileFiles.foreach(f => compile(packagePath.resolve(f), rootPath, compileCp, isScala3, compiler))

    caseClasses.foreach { caseClass =>
      Files.createDirectories(packagePath)
      val caseClassPath = packagePath.resolve(caseClass.tpeName + ".scala")
      Files.write(
        caseClassPath,
        s"""|package $packageLoc
            |
            |import io.circe._
            |${generateCaseClass(caseClass)}""".stripMargin.linesIterator.toSeq.asJava
      )
      compile(caseClassPath, rootPath, compileCp, isScala3, compiler)

      GenType.productValues(isScala3).foreach { genTpe =>
        val objName = caseClass.tpeName + "Derives" + genTpe.name.capitalize

        Files.write(
          packagePath.resolve(objName + ".scala"),
          s"""|package $packageLoc
              |
              |import io.circe._
              |${generateProductDerives(caseClass, objName, genTpe)}""".stripMargin.linesIterator.toSeq.asJava
        )
      }
    }
  }

  def generateRuntimeFiles(
      rootPath: Path,
      packageStr: Seq[String],
      caseClasses: Seq[CaseClass],
      sealedHierarchies: Seq[SealedHierarchy],
      isScala3: Boolean
  ): Unit = {
    generateProductRuntimeFiles(rootPath, packageStr, caseClasses, isScala3)
    generateSumRuntimeFiles(rootPath, packageStr, sealedHierarchies, isScala3)
  }

  def generateProductRuntimeFiles(
      rootPath: Path,
      packageStr: Seq[String],
      caseClasses: Seq[CaseClass],
      isScala3: Boolean
  ): Unit = GenType.productValues(isScala3).foreach { genTpe =>
    val packagePath = packageStr.foldLeft(rootPath)(_.resolve(_))
    val packageLoc  = packageStr.mkString(".")

    def adtLines(mkLine: CaseClass => String, i: Int = 0): String =
      indent(caseClasses.map(mkLine).mkString("\n"), i + 1)

    def quote(s: Any): String = s""""$s""""

    Files.createDirectories(packagePath)
    Files.write(
      packagePath.resolve(genTpe.name.capitalize + "ProductDefs.scala"),
      s"""|package $packageLoc
          |
          |import io.circe._
          |import io.circe.syntax._
          |import org.openjdk.jmh.annotations._
          |import scala.annotation.switch
          |import scala.util.Random
          |
          |object ${genTpe.name.capitalize}ProductDefs extends BenchmarkDefs {
          |
          |  ${adtLines(generateCaseClassAndDerives(genTpe, _))}
          |
          |  def encode(size: Int, obj: AnyRef): Json = (size: @switch) match {
          |    ${adtLines(c => s"case ${c.size} => obj.asInstanceOf[${c.tpeName}].asJson", i = 1)}
          |  }
          |
          |  def decode(size: Int, json: Json): AnyRef = (size: @switch) match {
          |    ${adtLines(c => s"case ${c.size} => json.as[${c.tpeName}]", i = 1)}
          |  }
          |
          |  @State(Scope.Benchmark)
          |  class EncodeData {
          |    @Param(Array("1234"))
          |    var randSeed: Int = _
          |
          |    ${adtLines(c => s"var v${c.tpeName}: ${c.tpeName} = null", i = 1)}
          |    
          |    @Param(Array(${caseClasses.map(c => quote(c.size)).mkString(", ")}))
          |    var size = 0
          |
          |    def getData: AnyRef = (size: @switch) match {
          |      ${adtLines(c => s"case ${c.size} => v${c.tpeName}", i = 2)}
          |    }
          |    
          |    @Setup
          |    def setup(): Unit = {
          |      val rand: Random = new Random(randSeed)
          |      ${adtLines(c => s"v${c.tpeName} = ${c.constructRandom("rand")}", i = 2)}
          |    }
          |  }
          |  
          |  @State(Scope.Benchmark)
          |  class DecodeData {
          |    @Param(Array("1234"))
          |    var randSeed: Int = _
          |
          |    ${adtLines(c => s"var s${c.tpeName}: Json = null", i = 1)}
          |    
          |    @Param(Array(${caseClasses.map(c => quote(c.size)).mkString(", ")}))
          |    var size = 0
          |
          |    def getData: Json = (size: @switch) match {
          |      ${adtLines(c => s"case ${c.size} => s${c.tpeName}", i = 2)}
          |    }
          |    
          |    @Setup
          |    def setup(): Unit = {
          |      val rand: Random = new Random(randSeed)
          |      ${adtLines(c => s"s${c.tpeName} = ${c.constructRandom("rand")}.asJson", i = 2)}
          |    }
          |  }
          |}""".stripMargin.linesIterator.toSeq.asJava
    )
  }

  def generateSumRuntimeFiles(
      rootPath: Path,
      packageStr: Seq[String],
      sealedHierarchies: Seq[SealedHierarchy],
      isScala3: Boolean
  ): Unit = GenType.sumValues(isScala3).foreach { genTpe =>
    val packagePath = packageStr.foldLeft(rootPath)(_.resolve(_))
    val packageLoc  = packageStr.mkString(".")

    def adtLines(mkLine: SealedHierarchy => String, i: Int = 0): String =
      indent(sealedHierarchies.map(mkLine).mkString("\n"), i + 1)

    def quote(s: Any): String = s""""$s""""

    Files.createDirectories(packagePath)
    Files.write(
      packagePath.resolve(genTpe.name.capitalize + "SumDefs.scala"),
      s"""|package $packageLoc
          |
          |import io.circe._
          |import io.circe.syntax._
          |import org.openjdk.jmh.annotations._
          |import scala.annotation.switch
          |import scala.collection.immutable.ArraySeq
          |import scala.util.Random
          |
          |object ${genTpe.name.capitalize}SumDefs extends BenchmarkDefs {
          |
          |  ${adtLines(generateSealedHierarchyAndDerives(genTpe, _))}
          |
          |  def encode(size: Int, obj: AnyRef): Json = (size: @switch) match {
          |    ${adtLines(c => s"case ${c.size} => obj.asInstanceOf[${c.tpeName}].asJson", i = 1)}
          |  }
          |
          |  def decode(size: Int, json: Json): AnyRef = (size: @switch) match {
          |    ${adtLines(c => s"case ${c.size} => json.as[${c.tpeName}]", i = 1)}
          |  }
          |  
          |  @State(Scope.Benchmark)
          |  class EncodeData {
          |    @Param(Array("1234"))
          |    var randSeed: Int = _
          |
          |    ${adtLines(c => s"var v${c.tpeName}: ArraySeq[${c.tpeName}] = null", i = 1)}
          |    
          |    @Param(Array(${sealedHierarchies.map(c => quote(c.size)).mkString(", ")}))
          |    var size = 0
          |    
          |    @Param(Array("FIRST", "MIDDLE", "LAST"))
          |    var ordinalLoc: String = _
          |    
          |    def ordinal: Int = ordinalLoc match {
          |      case "FIRST"  => 0
          |      case "MIDDLE" => size / 2
          |      case "LAST"   => size - 1
          |    }
          |
          |    def getData: AnyRef = (size: @switch) match {
          |      ${adtLines(c => s"case ${c.size} => v${c.tpeName}(ordinal)", i = 2)}
          |    }
          |    
          |    @Setup
          |    def setup(): Unit = {
          |      val rand: Random = new Random(randSeed)
          |      ${adtLines(
           s => s"v${s.tpeName} = ArraySeq(${s.ordinals.map(i => s.constructRandom(i, "rand")).mkString(", ")})",
           2
         )}
          |    }
          |  }
          |  
          |  @State(Scope.Benchmark)
          |  class DecodeData {
          |    @Param(Array("1234"))
          |    var randSeed: Int = _
          |
          |    ${adtLines(c => s"var s${c.tpeName}: ArraySeq[Json] = null", i = 1)}
          |    
          |    @Param(Array(${sealedHierarchies.map(c => quote(c.size)).mkString(", ")}))
          |    var size = 0
          |    
          |    @Param(Array("FIRST", "MIDDLE", "LAST"))
          |    var ordinalLoc: String = _
          |    
          |    def ordinal: Int = ordinalLoc match {
          |      case "FIRST"  => 0
          |      case "MIDDLE" => size / 2
          |      case "LAST"   => size - 1
          |    }
          |
          |    def getData: Json = (size: @switch) match {
          |      ${adtLines(c => s"case ${c.size} => s${c.tpeName}(ordinal)", i = 2)}
          |    }
          |    
          |    @Setup
          |    def setup(): Unit = {
          |      val rand: Random = new Random(randSeed)
          |      ${adtLines(
           s =>
             s"s${s.tpeName} = ArraySeq(${s.ordinals.map(i => s"${s.constructRandom(i, "rand")}.asJson").mkString(", ")})",
           2
         )}
          |    }
          |  }
          |
          |}""".stripMargin.linesIterator.toSeq.asJava
    )
  }

  val typeSizes: Seq[Int]     = Seq(1, 5, 10, 22, 23, 50, 75)
  val fieldTypes: Seq[String] = Seq("Int", "String", "Double", "Boolean", "Json")

  lazy val circeDerivationCaseClasses: Seq[CaseClass] = typeSizes.map { size =>
    CaseClass(s"BenchmarkCaseClass$size", Seq.tabulate(size)(i => (s"f$i", fieldTypes(i % fieldTypes.length))), None)
  }

  lazy val circeDerivationSealedHierarchies: Seq[SealedHierarchy] = typeSizes.map { size =>
    SealedHierarchy(
      s"BenchmarkSealedHierarchy$size",
      Seq.tabulate(size) { i =>
        CaseClass(
          s"HierarchyCase$i",
          Seq(("f", fieldTypes(i % fieldTypes.length))),
          Some(s"BenchmarkSealedHierarchy$size")
        )
      }
    )
  }

  val CompiletimeBenchmark = sbt.config("compiletimeBenchmark").hide

  def resolveCompiletimeBenchmarkClasspath = sbt.Def.task {
    sbt.Classpaths.managedJars(
      CompiletimeBenchmark,
      (CompiletimeBenchmark / sbt.Keys.classpathTypes).value,
      sbt.Keys.update.value
    )
  }

  def jmhCompiletimeParams(rootPath: Path, packageStr: Seq[String], isScala3: Boolean, compileCp: Seq[File]): String = {
    val packagePath = packageStr.foldLeft(rootPath)(_.resolve(_))

    /*
    val sources = for {
      caseClass <- circeDerivationCaseClasses
      genTpe    <- GenType.values(isScala3)
    } yield packagePath.resolve(s"${caseClass.tpeName}Derives${genTpe.name.capitalize}.scala").toAbsolutePath
     */

    val dependencies = rootPath.toAbsolutePath.toFile +: compileCp.map(_.getAbsoluteFile)

    val params = Map(
      "typeSize"      -> typeSizes.mkString(","),
      "genType"       -> GenType.productValues(isScala3).map(_.name.capitalize).mkString(","),
      "sourceRoot"    -> packagePath.toAbsolutePath.toString,
      "sourcePattern" -> "BenchmarkCaseClass{0}Derives{1}",
      "depsClasspath" -> dependencies.mkString(File.pathSeparator)
    )
	
	val scala3Params = Map(
	  "extraArgs" -> "-Xmax-inlines|128"
	)
	
	val realParams = if (isScala3) params ++ scala3Params else params

    realParams.map(t => s"${t._1}=${t._2}").mkString("-p ", " -p ", "")
  }
}
