# bateman-ocr
Tools and experiments in the OCR of the Bateman Manuscripts

This repository is for hosting a number of projects and experiments around the
efforts to OCR the Bateman Manuscript volumes.

All Java code should use the code format obtained by loading
EclipseJavaFormat_APS.xml into Eclipse
(Window|Preferences|Java|Formatter|Import...)

## Sub-project Layout

This project is to analyse the layout of pages extracted from the Bateman
Manuscript volumes in order to identify areas of the page that correspond to
different categories including:
* Displayed expressions
* Embedded expressions
* Tables
* Table Cells
* Diagrams
* Text
* Headings
* Page headers/footers
and any further categories that might appear relevant.

An area identification should consist of the category name, the minimal bounding
box that contains all the connected components that belong to that area and a
list of the connected components involved. Thus two displayed equations on
separate lines would be identified as two separate areas, each identified as a
displayed equation and each having associated with it the connected components
of the symbols that are part of the equation.

Areas can be nested: A Table contains Table Cells, each of which may contain
a text line which in turn may contain embedded expressions.

No image analysis is required as the data about all connected components on the
page is provided in associated comma separated value files. There is one line in
the CSV file for each connected component which includes, among other
information that is not relevant for this project, the bounding box of the
connected component on the page. This CSV information does not currently include
the identification of the character corresponding to that connected component,
but may be modified in the future to do so.

The analysis should be based on a spatial analysis of the relative positioning
of the connected components: Even without indentifying characters, one can see
that the connected components of text has a much more regular spacing and layout
than has mathematical expressions. An analysis of the white space between
components can be of significant guidance in this task.

The system should output the information about the areas as JSON files.

To evaluate the system, a visual representation of the areas found and their
types should be overlaid on the image of underlying page. This allows for quick
manual validation.

## Sub-project Ground Truth Engine

The ground truth engine is a tool to assist in manual classification of large
numbers of connected components in order to provide training and test sets for
character recognisers. In general and within certain limits, the larger and more
accurate the ground truth sets of character identifications are, the better the
character recogniser can be. Typically, manually assigning a ground truth is a
very labour intensive and tedious job. This engine is intended to significantly
speed up and simplify this work.

The basic engine simply allows visually iterating through all the connected
components on the page and allowing the user to enter identifications for them.
However, improved support can be added in terms of taking feature vectors, which
have been pre-calculated for each connected component and provided in the
associated data files, and using them to cluster the components into similar
ones. This allows displaying whole groups of visually similar connected
components to be displayed together. The whole group, of possibly many connected
components, can then be assigned a ground truth identification in one step. Of
course, the clustering will not be perfect and an identified group may contain
connected components that are not the same character as others in the group (a
typical case would be where, because of their visual similarity, the digit "1"
is clustered into the same group as the lowercase letter "l" and the upper case
letter "I"). The user interface to the system should support easy manual marking
of connected components that do not belong and their removal from the group.
Such marking and removal can then be used in feeding back information to the
clustering algorithm to improve it and help reduce further errors.

The input data includes a set of page images from the Bateman Manuscript
volumes, a CSV file for each page that includes the bounding box and a feature
vector for each connected component, and a set of image clips for each connected
component on the page.

A great deal of attention should be paid to the clustering algorithm and the
user interface to minimise key strokes and mouse interactions, thus assisting
the human user in constructing the ground truth with least time and effort.

The final output should be a JSON file containing the ground truth
identifications, associating each connected component to its character
identification.




