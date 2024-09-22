   	<plugin>
   		<groupId>org.apache.maven.plugins</groupId>
   		<artifactId>maven-checkstyle-plugin</artifactId>
   		<version>3.3.1</version>
   		<configuration>
   			<configLocation>checkstyle.xml</configLocation>
   			<failOnViolation>true</failOnViolation>
   			<logViolationsToConsole>true</logViolationsToConsole>
   			<includeTestSourceDirectory>true</includeTestSourceDirectory>
   		</configuration>
   		<executions>
   			<execution>
   				<goals>
   					<goal>check</goal>
   				</goals>
   				<phase>compile</phase>
   			</execution>
   		</executions>
   	</plugin>
