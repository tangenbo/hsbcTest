const lodash = require('lodash');
const util = require('@js/common');
const modal = 'trade_list';

export const columns = {
    id: {
        key: 'id',
        width: 45,
        displayInTable: true
    },
    tradeDate: {
        key: 'tradeDate',
        isDate: true,
        displayInTable: true,
        props: {
            type: 'date',
            format: 'dd/MM/yyyy'
        }
    },
    tradeAmount: {
        key: 'tradeAmount',
        width: 120,
        displayInTable: true
    },
    tradeType: {
        key: 'tradeType',
        width: 170,
        displayInTable: true
    },
    currencyPair: {
        key: 'currencyPair',
        width: 170,
        displayInTable: true
    }
};

const config = {
    getRequest: {
        url: '/rest/trade?',
        method: 'get'
    },
    removeRequest: {
        url: '/rest/trade/',
        method: 'delete',
        data: {}
    }
};

export const columnLocalePath = 'page.' + modal + '.columns';
const operationLocalePath = 'common';
const modalTitleLocalePath = 'page.' + modal + '.modalTitle';

export default {
    getCustomOptions: function () {
        return {
            searchFormData: {searchValue: '', vehicle_type: undefined, status_summary: undefined, type_name: undefined},
            defaultSearchFormData: {
                searchValue: '',
                vehicle_type: undefined,
                status_summary: undefined,
                type_name: undefined
            },
            searchFormRules: {},
            typeNameOptions: [],
            editFormData: {},
            imageFormData: {},
            transFormModels: {},
            totalRows: 0,
            pageNumber: 1,
            tableLoading: false,
            detailTable: {data: [], columns: []},
            detailTableLoading: false,
            detailPageNumber: 1,
            detailTotalRows: 0,
            modalTitle: '',
            formModalOpen: false,
            btnActive: '',
            table: {
                columns: [],//dynamically get locale for locale changing to work
                data: []
            },
            editFormControl: columns,
            tableCurd: {//TableCurd use this v-bind value to config view options
                toolbar: [],
                action: [{key: 'delete'}],
                formDefault: {org_type: 1},
                formLabelWidth: 80
            }
        };
    },
    getCustomMethods: function () {
        return {
            onTableHandle(btn, info) {
                let vm = this;
                vm.btnActive = btn.key;
                vm.editFormRules = {};
                vm.detailPageNumber = 1;
                switch (btn.key) {
                    case 'delete':
                        vm.editFormData = {id: info.row.id};
                        vm.$Modal.confirm({
                            title: vm.$t(operationLocalePath).delete + vm.$t(modalTitleLocalePath),
                            content: vm.$t('component.table-curd.delete-ack'),
                            onOk: vm.onEditSubmit,
                            onCancel: vm.onModalCancel
                        });
                        break;
                }
            },
            onEditSubmit() {
                let vm = this;
                util.showWaiting(true);
                if (vm.btnActive === 'delete') {
                    return removeData(vm, vm.editFormData);
                }
            },
            onQuery() {
                this.pageNumber = 1;
                getData(this);
            },
            onModalCancel() {
                this.formModalOpen = false;
            },
            getTable() {
                let vm = this;
                updateColumnTitles(vm);
                vm.table.fields = util.getTableColumns(columns);
                return vm.table;
            },
            pageChanged(page) {
                this.pageNumber = page;
                getData(this);
            }
        };
    }
};

/**
 * get data to display in table
 */
function getData(vm) {
    vm.tableLoading = true;
    let option = lodash.cloneDeep(config.getRequest);
    option.url += util.pagingParams(vm.pageNumber);

    let tableData = [];
    return vm.$axios(option)
        .then(function (res) {
            tableData = res.data.rows;
            vm.totalRows = res.data.count;
            util.parseRowsFormatColumns(columns, tableData);
            util.renderTable(vm, columns, tableData);
        }).catch(lodash.partial(util.errorResult, vm));
}

function removeData(vm, data) {
    let removeOption = lodash.cloneDeep(config.removeRequest);
    removeOption.data = {};
    removeOption.url += data.id;

    return vm.$axios(removeOption)
        .then(function () {
            util.successResult(vm);
            getData(vm);
        }).catch(lodash.partial(util.errorResult, vm));
}

function updateColumnTitles(vm) {
    lodash.forOwn(columns, function (v) {
        v.title = vm.$t(columnLocalePath)[v.key];
    });
}
