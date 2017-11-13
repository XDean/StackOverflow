package xdean.stackoverflow.readme

import com.google.common.base.Charsets
import com.google.common.io.LineProcessor
import com.google.common.io.Resources
import io.reactivex.Observable
import xdean.jex.util.file.FileUtil
import xdean.jex.util.log.LogUtil.log
import java.net.URL
import java.nio.file.Path
import java.util.Optional
import java.util.regex.Pattern


val MD_TEMPLATE: String = "[%s](https://stackoverflow.com/questions/%d) ([view code](%s))"

class Question(path: Path) : Comparable<Question> {
	val id = path.toQuestionId()
	val title = getTitleFromNetwork(id).map({ it.resolveASCII() }).orElse("Question can't found")
	val path: Path = path
	val category = Observable.fromIterable(path)
			.skip(5)
			.skipLast(1)
			.map(Path::toString)
			.toList()
			.blockingGet()
	val markdown = String.format(MD_TEMPLATE, title, id, path)

	override fun toString(): String = "Question [id=" + id + ", title=" + title + ", path=" + path + "]"

	override fun compareTo(other: Question): Int = id.compareTo(other.id)
}

val QUESTION_URL: String = "https://stackoverflow.com/questions/"
val TITLE_PATTERN = Pattern.compile("<title>(.*)</title>")
val ASCII_PATTERN = Pattern.compile("&#([0-9]{1,3});")

fun Path.toQuestionId(): Int = Integer.parseInt(FileUtil.getNameWithoutSuffix(this).substring(1))

fun getTitleFromNetwork(id: Int): Optional<String> {
	try {
		return Resources.readLines(URL(QUESTION_URL + id), Charsets.UTF_8,
				object : LineProcessor<Optional<String>> {
					var title: String = ""
					override fun processLine(line: String): Boolean {
						val matcher = TITLE_PATTERN.matcher(line)
						if (matcher.find()) {
							title = matcher.group(1)
							return false
						} else {
							return true
						}
					}

					override fun getResult(): Optional<String> = Optional.ofNullable(title)
				})
	} catch (e: Exception) {
		log().debug(e.message, e)
		return Optional.empty()
	}
}

fun String.resolveASCII(): String {
	var result = this;
	while (true) {
		val matcher = ASCII_PATTERN.matcher(result)
		if (!matcher.find()) {
			break
		}
		val start = matcher.start()
		val end = matcher.end()
		val c = Integer.parseInt(matcher.group(1)).toChar()
		result = result.substring(0, start) + c + result.substring(end)
	}
	return result
}