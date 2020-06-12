<!-- 增强版的表格组件 -->
<template>
    <div class="table-curd">
        <Table ref="table" border v-bind="table" :loading="tableLoading" :columns="columns" :stripe=true size="small"
               @on-selection-change="selectionChanged"
               :row-class-name="rowClassNameFn">
            <div slot="header" class="px-3">
                <div class="float-left">
                    <h3>{{ title }}</h3>
                </div>
                <div class="float-right">
                    <slot name="toolbar"/>
                    <ButtonGroup size="small">
                        <Button
                                v-for="v in toolbar" :key="v.key"
                                @click="onTableHandle(v)"
                                :type="BUTTON_TYPES[v.key]"
                                v-bind="v.props"
                                style="margin-right:5px;"
                        >{{ v.title || lang(v.key) }}
                        </Button>
                    </ButtonGroup>
                </div>
            </div>
            <template>
                <div v-if="pageSize && pageNumber && pageChanged" slot="footer" class="px-3 text-right"
                     style="float:right;width:fit-content;">
                    <Page :total="total" :page-size="pageSize" :current="pageNumber" @on-change="pageChanged"
                          @on-page-size-change="pageSizeChanged"
                          :page-size-opts="[10,25,50,75,100]"
                          show-sizer show-total/>
                </div>
                <div slot="footer" class="px-3 text-left" v-if="table.selectable">
                    <Button @click="handleSelectAll(true)">{{$t('common.selectAll')}}</Button>
                    <Button @click="handleSelectAll(false)">{{$t('common.unselectAll')}}</Button>
                </div>
            </template>
        </Table>
        <!-- 自带的编辑模态框 -->
        <Modal
                v-if="modalEnabled"
                v-model="modalOpen"
                :title="modalTitle"
                :loading="modalLoading"
                :mask-closable="false"
                @on-ok="onModalConfirm"
                @on-cancel="onModalCancel"
        >
            <Form class="mt-3" :model="formData" :rules="formRules" :label-width="formLabelWidth">
                <FormItem v-for="v in formControl" :label="v.title" :prop="v.key" :key="v.key">
                    <component :is="v.control" v-bind="v.props" v-model="formData[v.key]"/>
                </FormItem>
            </Form>
        </Modal>
    </div>
</template>

