package xdean.stackoverflow.readme

import java.util.Comparator
import java.util.LinkedList
import java.util.TreeMap

class QuestionTree {
	val subTrees = TreeMap<String, QuestionTree>(Comparator.naturalOrder())
	val questions = LinkedList<Question>()

	infix fun addQuestion(q: Question) = addQuestion(q, 0)

	private fun addQuestion(q: Question, step: Int) {
		val category = q.category
		if (category.size == step) {
			questions.add(q)
		} else {
			val sub = category[step]
			subTrees.getOrPut(sub, ::QuestionTree).addQuestion(q, step + 1)
		}
	}
}