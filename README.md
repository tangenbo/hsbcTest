# HSBC-WEB

## you need both backend and frontend to be started
## Backend setup under windows system
```
put java under java dir, it is hard to push it to git
maven\bin\mvn package
setup a mysql under db dir, it is hard to push it to git
you may need to update port number in properties/mysql.client.properties
source initDb.sql 
startServer.cmd
```

### frontend setup under windows system

```
cd web
npm install
npm run serve
use http://localhost:8003 to access
```
