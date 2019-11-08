CSC 242 Project 3
Collaborators: Arun Ramesh, Thabani Dube

For Project 3, we have implemented the exact inference (enumeration), approximate inference (rejection sampling and likelihood weighting)
We have also done Gibbs for extra credit.

Below are the steps to run the program (for example the aima-alarm and aima-wet-grass questions from the exam). We have chosen to run configurations using eclipse

To run configuration:

Main method is InferencerMain.java in the default package

1) Enumeration

	[enumeration] [file-name] [query] [evidence] [true/false] [evidence (if applicable)] [true/false] 
	
	enumeration aima-alarm.xml B J true M true
	enumeration aima-wet-grass.xml R S true

2) Rejection Sampling
	
	[rejection] [Number of Samples] [file-name] [query] [evidence] [true/false] [evidence (if applicable)] [true/false] 

	rejection 100000 aima-alarm.xml B J true M true
	rejection 100000 aima-wet-grass.xml R S true

3) Likelihood Weighting

	[likelihood] [Number of Samples] [file-name] [query] [evidence] [true/false] [evidence (if applicable)] [true/false] 
	likelihood 100000 aima-alarm.xml B J true M true
	likelihood 100000 aima-wet-grass.xml R S true


4) Gibbs Sampling

	[gibbs] [Number of Samples] [file-name] [query] [evidence] [true/false] [evidence (if applicable)] [true/false] 
	gibbs 100000 aima-alarm.xml B J true M true
	gibbs 100000 aima-wet-grass.xml R S true
An example output would be (say for exact inference)

aima-alarm.xml:

Exact Inferencer using Enumeration: 

{true=0.2841718353643929, false=0.7158281646356071}

aima-wet-grass-xml:

Exact Inferencer using Enumeration: 

{true=0.3, false=0.7}
