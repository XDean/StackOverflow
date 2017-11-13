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
					if (it.shouldTraverse()) Try.to<Iterable<Path>>({ Files.newDirectoryStream(it) }).getOrElse(emptyList())
					else emptyList()
				}
						.filter({ it.isQuestion() })
						.flatMap({ Flowable.just(it).map(::Question).subscribeOn(scheduler) })
						.doOnNext(::println)
			}
			.reduce(ReadMeWriter()) { t, q -> t.add(q) }
			.toObservable()
			.doOnNext(::println)
			.blockingForEach { it.writeTo(README) }
}

fun Path.shouldTraverse(): Boolean = Files.isDirectory(this) && !this.isQuestion()

fun Path.isQuestion(): Boolean = this.getFileName().toString().matches(Regex("Q[0-9]+(\\.(java)|(kt))?"))

