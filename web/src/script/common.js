/**
 * 全局工具、变量
 */
import $ from 'jquery';
import lodash from 'lodash';
import constants from '@js/constants';

// 一天毫秒数
export const dayMS = 1000 * 60 * 60 * 24;

let customPageSize = null;

/**
 * 空函数
 * @function noop
 */
export function noop() {
}

/**
 * 判断请求是否成功
 * @function isSuccess
 * @param {Object} res 响应
 */
export function isSuccess(res) {
    let business = res.data;
    let propCheck = ['data', 'code', 'message'].every(v => (v in business));
    return res.status === 200 && propCheck && business.code == 0;
}

/**
 * 以JSON的方式存取localStorage数据
 * @param {String} name
 * @param {*} value
 */
export function lStorage(name, value) {
    if (arguments.length === 1) { // get
        let valueStr = localStorage.getItem(name);
        return JSON.parse(valueStr);
    } else {
        localStorage.setItem(name, JSON.stringify(value));
    }
}

export function parseRowsFormatColumns(columnsObj) {
    lodash.forOwn(columnsObj, function (column, key) {
        if (column.isDate) {
            column.exportFormatter = function (row) {
                return formatDate(row[key], constants.DATE_FORMAT) || '-';
            };
        } else if (column.control === 'DateTimePicker') {
            column.exportFormatter = function (row) {
                return formatDate(row[key], constants.DATE_FORMAT + ' ' + constants.TIME_FORMAT) || '-';
            };
        } else if (column.isMoney) {
            column.exportFormatter = function (row) {
                return formatMoney(row[key]);
            };
        } else if (column.notEmpty) {
            column.exportFormatter = function (row) {
                return row[key] === undefined || row[key] === null || row[key] === '' ? '-' : row[key];
            };
        }
    });
}

export function renderTable(vm, columns, tableData) {
    lodash.forOwn(columns, function (column) {
        let style = column.highlight ? {
            backgroundColor: '#55bfbf',
            padding: '5px',
            color: 'white',
            borderRadius: '6px'
        } : {};
        if (column.control === 'Select' && column.options && (!column.readonly || typeof column.readonly === 'function' && !column.readonly())) {
            column.render = function (h, {row}) {
                return h('Select', {
                    props: {
                        size: 'small',
                        value: row[column.key]
                    },
                    on: {
                        'on-change': (newValue) => {
                            row[column.key] = newValue;
                            column.updateUrl && vm.$axios({
                                url: column.updateUrl,
                                method: 'put',
                                data: getDataToUpdate(row, columns, column.key, newValue)
                            }).then(function () {
                                successResult(vm);
                                column.onTableChange && column.onTableChange(vm, row);
                            }).catch(lodash.partial(errorResult, vm));
                        }
                    }
                }, lodash.map(column.options, function (option) {
                    return h('Option', {
                        props: {
                            value: option.key,
                            label: option.value
                        }
                    });
                }));
            };
        } else {
            column.render = function (h, {row}) {
                return h(column.click ? 'a' : 'span', {
                    style: style,
                    on: column.click ? {
                        click: () => {
                            column.click(vm, row);
                        }
                    } : {}
                }, column.exportFormatter ? column.exportFormatter(row) : row[column.key]);
            };
        }
    });
    vm.table.columns = getTableColumns(columns);
    vm.table.data = tableData;
    vm.tableLoading = false;
}

export function getTableColumns(columns) {
    return lodash.map(lodash.filter(columns, 'displayInTable'), function (column) {
        return column;
    });
}

export function pagingParams(page) {
    return 'pageSize=' + getPageSize() + '&pageNumber=' + page + '&';
}

export function getPageSize() {
    return customPageSize || constants.PAGE_SIZE;
}

export function setPageSize(size) {
    customPageSize = size;
}

