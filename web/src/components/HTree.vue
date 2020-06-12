<!-- 树形组件 -->
<template>
    <div class="h-tree">
        <TreeBody v-bind="$props"/>
    </div>
</template>

<script>
import { noop } from '@js/common';
import TransitionCollapse from '@c/TransitionCollapse';

// 数容器组件
const HTree = {
    name: 'HTree',
    props: {
        /**
         * 树形数据，节点属性
         * @property {Boolean} expand 展开节点
         * @property {String} [title] 节点显示名
         * @property {Array} [childKey] 子节点
         * @property {Boolean} checked checkbox 选中
         * @property {Boolean} disabled checkbox 禁用
         * @property {Boolean} indeterminate 子节点 checkbox 部分选中，通常会自动设置，但是在异步获取子节点时需要手动设置
         * @property {Boolean} hide 隐藏节点
         */
        tree: Array,
        // 子节点属性名
        childKey: { type: String, default: 'children' },
        // 显示属性名
        titleKey: { type: String, default: 'title' },
        /**
         * 自定义渲染节点内容
         * @param {Function} createElement Vue 创建元素方法
         * @param {Array} pTitle 原本需要传到`createElement`的参数
         * @param {Object} node 节点对象
         */
        render: { type: Function, default: (h, p) => h(...p) },
        // 开启单选
        radio: Boolean,
        // 开启 checkbox 多选
        checkbox: Boolean,
        /**
         * 初始化每一个节点回调函数，参数是节点对象。（例如要展开、勾选全部节点，可以在此处理）
         * @param {Object} node 节点对象
         */
        each: { type: Function, default: noop }
    },
    provide() {
        const vm = this;
        return {
            treeRoot: vm,
            selectNode: this.selectNode,
            expandNode: this.expandNode,
            checkedNode: this.checkedNode
        };
    },
    components: {},
    data() {
        return {
            select: {},
            uninit: false
        };
    },
    created() {
        this.init(this.tree);
    },
    methods: {
        /**
         * 选择节点
         * @export
         */
        selectNode(node = {}) {
            let old = this.select,
                select = old._id === node._id ? {} : node;

            this.select = select;
            this.$emit('select', select, old);
            return this;
        },
        /**
         * 展开节点
         * @export
         */
        expandNode(node) {
            this.$set(node, 'expand', !node.expand);
            this.$emit('expand', node);
            return this;
        },
        /**
         * checkbox 选中节点
         * @export
         */
        checkedNode(node, checked) {
            this.$set(node, 'indeterminate', false);
            this.checkboxChild(node, checked);
            this.checkboxParent(node);
            this.$emit('checkbox-change', checked, node);
            return this;
        },
        /**
         * 获取 checkbox 选中的所有节点（测试）
         * @export
         */
        getCheckedNodes() {
            const { childKey } = this;

            function each(nodes, result) {
                nodes.forEach(function(node) {
                    const children = node[childKey];
                    if (Array.isArray(children)) each(children, result);
                    else if (node.checked) result.push(node);
                }, this);
                return result;
            }

            return each(this.tree, []);
        },
        /**
         * 递归初始化每个节点
         */
        init(tree, parent) {
            const { childKey } = this;

            tree.forEach(function(node, i) {
                const children = node[childKey];

                if (parent) {
                    node._parent = parent;
                    node._id = `${parent._id}.${childKey}[${i}]`;

                    if (node.checked) this.checkboxParent(node);
                } else {
                    node._id = `[${i}]`;
                }

                this.each(node);

                if (Array.isArray(children)) this.init(children, node);
            }, this);
        },
        checkboxChild(node, checked) {
            let children = node[this.childKey];
            this.$set(node, 'checked', checked);
            if (children)
                children.forEach(v => this.checkboxChild(v, checked), this);
        },
        checkboxParent(node) {
            let parent = node._parent;

            if (parent) {
                let children = parent[this.childKey];
                let every = children.every(v => v.checked);

                this.$set(parent, 'checked', every);
                this.$set(
                    parent,
                    'indeterminate',
                    every
                        ? false
                        : children.some(v => v.checked || v.indeterminate)
                );

                return this.checkboxParent(parent);
            }
        }
    },
    watch: {
        tree: {
            handler(value) {
                // 这里 init 必须传入一个参数
                this.init(value);
            },
            deep: true
        }
    }
};

