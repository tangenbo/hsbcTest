# 本地模拟数据

作用见根目录的**README.md** server 处。

## 使用静态 json 文件（推荐）

例如访问：/json/a/b/c，会读取 server/static/a-b-c.json 内容返回。

优点：

- 无需逻辑上的配置，新建文件即可；
- 数据可固化。

缺点：

- 无法返回动态数据。


## 直接使用 Express 路由

为**server/index.js**添加，一个路由，可调用`Mock.mock`获取随机数据。

优点：

- 动态模拟数据

缺点：

- 需要写逻辑