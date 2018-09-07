This project aims to create sequence diagrams programmatically, using a programming language like Scala or Java or a customized language. So you could save the created sequence as SVG image or fixed-width text.

## Using Scala
For example in Scala you could import the dependency in your project:

```
add dependency url
```

Write something like:

```
val moduleA = new FluentActor("moduleA")
val moduleB = new FluentActor("moduleB")

new Sequence("example flow").startWith(
  moduleA.call("request", to(moduleB)) ::
  moduleB.does("doing the job") :: 
  moduleB.reply("response", to(moduleA)) ::
  Nil
)
```

and render the sequence to console using

```
sequence.printToConsole();
```

```
_______________
| example flow \
|-------------------------------------------------------
| .---------.          .---------.                     |
| | moduleA |          | moduleB |                     |
| '---------'          '---------'                     |
|      |                    |                          |
|     _|_                  _|_                         |
|     | |     request      | |                         |
|     | |----------------->| |                         |
|     | |                  | |                         |
|     | |                  | |____                     |
|     | |                  | |    |                    |
|     | |                  | |    | doing the job      |
|     | |                  | |<---'                    |
|     | |                  | |                         |
|     | |     response     | |                         |
|     | |<-----------------| |                         |
|     |_|                  |_|                         |
|      |                    |                          |
|                                                      |
|_______________________________________________________
```

or to svg using

```
sequence.printToSvg();
```

![Svg example](https://raw.githubusercontent.com/gnosly/fluent-sequence/master/sequence_example.svg?sanitize=true)



## Using Java

```
FluentSequence.FluentActor moduleA = new FluentSequence.FluentActor("moduleA", new SEQUENCE_ACTOR_TYPE());
FluentSequence.FluentActor moduleB = new FluentSequence.FluentActor("moduleB", new SEQUENCE_ACTOR_TYPE());

FluentSequence.Sequence sequence = new FluentSequence.Sequence("example flow").startWith(
        JavaConverters.asScalaBuffer(asList(
                moduleA.call("request", moduleB),
                moduleB.does("doing the job"),
                moduleB.reply("response", moduleA)
        ))
);

```
## Using a custom syntaxt
You can also create your syntax and use this library to render the sequence, like [http://sequencediagram.fabriziogiovannetti.com](http://sequencediagram.fabriziogiovannetti.com) does.

You have to complete these tasks:
1. Create your grammar using BSON,JISON or ANTLR
2. Encode the sequence following the schema below
3. Use this library to render the encoded schema


### Sequence encoding specifications

The encoded sequence is a UTF-8 string that tells what happens in the flow sequentially using one of those elements

| encoded element | description
| --- | ---
SEQUENCE_STARTED|sequenceName} | The sequence *sequenceName* is started 
DONE&#124;actorAType&#124;actorAName&#124;action | *actorAName* of type *actorAType* does the *action*
CALLED&#124;actorAType&#124;actorA&#124;action&#124;actorBType&#124;actorB| *actorA* of type *actorAType* called the *action* on *actorB* of type *actorBType*
REPLIED&#124;actorAType&#124;actorA&#124;response&#124;actorBType&#124;actorB| *actorA* of type *actorAType* replied with *response* to *actorB* of type *actorBType*
SEQUENCE_ENDED&#124;sequenceName| The sequence *sequenceName* is ended

The possible actorType are
- USER_TYPE
- SEQUENCE_ACTOR_TYPE

A possible example is shown here
```
SEQUENCE_STARTED|example flow
CALLED|SEQUENCE_ACTOR_TYPE|moduleA|request|SEQUENCE_ACTOR_TYPE|moduleB
DONE|SEQUENCE_ACTOR_TYPE|moduleB|doing the job
REPLIED|SEQUENCE_ACTOR_TYPE|moduleB|response|SEQUENCE_ACTOR_TYPE|moduleA
SEQUENCE_ENDED|example flow
```

Now you can render the encoded data doing
```
val encodedSequence = "..."
val svg = new SvgViewer().view(new EventBookReader().read(encodedSequence))
```