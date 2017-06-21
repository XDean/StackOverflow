fun parse(string: String?): LibObject? {
	fun String.parseMe() = SomeJavaLibrary.parse(this)
	return string?.parseMe()
}

object SomeJavaLibrary {
	fun parse(str: String): LibObject = LibObject(str)
}

class LibObject(val str: String)