<script>
    import pick from 'lodash/pick';
    import lodash from 'lodash';
    import {getPageSize, setPageSize} from '@js/common';

    export default {
        name: 'TableCurd',
        props: {
            // 显示在表格左上角的表格名字
            title: String,
            /**
             * 支持iView的所有表格组件配置，其中 columns 配置新增下面属性
             * @property {String|Object} control 表格内容编辑控件，通常传入表单组件名。如果需要传入多个组件，“函数式组件”就派上用场。
             * @property {Object} props [control]组件的属性。
             * @property {Boolean} tableHide 表格上隐藏该列
             * @property {Boolean} controlHide 编辑框上隐藏[control]
             */
            table: Object,
            // 每一行的行动按钮
            action: Array,
            // 右上角工具栏
            toolbar: Array,
            // 表格总条数
            total: Number,
            pageNumber: Number,
            pageChanged: {type: Function},
            // 行动按钮列配置
            actionColumn: Object,
            // 开启增删改模态框
            modalEnabled: Boolean,
            // 表单域标签的宽度
            formLabelWidth: {type: Number, default: 64},
            // 添加时表单默认值
            formDefault: {type: Object},
            // 表单验证规则，具体配置查看 async-validator
            formRules: {type: Object},
            rowClassNameFn: {type: Function},
            tableLoading: {type: Boolean}
        },
        data() {
            return {
                pageSize: getPageSize(),
                modalOpen: false,
                modalTitle: '',
                modalLoading: true,
                modalHandle: {row: null},
                formData: {},
                BUTTON_TYPES: {
                    edit: 'info',
                    delete: 'error',
                    detail: 'primary',
                    add: 'success',
                    export: 'warning',
                    income: 'info',
                    payed: 'primary',
                    end_time: 'info',
                    check_detail: 'success',
                    exchange: 'warning'
                }
            };
        },
        provide() {
            let formData = this.formData;
            return {
                assignData(data) {
                    Object.assign(formData, data);
                }
            };
        },
        computed: {
            columns() {
                const vm = this;
                const {table, action} = this;

                let columns = table.columns,
                    result = [];

                if (table.selectable) {
                    columns.unshift({
                        type: 'selection',
                        width: 30
                    });
                }

                if (Array.isArray(action) && action.length) {
                    let actionColumn = {
                        title: this.lang('action'),
                        key: 'action',
                        width: 250,
                        render(h, info) {
                            let slots = [];

                            action.forEach(function name(v) {
                                const btn =
                                    typeof v === 'function' ? v.call(vm, info) : v;
                                if (btn) {
                                    let data = pick(btn, [
                                        'attrs',
                                        'props',
                                        'class',
                                        'style'
                                    ]);
                                    data.props = {type: vm.BUTTON_TYPES[btn.key]};
                                    data.style = {marginRight: '5px'};
                                    data.on = {
                                        click: () => vm.onTableHandle(btn, info)
                                    };

                                    slots.push(
                                        h(
                                            'Button',
                                            data,
                                            btn.title || vm.lang(btn.key)
                                        )
                                    );
                                }
                            });

                            return h(
                                'ButtonGroup',
                                {props: {size: 'small'}},
                                slots
                            );
                        }
                    };

                    Object.assign(actionColumn, this.actionColumn);
                    columns.push(actionColumn);
                }

                columns.forEach(function (v) {
                    if (!v.tableHide) {
                        result.push(v);
                    }
                    v.align = 'center';
                });

                return result;
            },
            formControl() {
                let result = [];
                this.table.columns.forEach(function (v) {
                    if (v.control && !v.controlHide) result.push(v);
                });
                return result;
            }
        },
        methods: {
            lang(name) {
                let prefix = 'component.table-curd.';
                return this.$t(prefix + name);
            },
            openModal(btn, row) {
                btn.row = row;
                this.modalHandle = btn;
                this.modalTitle = btn.title || this.lang(btn.key);

                if (row) {
                    this.table.columns.forEach(function (v) {
                        if (v.control) {
                            const k = v.key;
                            this.formData[k] = row[k];
                        }
                    }, this);
                } else {
                    this.formData = Object.assign({}, this.formDefault);
                }

                if (btn.key === 'delete') {
                    this.$Modal.confirm({
                        title: this.modalTitle,
                        content: this.lang('delete-ack'),
                        onOk: this.onModalConfirm,
                        onCancel: this.onModalCancel
                    });
                } else {
                    this.modalOpen = true;
                }

                return this;
            },
            onTableHandle(btn, info) {
                if (this.modalEnabled)
                    this.openModal(btn, info && info.row);
                this.$emit('handle', btn, info);
            },
            onModalConfirm() {
                const fn = this.$listeners['on-ok'];
                if (typeof fn === 'function') {
                    const vm = this,
                        handle = this.modalHandle,
                        result = fn(this.modalHandle, this.formData);

                    if (handle.key === 'delete') return;

                    if (
                        result instanceof Object &&
                        typeof result.then === 'function'
                    ) {
                        result.then(
                            open => (vm.modalOpen = Boolean(open)),
                            function () {
                                vm.modalLoading = false;
                                vm.$nextTick(() => (vm.modalLoading = true));
                            }
                        );
                    } else {
                        this.modalOpen = Boolean(result);
                    }
                }
            },
            onModalCancel() {
                const fn = this.$listeners['on-cancel'];
                if (typeof fn === 'function') fn(this.modalHandle, this.formData);
            },
            selectionChanged(selected) {
                let vm = this;
                lodash.each(vm.table.data, function (rowData) {
                    rowData._checked = false;
                });
                lodash.each(selected, function (rowSelected) {
                    lodash.each(vm.table.data, function (rowData) {
                        if (rowData.id === rowSelected.id) {
                            rowData._checked = true;
                        }
                    });
                });
            },
            handleSelectAll(status) {
                this.$refs.table.selectAll(status);
            },
            pageSizeChanged(size) {
                setPageSize(size);
                this.pageChanged(1);
            }
        }
    };
</script>

<style>

    .ivu-table th, .ivu-table td {
        min-width: 100px !important;
        overflow: unset;
    }

    @media (max-width: 768px) {

        table {
            width: 100% !important;
        }

        colgroup {
            display: none;
        }

        .ivu-btn-group-small > .ivu-btn {
            margin: 5px;
        }

        .ivu-table th, .ivu-table td {
            width: 100px !important;
        }
    }
</style>