export function formatDate(date, formatStr) {
    if (!date) {
        return null;
    }

    date = new Date(date);
    var day = date.getDate();
    if (day < 10) {
        day = '0' + day;
    }
    var month = date.getMonth() + 1;
    if (month < 10) {
        month = '0' + month;
    }
    var year = date.getFullYear();
    var hour = date.getHours();
    if (hour < 10) {
        hour = '0' + hour;
    }
    var minute = date.getMinutes();
    if (minute < 10) {
        minute = '0' + minute;
    }
    var second = date.getSeconds();
    if (second < 10) {
        second = '0' + second;
    }
    return formatStr.replace('dd', day).replace('MM', month).replace('yyyy', year).replace('HH', hour).replace('mm', minute).replace('ss', second);
}

export function getEpochTimeFromDateStr(dateStr) {
    var dateParts = dateStr.split('/');
    return new Date(dateParts[1] + '/' + dateParts[0] + '/' + dateParts[2]).getTime();
}

export function getEpochTimeFromTimeStr(timeStr) {
    var timeParts = timeStr.split(':');
    return parseInt(timeParts[0]) * 3600 * 1000 + timeParts[1] * 60 * 1000 + timeParts[2] * 1000;
}

export function getCleanJson(object) {
    return JSON.parse(JSON.stringify(object));
}

export function isStringNotEmpty(value) {
    return typeof value === 'string' && value.trim().length > 0;
}

export function isEmptyString(value) {
    return typeof value === 'string' && value.trim().length === 0;
}

export function isNumber(value) {
    return typeof value === 'number' || typeof value === 'string' && /^\s*-?\d+(\.\d+)?\s*$/.test(value);
}

export function successResult(vm) {
    vm.$Message.success(vm.$t('message').successOp);
    showWaiting(false);
}

export function errorResult(vm, errorPathOrResp) {
    let options = {duration: 10, closable: true};
    options.content = errorPathOrResp.response.data;
    vm.$Message.error(options);
    showWaiting(false);
}

export function complementRules(vm) {
    let rulesObj = vm.editFormRules;
    let columnsObj = vm.editFormControl;
    lodash.forOwn(columnsObj, function (column, key) {
        rulesObj[key] = rulesObj[key] || [];
        if (column.required) {
            rulesObj[key].push({validator: lodash.partial(emptyCheck, vm)});
        }
        if (column.maxLength) {
            rulesObj[key].push({
                type: 'string',
                max: column.maxLength,
                message: vm.$t('message.max').replace('%d', column.maxLength).replace('%s', vm.$t('page.' + vm.modal + '.columns.' + column.key))
            });
        }
        if (column.minValue !== undefined) {
            rulesObj[key].push({validator: lodash.partial(minValueCheck, vm, column.minValue)});
        } else if (column.isNumber) {
            rulesObj[key].push({validator: lodash.partial(numberCheck, vm)});
        }
    });
}

export function showWaiting(show) {
    if (show) {
        $('body').append('<div class="coverage"></div>');
    } else {
        $('body .coverage').remove();
    }
}

export function getLang() {
    return lStorage('language') ? lStorage('language').split('-')[0] : 'en';
}

export function formatMoney(value) {
    if (value === undefined || value === '' || value === null) {
        return '-';
    }
    return '$' + value.toFixed(2);
}

const emptyCheck = (vm, rule, value, callback) => {
    if (value === undefined || value === null || typeof value === 'string' && value.trim() === '') {
        callback(new Error(vm.$t('message.required')));
    } else {
        callback();
    }
};

const minValueCheck = (vm, minValue, rule, value, callback) => {
    if (isStringNotEmpty(value) && (!isNumber(value) || parseFloat(value) < minValue)) {
        callback(new Error(vm.$t('message.minValue').replace('%d', minValue)));
    } else {
        callback();
    }
};

const numberCheck = (vm, rule, value, callback) => {
    if (isStringNotEmpty(value) && !isNumber(value)) {
        callback(new Error(vm.$t('message.number')));
    } else {
        callback();
    }
};

const getDataToUpdate = (row, columns, updateKey, newValue) => {
    let data = {};
    lodash.forOwn(columns, function (column, key) {
        if (key === 'id') {
            data[key] = row[key];
        }
        if (key === updateKey) {
            data[key] = newValue;
        }
    });
    return data;
};
