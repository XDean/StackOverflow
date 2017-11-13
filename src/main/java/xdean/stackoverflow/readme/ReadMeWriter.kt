package xdean.stackoverflow.readme

import java.io.IOException
import java.io.PrintStream
import java.nio.file.Files
import java.nio.file.Path
import java.util.LinkedList
import io.reactivex.Observable

class ReadMeWriter {
	var questions = LinkedList<Question>()

	fun add(question: Question) = questions.add(question)

	@Throws(IOException::class)
	fun writeTo(path: Path) {
		val ps = PrintStream(Files.newOutputStream(path))
		ps.println(HEAD)
		Observable.fromIterable(questions)
// 	.groupBy(keySelector)
	}

	companion object {
		val HEAD: String = "# StackOverflow\nMy answers on Stack Overflow\n"
	}
}