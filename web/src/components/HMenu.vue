<template>
    <Menu
        class="h-menu"
        ref="menu"
        width="auto"
        theme="dark"
        :active-name="$route.name"
        :open-names="open"
        @on-select="pushState"
    >
        <MenuNode v-for="n in menu" :node="n"/>
    </Menu>
</template>

<script>
function getTitle(vm, node) {
    return node.title || vm.$t(`page.${node.name}.title`);
}

const MenuNode = {
    name: 'MenuNode',
    props: { node: Object },
    components: { MenuSub: null },
    render(h) {
        let node = this.node;
        return h(node.children ? 'MenuSub' : 'MenuItem', { props: node }, [
            h('Icon', { props: { type: node.icon } }),
            h('span', getTitle(this, node))
        ]);
    }
};

const MenuSub = {
    props: {
        children: Array,
        name: String,
        title: String,
        icon: String
    },
    components: { MenuNode },
    render(h) {
        let slots = [
            h('template', { slot: 'title' }, [
                h('Icon', { props: { type: this.icon } }),
                h('span', getTitle(this, this))
            ])
        ];

        if (this.children) {
            this.children.forEach(function(node) {
                slots.push(h('MenuNode', { props: { node } }));
            });
        }
        return h('Submenu', { props: { name: this.name } }, slots);
    }
};

// 由于 iView Menu组件存在不合理性，这里通过组件循环引用纠正
MenuNode.components.MenuSub = MenuSub;

export default {
    name: 'HMenu',
    props: {
        // 菜单树 { name, title, icon, children }，详见 ../views/frame/data.js
        menu: Array,
        open: Array
    },
    components: { MenuNode, MenuSub },
    methods: {
        pushState(event) {
            this.$router.push({
                name: event
            });
        }
    },
    watch: {
        open() {
            this.$nextTick(() => this.$refs.menu.updateOpened());
        }
    }
};
</script>

<style lang="less">
@import '../style/variables';

.h-menu.ivu-menu-dark {
    .ivu-menu-item {
        &.ivu-menu-item-active.ivu-menu-item-selected {
            color: white !important;
            background-color: @red !important;
        }
    }

    .ivu-menu-item:hover,
    .ivu-menu-submenu > .ivu-menu-submenu-title:hover {
        color: @red !important;
    }
}
</style>
