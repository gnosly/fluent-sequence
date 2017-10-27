package com.gnosly.api

import com.gnosly.api.FluentSequence._
import org.scalatest.{FlatSpec, Matchers}

class FluentSequenceTest extends FlatSpec with Matchers {

	"Sequence" should "be empty" in {
		new FluentSequence.Sequence("sequenceName").toEventBook.toList shouldBe List()
	}


	it should "be simple sequence" in {
		val sequence = new Sequence("sequenceName").startWith(
			new User("user").does("something") :: Nil
		)

		sequence.toEventBook shouldBe EventBook(
			"SEQUENCE sequenceName STARTED",
			"USER user DOES something"
		)
	}

/*
	ignore should "handle complex scenario" in {
		val user = new User("tourist")
		val skyscanner = new Actor("skyscanner")
		val tracker = new Actor("tracker")
		val mss = new Actor("mss")
		val janine = new Actor("janine")
		val bsa = new Actor("bsa")
		val msr = new Actor("msr")
		val vg1 = new Actor("vg1")
		val opco: Actor = new Actor("opco")

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
			)
	}
*/
}
