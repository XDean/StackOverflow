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
import java.util.Properties
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption


val SAVED_PATH = Paths.get("src/main/resources/saved_title.properties");
val SAVED_TITLE = Properties().also {
	Files.createDirectories(SAVED_PATH.parent)
	if (!Files.exists(SAVED_PATH)) Files.createFile(SAVED_PATH)
	it.load(Files.newInputStream(SAVED_PATH))
}
val MD_TEMPLATE: String = "[%s](https://stackoverflow.com/questions/%d) ([view code](%s))"

class Question(path: Path) : Comparable<Question> {
	val id = path.toQuestionId()
	val title = getTitle(id)
	val path: Path = path
	val category = Observable.fromIterable(path)
			.skip(5)
			.skipLast(1)
			.map(Path::toString)
			.toList()
			.blockingGet()
	val markdown = String.format(MD_TEMPLATE, title, id, path.toString().replace('\\', '/'))

	override fun toString(): String = "Question [id=$id, title=$title, path=$path]"

	override fun compareTo(other: Question): Int = id.compareTo(other.id)
}

val QUESTION_URL: String = "https://stackoverflow.com/questions/"
val TITLE_PATTERN = Pattern.compile("<title>(.*)</title>")
val ASCII_PATTERN = Pattern.compile("&#([0-9]{1,3});")

fun Path.toQuestionId(): Int = Integer.parseInt(FileUtil.getNameWithoutSuffix(this).substring(1))

fun getTitle(id: Int): String =
		SAVED_TITLE.getProperty(id.toString())
				?: ((
				try {
					Resources.readLines(URL(QUESTION_URL + id), Charsets.UTF_8,
							object : LineProcessor<String?> {
								var title: String? = null
								override fun processLine(line: String): Boolean {
									val matcher = TITLE_PATTERN.matcher(line)
									if (matcher.find()) {
										title = matcher.group(1)
										return false
									} else {
										return true
									}
								}

								override fun getResult(): String? = title
							})
				} catch(e: Exception) {
					null
				}
						?.resolveASCII()
						?: "Question $id can't found")
						.also {
							SAVED_TITLE.setProperty(id.toString(), it)
							SAVED_TITLE.store(Files.newOutputStream(SAVED_PATH), null)
						})


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