package com.gnosly.fluentsequence.api

import com.gnosly.fluentsequence.api.FluentSequence.{FluentActor, _}
import com.gnosly.fluentsequence.core._
import org.scalatest.{FlatSpec, Matchers}

class FluentSequenceTest extends FlatSpec with Matchers {

	"Sequence" should "be empty" in {
		new FluentSequence.Sequence("sequenceName").toEventBook.toList shouldBe List()
	}


	it should "be simple sequence" in {
		val user = new User("user")

		val sequence = new Sequence("sequenceName").startWith(
			user.does("something") :: Nil
		)

		sequence.toEventBook shouldBe EventBook(
			SEQUENCE_STARTED("sequenceName"),
			DONE(user, "something")
		)
	}

	ignore should "handle complex scenario" in {
		val user = new User("tourist")
		val skyscanner = new FluentActor("skyscanner")
		val tracker = new FluentActor("tracker")
		val mss = new FluentActor("mss")
		val janine = new FluentActor("janine")
		val bsa = new FluentActor("bsa")
		val msr = new FluentActor("msr")
		val vg1 = new FluentActor("vg1")
		val opco: FluentActor = new FluentActor("opco")

		val tracking = new Sequence("track flight request").startWith(
			janine.call("track", to(tracker)) ::
				tracker.does("write on db01")
					.and()
					.fire("TRACKED")
					.and()
					.stop() :: Nil
		)

		val flightSearch = new Sequence("flight search").startWith(user.call("search", to(skyscanner)) ::
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
			).printToConsole()
	}
}
