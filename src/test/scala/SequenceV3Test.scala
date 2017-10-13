import org.scalatest.FlatSpec

class SequenceV3Test extends FlatSpec {



	def to(actor: Actor): Actor = ???

	def does(action: String): SequenceFlow = ???

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
		val bsaFlow = new SequenceFlow("bsaFlow")
		val trackingFlow: SequenceFlow = janine.does("track", to(tracker))

		val janineFlow = janine.does(bsaFlow)
			.and()
			.does("restriction filter")
			.and()
			.does("applyMargin")
			.and()
			.does("apply buckets filter")
			.and()
			.forEach("trip", does("transform domain to adapter"))
			.and()
			.does(trackingFlow)
			.and()
			.reply("transformedTrips", to(mss))

		new Sequence(
			user.does("search", to(skyscanner)),
			skyscanner.does("search", to(mss)),
			mss.does("search", to(janine)),
			janineFlow
		)

	}

	class SequenceFlow(name: String) {

		def does(action: String): SequenceFlow = ???

		def and(): Actor = ???
	}

	case class Actor(name: String) {
		def does(action: String): SequenceFlow = ???

		def does(bsaFlow: SequenceFlow): SequenceFlow = ???

		def does(action: String, actor: Actor): SequenceFlow = ???

		def reply(action: String, actor: Actor): SequenceFlow = ???

		def forEach(item: String, sequenceFlow: SequenceFlow): SequenceFlow = ???

	}

	case class User(role: String) extends Actor(name = role) {


	}

	case class Sequence(flow: SequenceFlow*)

}
