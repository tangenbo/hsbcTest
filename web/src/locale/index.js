import Vue from 'vue';
import VueI18n from 'vue-i18n';
import { lStorage } from '@js/common';
import zh from './zh';
import en from './en';

Vue.use(VueI18n);

let locale = lStorage('language') || 'en-US';

export default new VueI18n({
    locale,
    fallbackLocale: 'zh-CN',
    messages: {
        'zh-CN': zh,
        'en-US': en
    }
});