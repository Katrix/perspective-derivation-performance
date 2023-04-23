package perspective.circederivationcompiletime

import java.io.IOException
import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes

object BenchmarkUtils {

  def deleteRecursive(directory: Path): Unit = {
    if (Files.exists(directory)) {
      Files.walkFileTree(directory, new SimpleFileVisitor[Path]() {
        override def visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult = {
          Files.delete(file)
          FileVisitResult.CONTINUE
        }
        override def postVisitDirectory(dir: Path, exc: IOException): FileVisitResult = {
          Files.delete(dir)
          FileVisitResult.CONTINUE
        }
      })
    }
  }
}
