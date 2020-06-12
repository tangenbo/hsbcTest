import Vue from 'vue';
import iView from 'iview';
import BootstrapVue from 'bootstrap-vue';
import FullCalendar from 'vue-full-calendar';
import i18n from './locale';
import router from '@js/router';
import store from '@js/store';
import directive from '@js/directive';
import filter from '@js/filter';
import ajaxPlugin from '@js/ajaxPlugin';
import '@css/iview.less';
import '@css/common.less';
import '@css/bootstrap.css';

//require('./mock.js');

Vue.config.productionTip = false;

Vue.config.warnHandler = function (msg, vm, trace) {
	console.error(`[warn]: ${msg}${trace}\n\nView model:`, vm);
};

Vue.use(iView, { i18n: (...p) => i18n.t(...p) })
	.use(directive)
	.use(filter)
	.use(BootstrapVue)
    .use(FullCalendar)
	.use(ajaxPlugin, iView);

new Vue({
	el: '#app',
	router,
	store,
	i18n,
	render: h => h('router-view')
});
