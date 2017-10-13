import org.scalatest.FlatSpec

class SequenceV2Test extends FlatSpec {

	def that(): SequenceFlow = ???

	def call(name: String): SequenceFlow = ???

	def reply(name: String): SequenceFlow = ???

	def does(name: String): SequenceFlow = ???

	def does(flow: SequenceFlow): SequenceFlow = ???

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
		val opcoFlow = new SequenceFlow("opcoFlow")
		val oldVgFlow = new SequenceFlow("vgFlow")


		val flightSearch = call("search").to(skyscanner).that(
			call("search").to(mss).that(
				call("search").to(janine).that(call("search").to(bsa).that(
					reply("trips").to(janine).that(
						does("restriction filter")
							.and().does("applyMargin")
							.and().does("apply buckets filter"))))))


		sequence
			.startWith(user).that(does(flightSearch)).and(does(opcoFlow))

	}

	class SequenceFlow(name: String) {
		def and(flow: SequenceFlow): Any = ???

		def startWith(user: Actor): SequenceFlow = ???

		def to(skyscanner: Actor, flow: SequenceFlow): SequenceFlow = ???

		def check(str: String): SequenceFlow = ???

		def apply(opcoFlow: SequenceFlow): SequenceFlow = ???


		def forEach(name: String): SequenceFlow = ???

		def start(): SequenceFlow = ???

		def otherwise(): SequenceFlow = ???

		def otherwise(sequenceFlow: SequenceFlow): SequenceFlow = ???

		def inCase(str: String): SequenceFlow = ???

		def end(): SequenceFlow = ???

		def reply(str: String): SequenceFlow = ???

		def then(metasearch: Actor): SequenceFlow = ???

		def then(string: String, sequenceFlow: SequenceFlow): SequenceFlow = ???

		def and(): SequenceFlow = ???

		def does(str: String): SequenceFlow = ???

		def that(sequenceFlow: SequenceFlow): SequenceFlow = ???

		def to(metasearch: Actor): SequenceFlow = ???

		def call(name: String): SequenceFlow = ???

		def as(user: Actor): SequenceFlow = ???

	}

	case class Actor(name: String)

	case class User(role: String) extends Actor(name = role)

}
