<template>
    <div class="page">
        <Form inline>
            <FormItem>
                <Button type="primary" @click="onQuery()">{{$t('common.refresh')}}</Button>
            </FormItem>
        </Form>
        <TableCurd
                :table="table1"
                v-bind="tableCurd"
                :pageChanged="pageChanged"
                :pageNumber="pageNumber"
                :total="totalRows"
                :tableLoading="tableLoading"
                @handle="onTableHandle"
        />
    </div>
</template>

<script>
    import '@css/maintainPage.less';
    import TableCurd from '@c/TableCurd';
    import api from './api';

    export default {
        name: 'trade_list',
        components: {TableCurd},//so that these tags can be used in above <template>
        data() {//all custom options to be referred in <template>, not default ones like components/name/methods/events
            return api.getCustomOptions(this);
        },
        computed: {//when something changed
            table1() {
                return this.getTable();
            }
        },
        created() {//when loaded
            this.onQuery();
        },
        methods: api.getCustomMethods()
    };
</script>
