# Tutorial
# Introduction
---
Welcome to the GreenTea Plugin! The GreenTea Plugin is a non-profit plugin developed by 8 Computer Sciences and Engineering students of POSTECH, Korea. This Plugin will enable you to examine the metrics of  your very own source code, and you will also be able to document these metric values through textual Or visual reports. You could also use our logging feature to keep track of your progress over time. We hope you enjoy your experience with GreenTea Plugin.
# What does each metric mean?
---
While we provide you with information of 8 different metrics which changes real-time as you update your code, you may not be fully aware of what each emtric signifies. Therefore, we have prepared a short cheat-sheet for you to understand what each metric can tell you.

## Lines of Code
Lines of Code basically shows you the total lines of code you have written for a specific module.
While shorter code doesn't necessary mean better, you may always get an idea of how much code you have written.

## Halstead Volume
The function(s) to calculte Halstead Volume is as follows :

* Halstead Volume = (total number of operators/operands) * log(number of distinct operators/operands)

This metric approximates the size of elements and vocabulary of the module.
While this metric depends less on the formatting of the code, it still depends on the language you are using.
## Cyclomatic Complexity
The function(s) to calculate Cyclomatic Complexity is as follows :

* Cyclomatic Complexity = number of predicates(branches) + 1
* Cyclomatic Complexity = number of edges - number of nodes + 2
* Number of regions of the control flow graph

This metric is a measure of logical complexity, and tells us how many tests are needed to execute every statement of the program.

## Dhama's Coupling Metric
The function(s) to calculate Dhama's Coupling Metric is as follows :

* Module Coupling = 1 / (#input parameters + #output parameters + #global variables used + #modules called + #modules calling)

This metric (a type of coupling) shows the dependence among modules. High couplling is usually regarded as a bad sign.
## Martin's Coupling Metric
The function(s) to calculate Martin's Coupling Metric is as follows :

* Ca (afferent Coupling) : fan - in = the number of classes outside this module that depend on classes inside this module.
* Ce (efferent Coupling) : fan - out = the number of classes inside this module that depend on classes outside this module.

This metric (a type of coupling) shows the dependence among modules. High couplling is usually regarded as a bad sign.

## Maintainability Index
The function(s) to calculate Maintainability Index is as follows :

* Maintainability Index = max(0, (171−
5.2 ∗ log(Halstead Volume)−
0.23 ∗ (Cyclomatic Complexity)−
16.2 ∗ log(Lines of Code)) ∗ 100/171)

This metric calculates an index values between 0 and 100 that represents the relative ease of maintaining the code. Higher value means better maintainability.

## Instability
The function(s) to calculate Instability is as follows :

* Instability = Efferent Coupling / (Afferent Coupling + Efferent Coupling)

This metric - as the name tells - signifies the instability of the code.
The value of 1 means highly unstable, while the value of 0 means highly stable (but hard to change).

## Abstractness
The function(s) to calculate Abstractness is as follows :

* Abstractness = number of abstract classes in module / number of classes in module

This metric measures how 'abstract' or 'concrete' the module is.
The value of 1 means completely abstract, while the value of 0 means completely concrete.
# Additional Features
---
## Producing Reports
You can produce a report of your current metrics status in GreenTea PlugIn.
They can be textual and visual : visual reports could help others understand your software layout in a more straightforward way!

## Searching and Filtering through Results
You  may have a whole bunch of different projects in your workspace - not only that, you may also have a tremendous amount of source files for each project! Therefore, the GreenTea Plugin provides features to search and filter the results so that you can cherry-pick and view the metrics of specific modules you want.