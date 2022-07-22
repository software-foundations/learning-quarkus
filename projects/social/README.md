# Start Quarkus project

```bash
# mvn <plugin_gorup_id>:<artifact_id>:<version>:<command use with the plugin>
mvn io.quarkus.platform:quarkus-maven-plugin:2.3.0.Final:create
```

```bash
Set the project groupId: io.github.brunoconde07
Set the project artifactId: quarkus-social
Set the project version: 1.0
What extensions do you wish to add (comma separated list): <Enter>
Would you like some code to start (yes), or just an empty Quarkus project (no): yes
```

```bash
[INFO] ========================================================================================
[INFO] Your new application has been created in /home/bruno/Documents/dev/learning-quarkus/social/quarkus-social
[INFO] Navigate into this directory and launch your application with mvn quarkus:dev
[INFO] Your application will be accessible on http://localhost:8080
[INFO] ========================================================================================
[INFO] 
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  04:30 min
[INFO] Finished at: 2022-07-14T12:01:09-03:00
[INFO] ------------------------------------------------------------------------
```

- mvnw is maven wrapper which holds the maven version

- Test project
- Maven mades the hot swap (hto deploy) of the application
```bash
cd quarkus-social
mvn compile quarkus:dev
./mvnw compile quarkus:dev
```

```console
Listening for transport dt_socket at address: 5005
__  ____  __  _____   ___  __ ____  ______                                                                                                                                                 
 --/ __ \/ / / / _ | / _ \/ //_/ / / / __/                                                                                                                                                 
 -/ /_/ / /_/ / __ |/ , _/ ,< / /_/ /\ \                                                                                                                                                   
--\___\_\____/_/ |_/_/|_/_/|_|\____/___/                                                        
2022-07-14 12:11:33,026 INFO  [io.quarkus] (Quarkus Main Thread) quarkus-social 1.0 on JVM (powered by Quarkus 2.10.2.Final) started in 1.367s. Listening on: http://localhost:8080

2022-07-14 12:11:33,038 INFO  [io.quarkus] (Quarkus Main Thread) Profile dev activated. Live Coding activated.
2022-07-14 12:11:33,038 INFO  [io.quarkus] (Quarkus Main Thread) Installed features: [cdi, resteasy, smallrye-context-propagation, vertx]                                                  
                                                                                                                                                                                           
--                                                                                                                                                                                         
Tests paused                                                                                    
Press [r] to resume testing, [o] Toggle test output, [:] for the terminal, [h] for more options>
```

- Test the application

```bash
curl http://localhost:8080
curl http://localhost:8080/hello
```

# Adding dependencies on quarkus project using maven

- Use the terminal approach (quarkus cli plugin) instead of this
- <a href="https://mvnrepository.com/artifact/org.hibernate/hibernate-core/6.1.1.Final">Hibernate</a>
```xml
<dependency>
    <groupId>org.hibernate</groupId>
    <artifactId>hibernate-core</artifactId>
    <version>6.1.1.Final</version>
    <type>pom</type>
</dependency>
```
- Copy this to dependencies in pom.xml

# Adding dependencies on quarkus project quarkus plugin

- list extension

```bash
# mvn or ./mvnw
# chmod +x .mvnw
./mvnw quarkus:list-extensions
./mvnw quarkus:add-extension -Dextensions="hibernate-validator,hibernate-orm,hibernate-orm-panache"
./mvnw quarkus:add-extension -Dextensions="jdbc-h2"
./mvnw quarkus:add-extension -Dextensions="resteasy-jsonb"
```

- Reload dependencies
```bash
./mvnw dependency:resolve
```

# Create Database

```bash
sudo -iu postgres

createdb quarkus-social -O postgres
psql
\c quarkus-social

# Create password for postgres: postgres
\password
```

# Populate Database

```sql
CREATE TABLE USERS (
    id bigserial not null primary key,
    name varchar (100) not null,
    age integer not null
);
```
- List tables

```bash
\d
```

# Setup Database in application.properties

- <a href="https://quarkus.io/guides/datasource">quarkus guides datasource jdbc</a>

```
quarkus.datasource.db-kind=postgresql 
quarkus.datasource.username=quarkus
quarkus.datasource.password=quarkus

quarkus.datasource.jdbc.url=jdbc:postgresql://localhost:5432/quarkus-social
quarkus.datasource.jdbc.max-size=16
```

# Install postgresl driver with maven

```bash
./mvnw quarkus:add-extension -Dextension="jdbc-postgresql"
```

# Tips

- Use <code>PanacheEntityBase</code> instead of <code>PanacheEntity</code>
- <code>PanacheEntityBase</code> is more customizable
- Use <code>@Transactional</code> in Request handler which mades db operations
- <code>CTRL + ALT + V</code> to create a variable in intelij