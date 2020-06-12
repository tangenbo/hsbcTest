<!-- 增改明细模态框 -->
<template>
    <Modal class="dateTimeModal" v-model="open" :title="title" :loading=true :mask=true
           :mask-closable=false
           @on-ok="onEditSubmit" @on-cancel="onClose">
        <Form ref="dateTimeForm" class="mt-3">
            <Row>
                <Col span="7">
                    <FormItem>
                        <DatePicker type="date" :placeholder="datePlaceHolder" :format="dateFormat"
                                    v-model="datePart"></DatePicker>
                    </FormItem>
                </Col>
                <Col span="2" style="text-align: center">-</Col>
                <Col span="7">
                    <FormItem>
                        <TimePicker type="time" :placeholder="timePlaceHolder" :format="timeFormat"
                                    v-model="timePart"></TimePicker>
                    </FormItem>
                </Col>
            </Row>
        </Form>
    </Modal>
</template>
<script>
    import lodash from 'lodash';
    import {
        showWaiting,
        getEpochTimeFromDateStr,
        getEpochTimeFromTimeStr,
        successResult,
        errorResult,
    } from '@js/common';
    import constants from '@js/constants';

    export default {
        name: 'DateTimeModal',
        props: ['modalOpen', 'modalTitle', 'onClose', 'url', 'modalIds', 'onQuery'],
        components: {},
        data() {
            return {
                open: this.modalOpen,
                datePart: null,
                timePart: null,
                datePlaceHolder: constants.DATE_PLACEHOLDER,
                timePlaceHolder: constants.TIME_PLACEHOLDER,
                dateFormat: constants.DATE_FORMAT,
                timeFormat: constants.TIME_FORMAT,
                title: this.$t('common.edit') + this.$t('common.' + this.modalTitle) + this.$t('common.dateTime')
            };
        },
        computed: {},
        created() {
        },
        watch: {
            modalOpen: 'onOpen'
        },
        methods: {
            onEditSubmit() {
                let dataTimeToPost = 0;
                let vm = this;
                showWaiting(true);
                if (typeof vm.datePart == 'object' && vm.datePart !== undefined && vm.datePart !== null) {
                    dataTimeToPost += vm.datePart.getTime();
                } else if (vm.datePart) {
                    dataTimeToPost += getEpochTimeFromDateStr(vm.datePart);
                }
                if (vm.timePart && vm.datePart) {
                    dataTimeToPost += getEpochTimeFromTimeStr(vm.timePart);
                }
                if (dataTimeToPost === 0) {
                    dataTimeToPost = null;
                }
                return vm.$axios({
                    url: vm.url,
                    method: 'put',
                    data: {dateTime: dataTimeToPost, ids: vm.modalIds}
                }).then(function () {
                    showWaiting(false);
                    successResult(vm);
                    vm.onClose && vm.onClose();
                    vm.onQuery && vm.onQuery();
                }).catch(lodash.partial(errorResult, vm));
            },
            onOpen() {
                this.open = this.modalOpen;
                let now = new Date();
                this.datePart = new Date(now.toLocaleDateString());
                this.timePart = now.getHours() + ':' + now.getMinutes() + ':' + now.getSeconds();
            }
        }
    };
</script>