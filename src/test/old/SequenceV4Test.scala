import org.scalatest.FlatSpec

class SequenceV4Test extends FlatSpec {



	def to(actor: Actor): Actor = ???

	def does(action: String): SequenceFlow = ???

	def receiving(str: String):String = ???

	"Sequence" should "be empty" in {
		val sequence = new SequenceFlow("flight booking")
		val user = User("tourist")
		val skyscanner = Actor("skyscanner")
		val tracker = Actor("tracker")
		val mss = Actor("mss")
		val janine = Actor("janine")
		val bsa = Actor("bsa")
		val msr = Actor("msr")
		val vg1 = Actor("vg1")


		user.start(flow("search", to(skyscanner)), receiving("trips"))
	}

	class SequenceFlow(name: String) {

		def does(action: String): SequenceFlow = ???

		def and(): Actor = ???
	}

	case class Actor(name: String) {
		def does(action: String): SequenceFlow = ???

		def does(bsaFlow: SequenceFlow): SequenceFlow = ???

		def does(action: String, actor: Actor): SequenceFlow = ???

		def does(action: String, actor: Actor, result: String): Any = ???

		def reply(action: String, actor: Actor): SequenceFlow = ???

		def forEach(item: String, sequenceFlow: SequenceFlow): SequenceFlow = ???

	}

	case class User(role: String) extends Actor(name = role) {

	}

	case class Sequence(flow: SequenceFlow*)

}
