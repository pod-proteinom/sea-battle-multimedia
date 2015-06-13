just a game.

To build and deploy an application to a tomcat follow these steps:<br>
1) download project via the svn.<br>
2) edit your maven settings xml<br>
<blockquote>- you must create a server element named 'tomcat-standart', or set any other to the pom.xml.<br>
3) edit 'src/main/resources/spring/database/jdbc.properties' with and appropriate database parameters<br>
4) one of the following<br>
</blockquote><blockquote>- create a database in your mysql<br>
- run this sql script 'src/main/resources/create.sql' (it will create a database, and countries table, with some sample data)<br>
5) you may also want to change an email. It can be done in 'src/main/resources/spring/root-context.xml' file ('mailsender' bean and 'templateMessage' bean)<br>
6) run maven with those goals to make an application build, run test and become deployed:<br>
<blockquote>- TOMCAT compile war:exploded tomcat:exploded (it will create a build folder and deploy an application to a tomcat server)<br>
- JETTY compile war:exploded jetty:run<br>