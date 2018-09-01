package com.gnosly.fluentsequence.api;

import com.gnosly.fluentsequence.core.SEQUENCE_ACTOR_TYPE;
import scala.collection.JavaConverters;

import static java.util.Arrays.asList;

public class asdfTest {

    public static void main(String[] args) {
//        new Sequence("example flow").startWith(
//                moduleA.call("request", to(moduleB)) ::
//                        moduleB.does("something") ::
//                moduleB.reply("response", to(moduleA)) ::
//                Nil)

        FluentSequence.FluentActor moduleA = new FluentSequence.FluentActor("moduleA", new SEQUENCE_ACTOR_TYPE());
        FluentSequence.FluentActor moduleB = new FluentSequence.FluentActor("moduleB", new SEQUENCE_ACTOR_TYPE());

        FluentSequence.Sequence sequence = new FluentSequence.Sequence("example flow").startWith(
                JavaConverters.asScalaBuffer(asList(
                        moduleA.call("request", moduleB),
                        moduleB.does("doing the job"),
                        moduleB.reply("response", moduleA)
                ))
        );

        sequence.printToConsole();
        sequence.printToSvg();
    }
}
