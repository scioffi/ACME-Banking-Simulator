243 OOP: Project 02 README
===============================
(please use the RETURN key to make multiple lines; don't assume autowrap.)

0. Author Information
---------------------

CS Username: 	scc3459 	Name:  		Stephen Cioffi
CS Username:    mji8299     Name:       Michael Incardona

1. Problem Analysis
---------

Summarize the analysis work you did. 
What new information did it reveal?  How did this help you?
How much time did you spend on problem analysis?

	When it came to planning this project, we spent about an 
	hour drawing and mapping out what we were going to do 
	for the project.

2. Design
---------

Explain the design you developed to use and why. What are the major 
components? What connects to what? How much time did you spend on design?
Make sure to clearly show how your design uses the MVC model by
separating the UI "view" code from the back-end game "model".

	When we created our design, we immediately came up with the idea to use
	two seperate panels at a time - one being the sidebar with the buttons,
	and the other being a different panel to adjust to the current operation.
	
	The GUI objects listen to the model using an Observer/Observable pattern, ensuring that model
	updates are passed up the ladder to the GUI. For example, the ATM GUI displays information from an ATM model,
	which listens to a logged-in account.

	$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$

3. Implementation and Testing
-------------------

Describe your implementation efforts here; this is the coding and testing.

What did you put into code first?
How did you test it?
How well does the solution work? 
Does it completely solve the problem?
Is anything missing?
How could you do it differently?
How could you improve it?

How much total time would you estimate you worked on the project? 
If you had to do this again, what would you do differently?

	At first, as a team, we divided the work into backend/batch and frontend GUI.
	To test our program, we developed our test cases to try every time we made a change.
	At the end, our solution does all it is supposed to do and then some. If we were to
	do this project differently, we would probably make the individual screens display objects.
	The only improvement we really could make to make it more like a real ATM would be to
	have sounds when you click a button.

4. Development Process
-----------------------------

Describe your development process here; this is the overall process.

How much problem analysis did you do before your initial coding effort?
How much design work and thought did you do before your initial coding effort?
How many times did you have to go back to assess the problem?
What did you learn about software development?

	Before starting the coding, as a team, we spent around an hour in planning and
	sketching ideas for the project, including class diagrams and pictures.
	
	At one point, we had to refactor our ATM GUI code because it had gotten too messy to understand, much less modify.
	
	We did some design but not enough as we ran into some complications with the
	ATM GUI along the way. We should have made each screen an object, taking advantage of polymorphism to 
	avoid repetitive switch/case statements. By the time we realized this, it was too late to overhaul 
	the project design so dramatically. We had to work with the design we had and make it functional, 
	despite its flaws.
	
	We would go back and assess the problem anytime we were confused about something and wanted to check 
	again to make sure. In software development, we learned to work as a team and work with team versioning tools
	such as Git.
