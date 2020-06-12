# HTree

## Props

<!-- @vuese:HTree:props:start -->
|Name|Description|Type|Required|Default|
|---|---|---|---|---|
|tree|树形数据，节点属性|`Array`|`false`|-|
|childKey|子节点属性名|`String`|`false`|'children'|
|titleKey|显示属性名|`String`|`false`|'title'|
|render|自定义渲染节点内容|`Function`|`false`|(h, p) => h(...p)|
|checkbox|开启 checkbox 多选|`Boolean`|`false`|-|
|each|初始化每一个节点回调函数，参数是节点对象。（例如要展开、勾选全部节点，可以在此处理）|`Function`|`false`|noop|
<!-- @vuese:HTree:props:end -->


## Events

<!-- @vuese:HTree:events:start -->
|Event Name|Description|Parameters|
|---|---|---|
|select|-|-|
|expand|-|-|
<!-- @vuese:HTree:events:end -->


## Slots

<!-- @vuese:HTree:slots:start -->
<!-- @vuese:HTree:slots:end -->

## Methods

<!-- @vuese:HTree:methods:start -->
<!-- @vuese:HTree:methods:end -->