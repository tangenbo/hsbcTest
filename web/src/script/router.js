import Vue from 'vue';
import Router from 'vue-router';
import { LoadingBar } from 'iview';
import i18n from '@/locale';

Vue.use(Router);

let frameRoutes = [
	{
		path: 'trade_list',
		component: () => import('@v/frame/trade_list'),
        meta: { public: true }
	},
	{
		path: 'add_trade',
		component: () => import('@v/frame/add_trade'),
        meta: { public: true }
	}
];

frameRoutes.forEach(function (v) {
	if (v.path) {
		v.name = v.path;
		if (v.meta)
			v.meta.tag = true;
		else
			v.meta = { tag: true };
	}

});

let routes = [
	{
		path: '',
		redirect: '/frame/trade_list'
	},
	{
		path: '/frame',
		component: () => import('@v/frame'),
		children: frameRoutes
	},
	{
		path: '*',
		redirect: '/error'
	}
];

let router = new Router({
	routes,
	scrollBehavior(to, from, savedPosition) {
		if (savedPosition)
			return savedPosition;
		else if (to.hash)
			return { selector: to.hash };
		else
			return { x: 0, y: 0 };
	},
	mode: 'hash'
});

// 路由全局守卫
const globalTitle = document.title;

router.beforeEach(function (to, from, next) {
	LoadingBar.start();
	next();
});

router.afterEach(function (to) {
	let pageTitle = i18n.t(`page.${to.name}.title`);
	document.title = `${globalTitle} - ${pageTitle}`;
	LoadingBar.finish();
});

router.onError(function () {
	LoadingBar.error();
});

export default router;
