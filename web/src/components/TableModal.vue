<!-- 表格模态框 -->
<template>
    <Modal class="editModal" v-model="open" :title="modalTitle" :loading=true :mask=true
           :mask-closable=false @on-cancel="onClose">
        <Form ref="detailEditForm" :model="editFormData" :rules="detailEditFormRules" inline>
            <FormItem v-for="v in editFormData" v-bind:key="v.key" :prop="v.key">
                <Input type="text" v-model="v.value" :placeholder="v.placeHolder"
                       style="width:270px">
                </Input>
            </FormItem>
            <FormItem>
                <Button type="primary" @click="onEditSubmit()">{{$t('common.edit')}}</Button>
            </FormItem>
        </Form>
        <TableCurd
                :table="table1"
                v-bind="tableCurd"
                :tableLoading="tableLoading"
                @handle="onTableHandle"
        />
    </Modal>
</template>
<script>
    import lodash from 'lodash';
    import TableCurd from '@c/TableCurd';
    import async from 'async';
    import {showWaiting, successResult, errorResult} from '@js/common';

    export default {
        name: 'TableModal',
        props: ['modalOpen', 'modal', 'columns', 'detailEditFormData', 'detailEditFormRules', 'parent_key', 'parent_id', 'child_key', 'onModalCancel', 'tableLoading'],
        components: {TableCurd},
        data() {
            return {
                rules: {},
                open: this.modalOpen,
                table: {
                    columns: [],//dynamically get locale for locale changing to work
                    data: []
                },
                editFormData: lodash.cloneDeep(this.detailEditFormData),
                modalTitle: '',
                tableCurd: {//TableCurd use this v-bind value to config view options
                    toolbar: [{key: 'export'}],
                    action: [],
                    formDefault: {org_type: 1},
                    formLabelWidth: 80,
                    actionColumn: {width: 77}
                }
            };
        },
        computed: {
            table1() {
                return this.table;
            }
        },
        created() {
        },
        watch: {
            modalOpen: 'onOpen'
        },
        methods: {
            onEditSubmit() {
                let vm = this;
                showWaiting(true);
                vm.$refs.detailEditForm.validate(function (valid) {
                    if (valid) {
                        updateData(vm);
                    } else {
                        errorResult(vm, 'invalidData');
                    }
                });
            },
            onOpen() {
                this.open = this.modalOpen;
                if (this.modalOpen) {
                    let vm = this;
                    vm.editFormData = lodash.cloneDeep(vm.detailEditFormData);
                    vm.modalTitle = vm.$t('pages.' + vm.modal + '.modalTitle');
                    vm.table.fields = this.columns;
                    getData(vm);
                }
            },
            onClose() {
                this.onModalCancel && this.onModalCancel();
            },
            onTableHandle() {

            }
        }
    };

    function getData(vm) {
        return vm.$axios({
            url: '/maintain/' + vm.modal + '/list?' + vm.parent_key + '=' + vm.parent_id,
            method: 'get'
        }).then(function (res) {
            vm.table.data = res.data;
        }).catch(lodash.partial(errorResult, vm));
    }

    function updateData(vm) {
        let dataToPost = {[vm.parent_key]: vm.parent_id};
        lodash.forOwn(vm.editFormData, function (value, key) {
            if (value.value !== null && value.value !== undefined) {
                dataToPost[key] = value.value;
            }
        });
        async.mapSeries([], function (child_id, cb) {
            return vm.$axios({
                url: '/maintain/' + vm.modal + '/update',
                method: 'put',
                data: lodash.assign(dataToPost, {[vm.child_key]: child_id})
            }).then(function () {
                cb();
            }).catch(cb);
        }, function (err) {
            if (err) {
                errorResult(vm, err);
            } else {
                successResult(vm);
            }
        });
    }
</script>
