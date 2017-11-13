package xdean.stackoverflow.readme

import java.io.IOException
import java.io.PrintStream
import java.nio.file.Files
import java.nio.file.Path
import java.util.LinkedList
import io.reactivex.Observable
import xdean.jex.util.string.StringUtil
import lombok.ToString

val HEAD: String = "# StackOverflow\nMy answers on Stack Overflow\n"

class ReadMeWriter {
	var questions = QuestionTree()

	fun add(question: Question): ReadMeWriter {
		questions.addQuestion(question)
		return this;
	}

	@Throws(IOException::class)
	fun writeTo(path: Path) {
		val ps = PrintStream(Files.newOutputStream(path))
		ps.println(HEAD)
		questions.writeTo(ps, 0)
	}

	fun QuestionTree.writeTo(ps: PrintStream, space: Int) {
		this.subTrees.forEach { name, subTree ->
			ps.println("  " * space + "- $name")
			subTree.writeTo(ps, space + 1);
		}
		this.questions.forEach {
			ps.println("  " * space + "- ${it.markdown}")
		}
	}

	operator fun String.times(time: Int) = StringUtil.repeat(this, time)
}