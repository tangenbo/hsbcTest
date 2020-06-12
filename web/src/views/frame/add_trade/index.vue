<template>
    <div class="page">
        <!-- 增改明细模态框 -->
        <Form ref="editForm" class="mt-3" :model="editFormData" :rules="editFormRules" label-position="left"
              :label-width="160" :md="12" :xs="24">
            <FormItem v-for="v in editFormControl" v-bind:key="v.key" v-if="v.control"
                      :label="v.label !== undefined ? v.label : v.title" :md="8" :xs="24">
                <component v-if="v.control==='Input' || v.control==='DatePicker'"
                           :is="v.control" v-bind="v.props"
                           v-model="editFormData[v.key]"
                           :placeholder="v.placeHolder"/>
            </FormItem>
        </Form>
        <Button type="primary" @click="onEditSubmit()">{{$t('common.add')}}</Button>
    </div>
</template>

<script>
    import '@css/maintainPage.less';
    import api from './api';

    export default {
        name: 'add_trade',
        components: {},//so that these tags can be used in above <template>
        data() {//all custom options to be referred in <template>, not default ones like components/name/methods/events
            return api.getCustomOptions(this);
        },
        computed: {//when something changed
        },
        created() {//when loaded
            this.onLoaded();
        },
        methods: api.getCustomMethods()
    };
</script>
