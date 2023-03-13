# CustomUnitConverter

A JSP webpage that allows user to do simple CRUD on a DerbyDB database and perform calculations.

The back-end program can convert a value from one unit to another if any path between those two units is found - the conversion doesn't need to defined directly but can be a string of many different conversions, ultimately leading from unit A to unit B.

Complex conversions (consisting of many operations, i. e. Celsius to Fahrenheit) can be defined.

Calculations can be done in reverse order (i. e. if there is a defined conversion from Celsius to Fahrenheit, then the program can convert from Fahrenheit to Celsius without needing an extra entry).

Program tracks recent calculations through cookies and session data.

Technologies used: Java, Java servlets, JSP, JPA, DerbyDB, Glassfish, plain CSS/JS.
