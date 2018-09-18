## Get involved!

I'd like very much to work with other people. People could improve their competences working together. Working on a 
open source project is the best way to meet all kind of people around the world.
So if you like the project doesn't hesitate to get involved and contact me.
Below I give you some info useful to understand how the project is structured and the simple sequence flow.

### Project structure
The project is divided in three different layers
- Core: It contains the domain model. Basically it has got a event store that persist the sequence interactions transformed into events
- API: It contains the DSL used to declare the sequence diagram. It uses the domain model to transform each action into event. 
- View: It contains the logic to present the list of sequence events into view.


When you contribute code, you affirm that the contribution is your original work and that you license the work to the project under the project's open source license. Whether or not you state this explicitly, by submitting any copyrighted material via pull request, email, or other means you agree to license the material under the project's open source license and warrant that you have the legal authority to do so.