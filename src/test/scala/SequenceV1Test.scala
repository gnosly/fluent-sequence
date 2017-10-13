import org.scalatest.FlatSpec

class SequenceV1Test extends FlatSpec {


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

		val msrFlow = new SequenceFlow("msrFlow").start().as(user).call("/msr").to(msr)
			.that().reply("HTTP 302 /vg1").to(user).end()

		val flightSearch: SequenceV1Test.this.SequenceFlow = ???
		val checkout: SequenceV1Test.this.SequenceFlow = ???

		sequence.start()
			.with(user)
			.that()
			.does(flightSearch)
			.and()
			.does(checkout)


		sequence.start()
			.as(user).call("search").to(skyscanner)
			.that().call("search").to(mss)
			.that().call("search").to(janine)
			.that().call("search").to(bsa)
			.that().reply("trips").to(janine)
			.that().does("restriction filter")
			.and().does("applyMargin")
			.and().does("apply buckets filter")
			.and()
			.forEach("trip").does("transform domain to adapter").end()
			.and().reply("transformedTrips").to(mss)
			.that().does("sort by price")
			.and().does("generate deeplink")
			.and().reply("trips").to(skyscanner).that().reply("trips").to(user)
			.then(user).call("select trip").to(skyscanner).that().reply("HTTP 302 /msr").to(user)
			.then("msr flow", msrFlow)
			.then(user).call("/vg1").to(vg1)
			.that()
			.check("opco is enabled?")
			.inCase("yes").apply(opcoFlow)
			.inCase("boh").apply(opcoFlow)
			.otherwise(oldVgFlow)

	}

	class SequenceFlow(name: String) {
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

		def that(): SequenceFlow = ???

		def to(metasearch: Actor): SequenceFlow = ???

		def call(name: String): SequenceFlow = ???

		def as(user: Actor): SequenceFlow = ???

	}

	case class Actor(name: String)

	case class User(role: String) extends Actor(name = role)

}