HTree.components.TreeBody = {
    name: 'TreeBody',
    props: Object.keys(HTree.props),
    components: { TransitionCollapse },
    inject: ['treeRoot', 'selectNode', 'expandNode', 'checkedNode'],
    render(h) {
        const vm = this;
        // ul slots
        let slots = this.tree.map(function(node) {
            const children = node[this.childKey];

            // li createElement 参数
            let liData = {
                    class: ['node', node.class],
                    key: node._id,
                    directives: [{ name: 'show', value: !node.hide }]
                },
                liSlots = [
                    /**
                     * 0 状态图标
                     * 1 checkbox
                     * 2 标题 || render
                     * 3 子节点
                     */
                ];

            let pIcon = [
                    'Icon',
                    {
                        class: ['btn-expand', 'invisible'],
                        props: { type: 'ios-arrow-forward' },
                        on: {
                            click: () => this.expandNode(node)
                        }
                    }
                ],
                pTitle = [
                    'div',
                    {
                        class: 'title',
                        attrs: { 'data-select': 'native title' }
                    },
                    node[this.titleKey]
                ];

            if (children) {
                // 子节点
                liSlots[3] = h('TransitionCollapse', [
                    h('TreeBody', {
                        props: Object.assign({}, this.$props, {
                            tree: children
                        }),
                        directives: [{ name: 'show', value: node.expand }]
                    })
                ]);
                // 删掉 invisible 类
                pIcon[1].class[1] = node.expand ? 'rotate' : undefined;
            }

            if (this.radio) {
                liData.on = {
                    click(event) {
                        // 点击带上 data-select 的元素才能被选中
                        if (!event.target.dataset.select) return;
                        event.stopPropagation();
                        vm.selectNode(node);
                    }
                };
                if (this.treeRoot.select._id === node._id)
                    liData.class.push('select');
            }

            // 状态图标
            liSlots[0] = h('transition', { props: { mode: 'out-in' } }, [
                node.loading
                    ? h('Spin', {
                          class: 'spin',
                          props: { size: 'small' }
                      })
                    : h(...pIcon)
            ]);
            // checkbox
            if (vm.checkbox) {
                liSlots[1] = h('Checkbox', {
                    props: {
                        value: node.checked,
                        disabled: node.disabled,
                        indeterminate: node.indeterminate
                    },
                    on: {
                        'on-change': checked => vm.checkedNode(node, checked)
                    }
                });
            }

            // render
            let fn = node.render || this.render;
            liSlots[2] = fn(h, pTitle, node);

            return h('li', liData, liSlots);
        }, this); // map end

        return h(
            'transition-group',
            { class: 'tree', props: { tag: 'ul', name: 'fade' } },
            slots
        );
    }
};

export default HTree;
</script>

<style lang="less">
@import '../style/variables';

@pl: 1rem;
@quarter: 0.25rem;

.h-tree {
    .m-y(-@quarter);
    overflow: auto;

    .tree {
        list-style: none;

        .node {
            .m-y(@quarter);
            white-space: nowrap;

            & > .tree {
                padding-left: @pl;
            }

            & > .title:hover {
                background-color: @gray-200;
            }

            &.select > .title {
                background-color: @primary;
                color: white;
            }
        }

        .btn-expand {
            cursor: pointer;
            margin-right: 0.2rem;
            transition: transform 0.3s;
            &.rotate {
                transform: rotate(90deg);
            }
        }

        .spin {
            width: @pl;
            display: inline-block;
            vertical-align: baseline;
        }

        .title {
            .p-x(@quarter);
            display: inline-block;
            font-weight: normal;
            cursor: pointer;
            transition: background-color 0.3s;
            border-radius: 0.25rem;
        }
    }
}
</style>
