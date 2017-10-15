import org.scalatest.FlatSpec
import org.scalatest.events.Event

class SequenceV3Test extends FlatSpec {


	def to(actor: Actor): Actor = ???

	def does(action: String): SequenceFlow = ???

	def using(tracking: Sequence): Sequence = ???

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

		val tracking = new Sequence("track flight request").as(
			janine.does("track", to(tracker)) :: 
			tracker.does("write on db01")
				.and()
				.fire("TRACKED")
				.and()
				.stop()	:: Nil
		)

		new Sequence("flight search by meta")
			.as(
				user.does("search", to(skyscanner)) ::
					skyscanner.does("search", to(mss)) ::
					mss.launch(tracking).and().does("search", to(janine)) ::
					janine.does("search", to(bsa)) ::
					bsa.reply("trips", to(janine)) ::
					janine.does("restriction filter")
						.and().does("applyMargin")
						.and().does("apply buckets filter")
						.and().forEach("trip", does("transform domain to adapter"))
						.and().reply("transformedTrips", to(mss)) ::
					mss.does("sorts trips by price")
						.and().forEach("trip", does("create deeplink"))
						.and().reply("trips", to(skyscanner)) ::
					skyscanner.reply("trips", to(user)) :: Nil
			)


	}

	class SequenceFlow(name: String) {

		def does(action: String): SequenceFlow = ???

		def and(): Actor = ???
	}

	case class Actor(name: String) {
		def stop(): SequenceFlow = ???

		def fire(event: String): SequenceFlow = ???

		def launch(tracking: Sequence): SequenceFlow = ???

		def does(tracking: Sequence): SequenceFlow = ???

		def does(action: String): SequenceFlow = ???

		def does(bsaFlow: SequenceFlow): SequenceFlow = ???

		def does(action: String, actor: Actor): SequenceFlow = ???

		def reply(action: String, actor: Actor): SequenceFlow = ???

		def forEach(item: String, sequenceFlow: SequenceFlow): SequenceFlow = ???

	}

	case class User(role: String) extends Actor(name = role) {


	}

	case class Sequence(name: String) {
		def as(flow: Seq[SequenceFlow]): Sequence = ???
	}

}
