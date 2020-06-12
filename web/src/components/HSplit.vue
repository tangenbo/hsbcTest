<!-- Split 面板分割（iView的 Split 组件存在无法自动撑开高度问题，因此重新写了一个） -->
<template>
    <div class="h-split clearfix">
        <div class="panel-left" :style="{ width: left }" v-show="value > 0">
            <slot name="left"/>
        </div>
        <div class="trigger" ref="trigger" :style="{ left }" v-show="value > 0 && value < 100"></div>
        <div class="panel-right" :style="{ width: right }" v-show="value < 100">
            <slot name="right"/>
        </div>
    </div>
</template>

<script>
let fnMouse = null;

export default {
    name: 'HSplit',
    props: {
        // 分割百分比 v-model
        value: { type: Number, default: 50 }
    },
    data() {
        return {
            x: this.value
        };
    },
    computed: {
        left() {
            return this.x + '%';
        },
        right() {
            return 100 - this.x + '%';
        }
    },
    mounted() {
        const vm = this,
            split = this.$el,
            maxWidth = split.clientWidth,
            trigger = this.$refs.trigger;

        let srcX, downX;

        function move(event) {
            let moveX = (event.clientX - downX) / maxWidth;
            vm.x = moveX * 100 + srcX;
        }

        fnMouse = {
            mousedown(event) {
                srcX = vm.x;
                downX = event.clientX;
                split.addEventListener('mousemove', move);
            },
            mouseup() {
                split.removeEventListener('mousemove', move);
                vm.$emit('input', vm.x);
                srcX = undefined;
                downX = undefined;
            }
        };

        this.$nextTick(function() {
            trigger.addEventListener('mousedown', fnMouse.mousedown);
            split.addEventListener('mouseup', fnMouse.mouseup);
        });
    },
    beforeDestroy() {
        let trigger = this.$refs.trigger,
            split = this.$el;

        trigger.removeEventListener('mousedown', fnMouse.mousedown);
        split.removeEventListener('mouseup', fnMouse.mouseup);

        fnMouse = null;
    },
    watch: {
        value(x) {
            this.x = x;
        }
    }
};
</script>

<style lang="less">
@w: 5px;

.h-split {
    position: relative;

    .panel-left,
    .panel-right {
        float: left;
    }

    .trigger {
        top: 0;
        position: absolute;
        width: @w;
        height: 100%;
        z-index: 1;
        margin-left: -@w / 2;
        cursor: col-resize;
        background-color: white;
        border-radius: @w;
        opacity: 0.5;

        &:hover {
            box-shadow: 0 0 0.5rem rgba(0, 0, 0, 0.1);
            opacity: 1;
        }
    }
}
</style>