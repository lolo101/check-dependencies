package csperandio.dependencies

import csperandio.dependencies.dependencies.Dependency
import csperandio.dependencies.files.PomFinder
import csperandio.dependencies.repo.ExternalMavenRepo
import csperandio.dependencies.repo.LocalMavenRepo
import java.io.File
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    if (args.size != 2) {
        System.err.println("Usage: [PROJECT ROOT DIR] [RESULT FILE]")
        exitProcess(1)
    }

    val rootDir = File(args[0])
    if (!rootDir.exists()) {
        System.err.print("Directory $rootDir not found.")
        exitProcess(1)
    }

    val finder = PomFinder(rootDir)
    val poms = finder.all("pom.xml")

    val checker = VersionChecker(LocalMavenRepo(), ExternalMavenRepo("https://repo1.maven.org/maven2"))

    val allDates = mutableMapOf<Dependency, String>()
    poms.forEach { allDates.putAll(checker.dates(it.readText()))}

    val result = File(args[1])
    val writer = CsvWriter(result)
    writer.write(allDates)

}
