import java.nio.file.{Files, Path}

import scala.jdk.CollectionConverters._

object GenerateCirceSources {

  case class CaseClass(tpeName: String, fields: Seq[(String, String)], intId: Int)

  sealed abstract class GenType(val name: String) {
    def imports: String = this match {
      case GenType.CirceDerivation => "import io.circe.derivation._"
      case GenType.CirceGeneric    => "import io.circe.generic.semiauto._"

      case GenType.Handwritten => "import io.circe.syntax._"
      case GenType.Shapeless2  => "import perspective.circederivation.Shapeless2Derive"
      case GenType.Shapeless3  => "import perspective.circederivation.Shapeless3Derive"

      case GenType.Perspective          => "import perspective.circederivation.PerspectiveDerive"
      case GenType.PerspectiveFaster    => "import perspective.circederivation.PerspectiveFasterDerive"
      case GenType.PerspectiveInline    => "import perspective.circederivation.PerspectiveInlineDerive"
      case GenType.PerspectiveUnrolling => "import perspective.circederivation.PerspectiveUnrollingDerive"
    }

    def deriveEncoder(caseClass: CaseClass): String = {
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
        case GenType.Shapeless2 => s"Shapeless2Derive.deriveEncoder"
        case GenType.Shapeless3 => s"Shapeless3Derive.deriveEncoder"

        case GenType.Perspective          => s"PerspectiveDerive.deriveEncoder"
        case GenType.PerspectiveFaster    => s"PerspectiveFasterDerive.deriveEncoder"
        case GenType.PerspectiveInline    => s"PerspectiveInlineDerive.deriveEncoder"
        case GenType.PerspectiveUnrolling => s"PerspectiveUnrollingDerive.deriveEncoder"
      }
    }

