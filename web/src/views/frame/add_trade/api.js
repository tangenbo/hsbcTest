const lodash = require('lodash');
const util = require('@js/common');
const modal = 'add_trade';

export const fields = {
    tradeDate: {
        key: 'tradeDate',
        required: true,
        isDate: true,
        props: {
            type: 'date',
            format: 'dd/MM/yyyy'
        },
        placeHolder: 'DD/MM/YYYY',
        control: 'DatePicker'
    },
    tradeAmount: {
        key: 'tradeAmount',
        isNumber: true,
        required: true,
        control: 'Input'
    },
    tradeType: {
        key: 'tradeType',
        required: true,
        control: 'Input'
    },
    currencyPair: {
        key: 'currencyPair',
        required: true,
        control: 'Input'
    }
};

export const columnLocalePath = 'page.' + modal + '.columns';

export default {
    getCustomOptions: function () {
        return {
            modal: modal,
            addUrl: '/rest/trade',
            editFormData: {},
            editFormRules: {},//dynamically get locale for locale changing to work
            editFormControl: fields
        };
    },
    getCustomMethods: function () {
        return {
            onEditSubmit() {
                let vm = this;
                lodash.forOwn(vm.editFormData, function (value, key) {
                    if (typeof value === 'string') {
                        vm.editFormData[key] = value.trim();
                    }
                });
                let dataToPost = lodash.cloneDeep(vm.editFormData);
                lodash.forOwn(vm.editFormControl, function (formItemControl, key) {
                    if (formItemControl.isDate) {
                        let value = dataToPost[key];
                        if (typeof value == 'object' && value !== undefined && value !== null) {
                            dataToPost[key] = value.getTime();
                        } else if (value) {
                            dataToPost[key] = util.getEpochTimeFromDateStr(value);
                        } else {
                            dataToPost[key] = null;
                        }
                    } else if (formItemControl.isNumber) {
                        if (util.isEmptyString(dataToPost[key])) {
                            dataToPost[key] = null;
                        } else if (util.isStringNotEmpty(dataToPost[key])) {
                            dataToPost[key] = parseFloat(dataToPost[key]);
                        }
                    }
                });

                return addData(vm, dataToPost);
                /*vm.$refs.editForm.validate(function (valid) {
                    if (valid) {
                        console.error("success");
                    } else {
                        util.showWaiting(false);
                        util.errorResult(vm, 'invalidData');
                    }
                });*/
            },
            onLoaded() {
                let vm = this;
                updateInputLabels(vm);
                util.complementRules(vm);
            }
        };
    }
};

function addData(vm, data) {
    return vm.$axios({
        url: vm.addUrl,
        method: 'post',
        data: data
    }).then(function () {
        util.successResult(vm);
    }).catch(lodash.partial(util.errorResult, vm));
}

function updateInputLabels(vm) {
    lodash.forOwn(fields, function (v) {
        v.title = vm.$t(columnLocalePath)[v.key];
    });
}
