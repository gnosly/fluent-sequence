import org.scalatest.FlatSpec

class SequenceV3Test extends FlatSpec {


	def to(actor: Actor): Actor = ???

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
		val opco: Actor = Actor("opco")

		val tracking = new Sequence("track flight request").startWith(
			janine.call("track", to(tracker)) ::
				tracker.does("write on db01")
					.and()
					.fire("TRACKED")
					.and()
					.stop() :: Nil
		)

		val flightSearch = new Sequence("flight search")
			.startWith(user.call("search", to(skyscanner)) ::
				skyscanner.call("search", to(mss)) ::
				mss.launch(tracking).and().call("search", to(janine)) ::
				janine.call("search", to(bsa)) ::
				bsa.reply("trips", to(janine)) ::
				janine.does("restriction filter")
					.and().does("applyMargin")
					.and().does("apply buckets filter")
					.and().forEach("trip", janine.does("transform domain to adapter"))
					.and().reply("transformedTrips", to(mss)) ::
				mss.does("sorts trips by price")
					.and().forEach("trip", mss.does("create deeplink"))
					.and().reply("trips", to(skyscanner)) ::
				skyscanner.reply("trips", to(user)) :: Nil)

		val opcoFlow = vg1.call("redirect", to(opco))

		val checkout = new Sequence("checkout").startWith(
			user.call("choose our flight", to(skyscanner)) ::
				skyscanner.reply("302 to deeplink", to(user)) ::
				user.call("open deeplink", to(msr)) ::
				msr.reply("302 /vg", to(user)) ::
				user.call("open /vg", to(vg1)) ::
				vg1.check("is opco enabled?")
					.inCase("yes", opcoFlow)
					.inCase("no", vg1.does("old checkout")) :: Nil)

		new Sequence("flight search by meta")
			.startWith(
				user
					.does(flightSearch)
					.and()
					.does(checkout) :: Nil
			)
	}

	class SequenceFlow(name: String) {
		def inCase(statement: String, flow: SequenceFlow): SequenceFlow = ???

		def and(): Actor = ???
	}

	case class Actor(name: String) {
		def check(condition: String): SequenceFlow = ???

		def stop(): SequenceFlow = ???

		def fire(event: String): SequenceFlow = ???

		def launch(tracking: Sequence): SequenceFlow = ???

		def does(sequence: Sequence): SequenceFlow = ???

		def does(action: String): SequenceFlow = ???

		def call(action: String, actor: Actor): SequenceFlow = ???

		def reply(action: String, actor: Actor): SequenceFlow = ???

		def forEach(item: String, sequenceFlow: SequenceFlow): SequenceFlow = ???

	}

	case class User(role: String) extends Actor(name = role) {}

	case class Sequence(name: String) {
		def startWith(flow: Seq[SequenceFlow]): Sequence = ???
	}

}
