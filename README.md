# GreenTea

# User Stories and Prioritization

T_S01

* Title: `Calculate lines of code`
* Priority: 1
* Units (Story Points): 1
* `Calculate total/methods lines of code to display on metric window. Here, line is a line that is well aligned`

T_S02

* Title: `Calculate Halstead Volume`
* Priority: 2
* Units (Story Points): 3
* `If user will open a project in Green Tea plug-in, user can instantly see the result of Halstead volume in Tree view UI.`

T_S03

* Title: `Calculate Cyclomatic Complexity`
* Priority: 2
* Units (Story Points): 3
* `Calculate Cyclomatic Complexity, Plug in have to calculate number of branches(if,while,for)`

T_S04

* Title: `Calculate Dhama's Coupling Metric`
* Priority: 2
* Units (Story Points): 5
* `Calculate Dhama's Coupling Metric. Plug in have to calculate number of input/output parameters and number of global variable used and number of modules called/calling`

T_S05

* Title: `Calculate Martin's Coupling Metric`
* Priority: 2
* Units (Story Points): 5
* `Calculate Martin's Coupling Metric. For these, plug in have to calculate afferent coupling(fan-in) and efferent coupling(fan-out)`

T_S06

* Title: `Calculate Maintainability Index`
* Priority: 2
* Units (Story Points): 8
* `Plug in have to calculate Maintainability Index using T_S01, T_S02, T_S03,`

T_S07

* Title: `Calculate Instability`
* Priority: 2
* Units (Story Points): 4
* `Calculate the Instability of the module. This can be calculated by the Efferent Coupling divided by the sum of Afferent Coupling and Efferent Coupling.`

T_S08

* Title: `Calculate Abstractness`
* Priority: 2
* Units (Story Points): 4
* `If user will open a project in Green Tea plug-in, user can instantly see the result of Abstractness in Tree view UI.`

T_S09

* Title: `Display Green tea UI
* Priority: 2
* Units (Story Points): 5
* `When cliking on views -> Others -> GreenTea, the UI window for the GreenTea can be accessed`

T_S10

* Title: `Tree Logical View of UI`
* Priority: `2`
* Units (Story Points): `2`
* `There may be more than one projects in the workspace, and each projects may have many files, which may have many methods, and so forth. Therefore, it would be nice if the view is arranged in a tree format, so that all the entities are arranged in a logical order of project > package > source > methods. This would make it more straightforward to understand the plugin.`

T_S11

* Title: `Display tutorial guide on metrics`
* Priority: `2`
* Units (Story Points): `5`
* `Display tutorial guide page when double-click metric name.`

T_S12

* Title: `Generate Textual Report`
* Priority: `2`
* Units (Story Points): `2`
* `Generate program metric report by file (pdf, txt, etc). Report mus provide all the given metric values and evaluation of system.`

T_S13

* Title: `Save metric information to log`
* Priority: `2`
* Units (Story Points): `2`
* `Make log file by push 'Log' button. Log includes all metric value of classes and methods. This enables keeping track of progress in development.`

T_S15

* Title: `Real time Update`
* Priority: `5`
* Units (Story Points): `1`
* `Whenever any changes are made to the code on the files on the workspace, the changes are applied to the metrics and the metrics are recalculated real-time according to the changes made. So when we save the file, all the thing have to change automatically`

T_S16

* Title: `Double Click to display source`
* Priority: `2`
* Units (Story Points): `5`
* `If all the tree structure is all open at once, it may be too long to see in one screen - therefore, for a non-terminal object such as packages or projects, it would be better to enable expanding and collapsing to open and close any nonterminal nodes at will. And when double click the method, it will connect to method body.`

T_S17

* Title: `Metric search on UI`
* Priority: `2`
* Units (Story Points): `5`
* `In Green Tea plug-in, user can search the class or method by typing keyword on "type filter text" box.`

T_S18

* Title: `Metric filter on UI`
* Priority: `2`
* Units (Story Points): `5`
* `In Green Tea plug-in, user can see the filtered tree by typing keyword on "type filter text" box.`

T_S19

* Title: `State-indicating Icons`
* Priority : `3`
* Units (Story Points): `3`
* `The plugin does show the actual values of metrics, but the values themselves does not actually indicate how efficient the code is. Therefore, depending on whether each value of metric indicates a good/moderate/bad condition, it would be nice to have different icons representing the state of the metric values to enable the determination of the code's efficiency.`
