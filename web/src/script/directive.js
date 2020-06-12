/**
 * 全局指令
 */
export default {
    install(Vue) {
        // 元素滚动可见
        Vue.directive('intoView', function (el, binding) {
            if (binding.value)
                el.scrollIntoView({ behavior: 'smooth' });
        });

        // visibility 版的显示隐藏
        Vue.directive('visibility', function (el, binding) {
            if (binding.value)
                el.style.visibility = 'visible';
            else
                el.style.visibility = 'hidden';
        });
    }
};