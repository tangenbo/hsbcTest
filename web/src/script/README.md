# 公共逻辑

作用见根目录的**README.md** src/script/ 处。


## common.js

添加到这里的公共常量、工具函数务必增加注释，具体到函数的每一个参数。

## router.js

- 路由元信息 meta

| 属性 | 类型 | 说明 |
| - | - | - |
| tag | Boolean | 标签页 |
| public | Boolean | 不需要登录的路由 |
| projectSelect | Boolean | 显示项目选择控件 |

- 框架里面的路由，只需要配置`path`属性，`name`属性会自动配置成一样。 