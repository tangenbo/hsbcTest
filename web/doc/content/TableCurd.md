# TableCurd

## Props

<!-- @vuese:TableCurd:props:start -->
|Name|Description|Type|Required|Default|
|---|---|---|---|---|
|title|显示在表格左上角的表格名字|`String`|`false`|-|
|table|支持iView的所有表格组件配置，其中 columns 配置新增下面属性|`Object`|`false`|-|
|action|每一行的行动按钮|`Array`|`false`|-|
|toolbar|右上角工具栏|`Array`|`false`|-|
|total|表格总条数|`Number`|`false`|-|
|actionColumn|行动按钮列配置|`Object`|`false`|-|
|modalEnabled|开启增删改模态框|`Boolean`|`false`|-|
|formLabelWidth|表单域标签的宽度|`Number`|`false`|64|
|formDefault|添加时表单默认值|`Object`|`false`|-|
|formRules|表单验证规则，具体配置查看 async-validator|`Object`|`false`|-|

<!-- @vuese:TableCurd:props:end -->


## Events

<!-- @vuese:TableCurd:events:start -->
|Event Name|Description|Parameters|
|---|---|---|
|handle|-|-|

<!-- @vuese:TableCurd:events:end -->


## Slots

<!-- @vuese:TableCurd:slots:start -->
|Name|Description|Default Slot Content|
|---|---|---|
|toolbar|-|-|

<!-- @vuese:TableCurd:slots:end -->


