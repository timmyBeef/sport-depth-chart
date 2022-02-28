# Sport Depth Chart RESTful Backend API
## Tech Stack:
* Java 11
* Gradle
* SpringBoot
* Spring Web
* Spring Data JPA
* Lombok
* Mapstruct
* H2 in-memory DB
* PostgreSQL
* Docker
* Docker compose
* Flyway
* Spring Boot Test
* JUnit
* Mockito
* MockMvc
* Swagger API

### This is the SpringBoot based RESTful API. It supports the below functions:

* Application support H2 in-memory DB and Postgres DB, H2 in-memory DB is being used by default.
* DB data is pre-populated by Flyway, SQL scripts can be found here
  ![](https://i.imgur.com/Dps2sYF.png)


## Topics
1. [How to run this application](#How-to-run-this-application)
2. [Requirements Analysis](#Requirements-Analysis)
   a. [Entity Relationship Diagram (ERD) for Sport Depth Chart](#Entity-Relationship-Diagram-ERD-for-Sport-Depth-Chart)
   b. [Implementation Thought Explanation](#Implementation-Thought-Explanation)
4. [Swagger API definition](#Swagger-API-definition)
5. [How to use API](#How-to-use-API)

# How to run this application

There are multiple ways to run application

### 1. start from IntelliJ IDEA
* the default application.yaml will run profile h2, you can change it

![](https://i.imgur.com/lFUeync.png)


* For Lombok and Mapstruct, should enalble annotation processing in IntelliJ IDEA
  ![](https://i.imgur.com/HWwNlVc.png)

* click Run
  ![](https://i.imgur.com/xZ1xCin.png)


### 2. start with H2 in-memory DB
* How to install gradle: https://gradle.org/install/
* Run the command to build the whole project: **gradle clean build**
* start the application with H2 in-mem DB: **java -jar ./build/libs/sport-depth-chart-0.0.1-SNAPSHOT.jar --spring.profiles.active=h2**

### 3. start with Postgres DB
* Run the command to build the whole project: **gradle clean build**
* with docker-compose.yaml, so you can only docker-compose up todos-postgres
* start the application with Postgres DB: **java -jar ./build/libs/sport-depth-chart-0.0.1-SNAPSHOT.jar --spring.profiles.active=postgres**

### 4. start with docker & docker-compose
* Run the command to build the whole project: **gradle clean build**
* run **docker-compose up**, then application and postgres DB are both up and running.


# Requirements Analysis and System Design
## Entity Relationship Diagram (ERD) for Sport Depth Chart
![](https://i.imgur.com/FJ3FymC.png)
* For the scale of the solution, I design the sport and team table, which means we can have more sports and more teams for different sports.

* From the requirements, it mentioned "A player can be listed on the Depth Chart for multiple positions", so we can use a link table "player_position" to represent this many to many relationships (usually use one to many relationships on both player and position tables).

* For more sports and teams, player and position table's PK both use increment id (BIGSERIAL).
* I found a tricky thing in position. From the depth chart, the TE position appears two times, so I create a column "real_position_name" in position table. In that way, we can insert data by position_name with "TE1", "TE2", and show the real position name with column "real_position_name".

* Here is the position table's data:
  ![](https://i.imgur.com/ZT7dpn2.png)

## Implementation Thought Explanation

* for more sports, teams, so all the API needs the sportId, teamId, I assumed frontend already have these ids in UI.
* In my folloing explanations, a "depth list" actually the player_postion table data, it represents the depth chart data in particular position
  ex: "LWR-(#13, Evans, Mike),(#1, Darden, Jaelon),(#10, Miller, Scott)"

in DB, after join table(player, position, player_position), data looks like this:
![](https://i.imgur.com/MXcEJ3J.png)


## addPlayerToDepthChart(position, player, position_depth)
There are 2 cases in addPlayerToDepthChart()
case 1: adding a player without a position_depth,
case 2: adding a player with a position_depth

For both case 1, case 2,
avoid the player existing in this position's depth list
call removePlayerFromDepthChart() first, so all the players will shift to the right depth

* case 1: adding a player without a position_depth
1. call removePlayerFromDepthChart() first
2. find current max depth
3. add this player with position_depth with current max depth + 1

* case 2: adding a player with a position_depth
1. For consecutive depth, check the position_depth is too big? because I hope each player's position_depth in this depth list is consecutive.
2. call removePlayerFromDepthChart() first
3. shift other players' depth + 1 (if other players' depth is bigger than or equal to input position_depth)
4. add this player to a particular position_depth

## removePlayerFromDepthChart(position, player)
1. shift other players' depth - 1 (if other players' depth is bigger than removed player's depth)
2. remove this player in this depth list

## getBackups(position, player)
1. find this player's position depth in this position
2. find backups (other players' position depth is bigger)

## getFullDepthChart()
Output the depth chart


# Swagger API definition
if you start the application successful, the Swagger API is here:
http://localhost:8080/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config
![](https://i.imgur.com/3TZ2Mhk.png)

# How to use API

## GET api/v1/depth-chart/all
For convenience, you can directly call this API without input (I have hard code with sportId is 1 (NFL)), you also can find some names to use in other API.

the response shows the depth chart
![](https://i.imgur.com/I1XMtdP.png)



## POST api/v1/depth-chart/

the original depth chart:
![](https://i.imgur.com/OLGJD8M.png)


### case 1: adding a player without a position_depth,
```json=
{
  "sportId": 1,
  "teamId": 1,
  "positionName": "TE1",
  "playerName": "Smith, Donovan"
}
```
![](https://i.imgur.com/8jEAXI4.png)

After execution:
In depth chart, the first position of TE has "Smith, Donovan" at the last
![](https://i.imgur.com/36OUrCG.png)



### case 2: adding a player with a position_depth
```json=
{
  "sportId": 1,
  "teamId": 1,
  "positionName": "LWR",
  "playerName": "Smith, Donovan",
  "positionDepth": 2
}
```
After execution:
In depth chart, "Smith, Donovan" in position "LWR" and positionDepth is 2
![](https://i.imgur.com/tA9PYaI.png)

## DELETE api/v1/depth-chart/
![](https://i.imgur.com/r2JpbUY.png)

```json=
{
  "sportId": 1,
  "teamId": 1,
  "positionName": "LWR",
  "playerName": "Smith, Donovan"
}
```
get right response
![](https://i.imgur.com/B0xSgy7.png)

In depth chart, "Smith, Donovan" in position "LWR" has gone.
![](https://i.imgur.com/ITKBHDK.png)

# That's all! hope you'll like it!
