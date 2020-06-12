import iLocale from 'iview/dist/locale/en-US';

let locale = {
    title: 'English',
    // 公共
    common: {
        enabled: 'Enabled',
        disabled: 'Disabled',
        add: 'Add ',
        delete: 'Delete ',
        refresh: 'Refresh',
        selectAll: 'Select All',
        unselectAll: 'Unselect All',
        dateTime: 'Date '
    },
    // iView Message Alert Notice Tooltip
    message: {
        successOp: 'Operation succeeded',
        failOp: 'Operation failed',
        invalidData: 'Invalid input',
        max: 'At most %d characters',
        number: 'Only number',
        required: 'Required',
        minValue: '>=%d'
    },
    yesNo: ['no', 'yes'],
    successFail: ['Succeeded', 'Failed'],
    // 页面
    page: {
        // 框架
        frame: {
            language: 'Language',
            'sign-out': 'Sign out',
            'close-tags': 'Close other',
            'toggle-language': 'You may need a refresh to take effect',
            'pwd-ack': 'The two input passwords do not match',
            organization: 'Organization',
            project: 'Projects',
            header: 'HSBC Test'
        },
        sys_mgmt: {title: 'System Management'},
        add_trade: {
            modalTitle: 'Trade',
            title: 'Add Trade',
            columns: {
                id: 'ID',
                tradeDate: 'Trade Date',
                tradeAmount: 'Trade Amount',
                tradeType: 'Trade Type',
                currencyPair: 'Currency Pair'
            }
        },
        trade_list: {
            modalTitle: 'Trade',
            title: 'Trade List',
            columns: {
                id: 'ID',
                tradeDate: 'Trade Date',
                tradeAmount: 'Trade Amount',
                tradeType: 'Trade Type',
                currencyPair: 'Currency Pair'
            }
        }
    },
    // 组件
    component: {
        'table-curd': {
            add: 'Add',
            delete: 'Delete',
            edit: 'Edit',
            export: 'Export',
            action: 'Operation',
            detail: 'Detail',
            'delete-ack': 'Are you sure you want to remove this record? All other related records will be removed as well! Please be careful when doing this!',
            'payed-ack': 'Are you sure to settle selected checks? Pay time will be now if missed.',
            noSelected: 'Select at least one row.'
        }
    }
};

Object.assign(locale, iLocale);

export default locale;
