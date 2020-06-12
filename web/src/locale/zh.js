import iLocale from 'iview/dist/locale/zh-CN';

let locale = {
    title: '简体中文',
    // 公共
    common: {
        enabled: '激活',
        disabled: '禁用',
        add: '添加',
        delete: '删除',
        refresh: '刷新',
        selectAll: '全选',
        unselectAll: '取消全选',
        dateTime: '日期'
    },
    // iView Message Alert Notice Tooltip
    message: {
        successOp: '操作成功',
        failOp: '操作失败',
        invalidData: '无效输入',
        max: '最多%d个字符',
        number: '必须数字',
        required: '必填',
        minValue: '>=%d'
    },
    yesNo: ['否', '是'],
    successFail: ['成功', '失败'],
    // 页面
    page: {
        // 框架
        frame: {
            language: '语言',
            'sign-out': '注销',
            'close-tags': '关闭其他',
            'toggle-language': '可能有些地方需要刷新页面才生效',
            'pwd-ack': '两次输入不一致',
            organization: '组织',
            project: '项目',
            header: 'HSBC测试'
        },
        sys_mgmt: {title: '系统管理'},
        add_trade: {
            modalTitle: '交易',
            title: '交易列表',
            columns: {
                id: 'ID',
                tradeDate: '交易日期',
                tradeAmount: '交易数量',
                tradeType: '交易类型',
                currencyPair: '货币对'
            }
        },
        trade_list: {
            modalTitle: '交易',
            title: '交易列表',
            columns: {
                id: 'ID',
                tradeDate: '交易日期',
                tradeAmount: '交易数量',
                tradeType: '交易类型',
                currencyPair: '货币对'
            }
        }
    },
    // 组件
    component: {
        'table-curd': {
            add: '添加',
            delete: '删除',
            edit: '修改',
            export: '导出',
            action: '操作',
            detail: '详情',
            'delete-ack': '确认删除该行记录？所有相关记录也会一起删除！请谨慎执行该类操作！',
            'payed-ack': '确认结清选中进账记录？结清时间未设置则会设为当前时间。',
            noSelected: '请选中至少一行'
        }
    }
};

Object.assign(locale, iLocale);

export default locale;