    def deriveDecoder(caseClass: CaseClass): String = {
      import caseClass._
      this match {
        case GenType.CirceDerivation => "deriveDecoder"
        case GenType.CirceGeneric    => "deriveDecoder"

        case GenType.Handwritten =>
          s"""|new Decoder[$tpeName] {
              |  override def apply(cursor: HCursor): Decoder.Result[$tpeName] = {
              |    for {
              |      ${fields.map(t => s"""${t._1} <- cursor.get[${t._2}]("${t._1}")""").mkString("\n" + "  " * 3)}
              |    } yield $tpeName(${fields.map(_._1).mkString(", ")})
              |  }
              |}
              |""".stripMargin
        case GenType.Shapeless2 => s"Shapeless2Derive.deriveDecoder"
        case GenType.Shapeless3 => s"Shapeless3Derive.deriveDecoder"

        case GenType.Perspective          => s"PerspectiveDerive.deriveDecoder"
        case GenType.PerspectiveFaster    => s"PerspectiveFasterDerive.deriveDecoder"
        case GenType.PerspectiveInline    => s"PerspectiveInlineDerive.deriveDecoder"
        case GenType.PerspectiveUnrolling => s"PerspectiveUnrollingDerive.deriveDecoder"
      }
    }
  }
  object GenType {
    case object CirceDerivation extends GenType("circeDerivation")
    case object CirceGeneric    extends GenType("circeGeneric")

    case object Handwritten extends GenType("handwritten")
    case object Shapeless2  extends GenType("shapeless2")
    case object Shapeless3  extends GenType("shapeless3")

    case object Perspective          extends GenType("perspective")
    case object PerspectiveFaster    extends GenType("perspectiveFaster")
    case object PerspectiveInline    extends GenType("perspectiveInline")
    case object PerspectiveUnrolling extends GenType("perspectiveUnrolling")

    def values(isScala3: Boolean): Seq[GenType] = {
      val global = Seq(CirceGeneric, Handwritten, Shapeless2, Perspective, PerspectiveFaster)

      if (isScala3) global ++ Seq(Shapeless3, PerspectiveInline, PerspectiveUnrolling)
      else global ++ Seq(CirceDerivation)
    }
  }

  def generateDerives(caseClass: CaseClass, objName: String, genTpe: GenType): String =
    s"""|object $objName {
        |  ${genTpe.imports}
        |
        |  implicit val encoder: Encoder[${caseClass.tpeName}] = ${genTpe
         .deriveEncoder(caseClass)
         .linesIterator
         .map("  " + _)
         .mkString("\n")}
        |  implicit val decoder: Decoder[${caseClass.tpeName}] = ${genTpe
         .deriveDecoder(caseClass)
         .linesIterator
         .map("  " + _)
         .mkString("\n")}
        |}""".stripMargin

  def generateCaseClass(caseClass: CaseClass): String =
    s"case class ${caseClass.tpeName}(${caseClass.fields.map(t => s"${t._1}: ${t._2}").mkString(", ")})"

  def generateCaseClassAndDerives(genTpe: GenType, caseClass: CaseClass): String =
    s"""|${generateCaseClass(caseClass)}
        |${generateDerives(caseClass, caseClass.tpeName, genTpe)}""".stripMargin

  def generateCompiletimeFiles(rootPath: Path, caseClasses: Seq[CaseClass], isScala3: Boolean): Unit = {
    for {
      caseClass <- caseClasses
    } {
      Files.createDirectories(rootPath)
      Files.write(
        rootPath.resolve(caseClass.tpeName + ".scala"),
        s"""|import io.circe._
            |${generateCaseClass(caseClass)}""".stripMargin.linesIterator.toSeq.asJava
      )

      for {
        genTpe <- GenType.values(isScala3)
      } {
        val objName = caseClass.tpeName + "Derives" + genTpe.name.capitalize

        Files.write(
          rootPath.resolve(objName + ".scala"),
          s"""|import io.circe._
              |${generateDerives(
               caseClass,
               objName,
               genTpe
             )}""".stripMargin.linesIterator.toSeq.asJava
        )
      }
    }
  }

  def generateRuntimeFiles(rootPath: Path, caseClasses: Seq[CaseClass], isScala3: Boolean, packageLoc: String): Unit = {
    for {
      genTpe <- GenType.values(isScala3)
    } {
      def classLines(mkLine: CaseClass => String, i: Int = 0): String =
        caseClasses.map(mkLine).mkString("\n").linesIterator.mkString("\n" + "  " * (i + 1))

      Files.createDirectories(rootPath)
      Files.write(
        rootPath.resolve(genTpe.name.capitalize + "Defs.scala"),
        s"""|package $packageLoc
            |
            |import io.circe._
            |import io.circe.syntax._
            |import org.openjdk.jmh.annotations._
            |import scala.annotation.switch
            |import scala.util.Random
            |
            |object ${genTpe.name.capitalize}Defs extends BenchmarkDefs {
            |
            |  ${classLines(generateCaseClassAndDerives(genTpe, _))}
            |
            |  def encode(intId: Int, obj: AnyRef): Json = (intId: @switch) match {
            |    ${classLines(c => s"case ${c.intId} => obj.asInstanceOf[${c.tpeName}].asJson", i = 1)}
            |  }
            |
            |  def decode(intId: Int, json: Json): AnyRef = (intId: @switch) match {
            |    ${classLines(c => s"case ${c.intId} => json.as[${c.tpeName}]", i = 1)}
            |  }
            |
            |  @State(Scope.Benchmark)
            |  class EncodeData {
            |    ${classLines(c => s"var v${c.tpeName}: ${c.tpeName} = null", i = 1)}
            |    
            |    @Param(Array(${caseClasses.map(c => s""""${c.intId}"""").mkString(", ")}))
            |    var intId = 0
            |
            |    def getData(id: Int): AnyRef = (id: @switch) match {
            |      ${classLines(c => s"case ${c.intId} => v${c.tpeName}", i = 2)}
            |    }
            |    
            |    @Setup
            |    def setup(): Unit = {
            |      val rand: Random = new Random(1234)
            |      ${classLines(
             c => s"v${c.tpeName} = ${c.tpeName}(${c.fields.map(f => s"genField[${f._2}](rand)").mkString(", ")})",
             i = 2
           )}
            |    }
            |  }
            |  
            |  @State(Scope.Benchmark)
            |  class DecodeData {
            |    ${classLines(c => s"var s${c.tpeName}: Json = null", i = 1)}
            |    
            |    @Param(Array(${caseClasses.map(c => s""""${c.intId}"""").mkString(", ")}))
            |    var intId = 0
            |
            |    def getData(id: Int): Json = (id: @switch) match {
            |      ${classLines(c => s"case ${c.intId} => s${c.tpeName}", i = 2)}
            |    }
            |    
            |    @Setup
            |    def setup(): Unit = {
            |      val rand: Random = new Random(1234)
            |      ${classLines(
             c =>
               s"s${c.tpeName} = ${c.tpeName}(${c.fields.map(f => s"genField[${f._2}](rand)").mkString(", ")}).asJson",
             i = 2
           )}
            |    }
            |  }
            |}""".stripMargin.linesIterator.toSeq.asJava
      )
    }
  }

  lazy val circeDerivationCaseClasses: Seq[CaseClass] = {
    val sizes      = Seq(5) // Seq(1, 5, 10, 20, 22, 23, 30, 50, 75, 99)
    val fieldTypes = Seq("Int", "String", "Double", "Boolean", "Json")

    sizes.map { size =>
      CaseClass(s"BenchmarkCaseClass$size", Seq.tabulate(size)(i => (s"f$i", fieldTypes(i % fieldTypes.length))), size)
    }
  }
}
