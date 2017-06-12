# Mongo-SQL
CLI alternative mongoDB client which performs select commands entered via SQL syntax.

### To run this application:
* run your Mongo server
```
mongod
```
* run alternativeMongodb.jar file
```
java -jar alternativeMongodb.jar
```
This alternative mongoDB client supports next query format:
```
SELECT [<Projections>] [FROM <Target>]

[WHERE <Condition>*]
```
Limitations
* supports only SELECT operation
* table name = users
* supports only field query ( *, _id, firstName etc. ) without subfields
* case insensitivity on column names and sql words(select, from, where)
* all elements should have gaps between each other (select firstname, age from users where age <= 61)
* DOES NOT support GROUP BY, ORDER BY, SKIP, LIMIT, aggregate functions

Requirements
* CLI app
* Java 8
* MongoDB 3+
* Unix OS (Linux, OSX)
* Unit testing coverage
* displaying result data via pretty JSON
* supported conditions: =, !=, <>, >, >=, <, <=.
### Query examples:
```
select * from users
SELECT * FROM USERS
select * from users where age = 33
select * from users where age != 33
select * from users where age <> 33
select firstName, country from users where country = USA
sELECT FIRSTNAME, couNTRY frOM USERS wheRE counTRY = USa
select * from users where age <= 61
```
