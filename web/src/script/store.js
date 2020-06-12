import Vue from 'vue';
import Vuex from 'vuex';
import { lStorage } from './common';
import set from 'lodash/set';

Vue.use(Vuex);

export default new Vuex.Store({
	state: {
		user: {},
		projectId: lStorage('projectId'),
		locationTags: []
	},
	getters: {
		nameTags(state) {
			return state.locationTags.map(v => v.name);
		},
		session() {
			return localStorage.getItem('session');
		}
	},
	mutations: {
		setState(state, [name, value]) {
			set(state, name, value);
		},
		updateSession(state, payload) {
			lStorage('session', payload);
		},
		updateProject(state, payload) {
			state.projectId = payload;
			lStorage('projectId', payload);
		},
		updateTags(state, payload) {
			if (Array.isArray(payload)) {
				// 覆盖
				state.locationTags = payload;
			} else {
				const tags = state.locationTags;
				if (typeof payload === 'number') {
					// 删除
					tags.splice(payload, 1);
				} else {
					const { index, data } = payload;
					if (index < 0) {
						// 增加
						tags.push(data);
						if (tags.length > 10)
							tags.shift();
					} else {
						// 修改
						Object.assign(tags[index], data);
					}
				}
			}
		} // updateTags
	}
});
