/**
 * 菜单配置，如果希望路由可以在菜单上显示，则在这里配置
 * @property {String} title 优先显示的菜单显示名字，如果已经配置多语言，可以删除此项
 * @property {String} name 页面路由名字，菜单唯一标识
 * @property {String} icon 菜单图标
 * @property {Array} children 子菜单项
 */
export let menu = [
    {
        name: 'sys_mgmt',
        icon: 'md-folder',
        children: [
            { name: 'add_trade'},
            { name: 'trade_list'}
        ]
    }
];
