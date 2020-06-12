# HSBC-WEB

## Project setup
```
npm install
```

### Compiles and hot-reloads for development

```
npm run serve
```
or
```
npm start
```
`npm run serve`会使用代理数据，依赖代理服务器，在[vue.config.js](https://cli.vuejs.org/zh/config/#devserver-proxy)进行配置；
`npm start`不使用代理，而是使用本地服务器进行数据模拟，因此适合任何场合。

### Compiles and minifies for production
```
npm run build
```

### Lints and fixes files
```
npm run lint
```

## 概要

### JavaScript

项目使用 [Vue.js](https://cn.vuejs.org/) 基于 [Vue CLI 3](https://cli.vuejs.org/zh/) 构建；

还有 Vue 全家桶：
[Vue Router](https://router.vuejs.org/zh/)；
[Vuex](https://vuex.vuejs.org/zh/)。

ES6 语法；

ESLint 检查；

### CSS

预处理器使用LESS；

引入了 Bootstrap 4 的
[工具类](https://getbootstrap.com/docs/4.1/utilities/colors/)

### UI 框架

使用 [iView](https://www.iviewui.com/)，并根据 Bootstrap 4 的配色定制（为了配合 Bootstrap 4 工具类）。

### 其他库

- JS工具库：[lodash](https://lodash.com/)

- Ajax库：[axios](https://github.com/axios/axios)

- 国际化方案：[vue-i18n](http://kazupon.github.io/vue-i18n/)


## 开发规范

[Vue 风格指南](https://cn.vuejs.org/v2/style-guide/)

值得注意的是命名方式，前端项目通常采用下面命名方式

- 类、组件（**index.vue**除外，因为**vue-loader**会自动读取指定目录的index.vue）：PascalCase

- 变量：camelCase

- 文件、目录：kebab-case


## 开发文档

### Docute 文档

Docute 文档利用 [vuese](https://github.com/HcySunYang/vuese) 生成。

至于哪些代码会被生成文档，目前主要是公共组件，详见[vuese 配置](vuese.config.js)。

首次使用，需要安装 
```
npm install -g vuese
```

改动了组件，需要更新文档。
```
vuese gen
```

查看文档
```
vuese serve --open
```

由于 vuese 可能不够智能，有些使用了render函数的组件无法被识别，因此可能需要进入组件看注释。

### Markdown 文档

- [公共组件](doc/README.md)
- [页面模板](src/views/template/README.md)
- [公共逻辑](src/script/README.md)
- [模拟数据](server/README.md)

## 项目结构

```
.
├─dist
├─public
├─doc
├─server
│  └─static
└─src
    ├─assets
    ├─components
    ├─locale
    ├─script
    ├─style
    └─views
        ├─error
        ├─frame
        ├─login
        └─template
```
开发用到的目录见下：

### src

项目开发源码。路径别名：@

- src/components/

    全局 Vue 组件。路径别名：@c

- src/views/

    局部视图，特指页面，每个视图独立一个文件价存放。路径别名：@v

- src/style/

    全局样式。零散的全局样式全部在 **common.scss** 文件编写，成模块的全局样式建立新文件编写。路径别名：@css

- src/assets/

    项目公共资源。路径别名：@a

- src/script/

    项目公共JavaScript代码。路径别名：@js

    - src/script/api.js

        定义公共接口调用方法

    - src/script/common.js

        常量，工具函数。

    - src/script/directive.js

        Vue全局指令。

    - src/script/filter.js

        Vue全局过滤器。

    - src/script/router.js

        Vue Router 配置。

    - src/script/store.js

        Vuex 全局数据。

    - src/script/ajaxPlugin.js

        封装成 Vue 插件形式的 axios，并做默认配置。引用时不需要引用该文件，直接引用 axios 就行。

- src/locale/

    国际化语言包以及配置。

- src/App.vue
    根组件。只有一个路由入口，和引用全局样式。

- src/main.js
    webpack 打包的 [入口](https://webpack.docschina.org/concepts/#%E5%85%A5%E5%8F%A3-entry-) 文件。

### server

模拟服务器数据。

- server/index.js

    基于 [Express](http://expressjs.com/zh-cn/) 对请求做模拟响应；
    支持 [Mock.js](http://mockjs.com/) （但不直接应用到项目代码当中，而是用在 Express 路由里面）。**废弃**

- server/static/

    用于模拟请求的静态json数据。若需要动态的结果，可在上面文件配置。


### public

任何放置在 public 文件夹的静态资源都会被简单的复制，而不经过 webpack。
[查看详情](https://cli.vuejs.org/zh/guide/html-and-static-assets.html#public-%E6%96%87%E4%BB%B6%E5%A4%B9)


### doc

存放 Docute 文档。**实验**

### vue.config.js

Vue CLI 3 配置文件
[查看详情](https://cli.vuejs.org/zh/config/#vue-config-js)
