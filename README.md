This project aims to create sequence diagram programmatically in Scala and Java. 

For example in Scala you could write something like:
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

or in Java

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

