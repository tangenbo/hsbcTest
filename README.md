# HSBC-WEB

## you need both backend and frontend to be started
## Backend setup under windows system
```
put jdk under java dir, it is hard to push it to git
maven\bin\mvn package
setup a mysql server under db dir, it is hard to push it to git
db\bin\mysqld --defaults-file=properties\mysql.properties --initialize-insecure --basedir=db --datadir=data
db\bin\mysqld --defaults-file=properties\mysql.properties --basedir=db --datadir=data --port=3306 --bind_address=127.0.0.1 --socket=db\mysql.socket --lc_messages=en_US
you may need to update port number in properties/mysql.client.properties if that is already occupied
db\bin\mysqladmin -uroot password root
db\bin\mysql --user=root -p
> source initDb.sql
exit
startServer.cmd
```

### frontend setup under windows system

```
cd web
npm install
npm run serve
use http://localhost:8003 to access
```
