package xdean.stackoverflow.readme

import io.reactivex.Flowable
import xdean.jex.extra.collection.Traverse
import xdean.jex.extra.rx2.RxSchedulers
import xdean.jex.util.task.TaskUtil
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.Arrays
import java.util.Collections
import xdean.jex.util.task.tryto.Try
import io.reactivex.rxkotlin.toFlowable

val ROOT = arrayOf(
		Paths.get("src", "main", "java"),
		Paths.get("src", "main", "kotlin"))
val README = Paths.get("README.md")

fun main(args: Array<String>) {
	assert(Files.exists(README))
	val scheduler = RxSchedulers.fixedSize(10)
	ROOT.toFlowable()
			.flatMap {
				Traverse.preOrderTraversal<Path>(it)
				{
					if (it.shouldTraverse)
						try {
							Files.newDirectoryStream(it)
						} catch(e: Exception) {
							emptyList<Path>()
						}
					else emptyList<Path>()
				}
						.filter({ it.isQuestion })
						.flatMap({ Flowable.just(it).map(::Question).subscribeOn(scheduler) })
						.doOnNext(::println)
			}
			.reduce(ReadMeWriter()) { t, q -> t.add(q) }
			.toObservable()
			.blockingForEach { it.writeTo(README) }
}

val Path.shouldTraverse: Boolean get() = Files.isDirectory(this) && !this.isQuestion

val Path.isQuestion: Boolean get() = this.getFileName().toString().matches(Regex("Q[0-9]+(\\.((java)|(kt)))?"))

