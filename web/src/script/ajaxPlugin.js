/**
 * vue 插件版 axios，只在 main.js 引用
 */
import axios from 'axios';

function errorHandle(error, options) {
    options.LoadingBar.error();
    return Promise.reject(error);
}

export default {
    install(Vue, options) {
        // Add a request interceptor
        axios.interceptors.request.use(function (config) {
            options.LoadingBar.start();
            return config;
        }, function (error) {
            return errorHandle(error, options);
        });

        // Add a response interceptor
        axios.interceptors.response.use(function (response) {
            options.LoadingBar.finish();
            return response;
        }, function (error) {
            return errorHandle(error, options);
        });

        //to support body params in ajax requests
        axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded';
        axios.defaults.headers.put['Content-Type'] = 'application/x-www-form-urlencoded';
        axios.defaults.headers.delete['Content-Type'] = 'application/x-www-form-urlencoded';

        Vue.prototype.$axios = axios;
    }
};