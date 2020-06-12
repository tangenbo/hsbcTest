<!-- 框架 -->
<template>
    <Layout class="frame">
        <Sider
                class="main-sider"
                ref="mainSider"
                breakpoint="md"
                collapsible
                hide-trigger
                collapsed-width="78"
                v-model="collapsed"
        >
            <Header class="sider-title">{{header}}</Header>
            <HMenu :menu="menu" :open="menuOpen" width="auto"/>
        </Sider>
        <Layout class="main-layout">
            <Header class="header">
                <section class="clearfix">
                    <div class="float-left">
                        <Icon
                                class="menu-trigger px-0 mr-5"
                                :class="{ collapsed }"
                                size="24"
                                type="md-menu"
                                @click="menuTrigger"
                        />
                        <Breadcrumb class="breadcrumb">
                            <BreadcrumbItem>
                                <Icon type="md-home"/>
                            </BreadcrumbItem>
                            <BreadcrumbItem
                                    v-for="v in breadcrumb"
                                    :key="v.name"
                            >{{ v.title || pageTitle(v.name) }}
                            </BreadcrumbItem>
                        </Breadcrumb>
                    </div>
                    <div class="float-right">
                        <template v-if="$route.meta.projectSelect">
                            <!-- 组织 -->
                            <div class="d-inline-block mr-3">
                                {{ $t('page.frame.organization') }}：
                                <strong>{{ user.org_name }}</strong>
                            </div>
                            <!-- 项目 -->
                            <div class="project-select mr-2">
                                {{ $t('page.frame.project') }}：
                                <Select
                                        class="w-auto"
                                        v-model="projectId"
                                        size="small"
                                        @on-change="updateProject"
                                >
                                    <Option
                                            v-for="v in user.projects"
                                            :value="v.proj_id" :key="v.proj_id"
                                    >{{ v.proj_name }}
                                    </Option>
                                </Select>
                            </div>
                        </template>

                        <!-- 切换语言 -->
                        <Dropdown trigger="click" @on-click="toggleLang">
                            <Button type="text" size="small">
                                <!--{{ $t('page.frame.language') }}-->
                                <!--<Icon type="ios-arrow-down"/>-->
                                <Icon type="md-planet" size="18"/>
                            </Button>
                            <DropdownMenu slot="list">
                                <DropdownItem
                                        v-for="(v, n) in $i18n.messages"
                                        :name="n"
                                        :selected="$i18n.locale === n"
                                        :key="n"
                                >{{ v.title }}
                                </DropdownItem>
                            </DropdownMenu>
                        </Dropdown>
                    </div>
                </section>
            </Header>
            <Content class="body">
                <!-- 主页面容器 -->
                <keep-alive :include="nameTags">
                    <router-view/>
                </keep-alive>
            </Content>
            <Footer class="text-light" v-once>
                <div class="float-left"></div>
                <div class="float-right">{{ version }}</div>
            </Footer>
        </Layout>
    </Layout>
</template>

<script>
    import {mapState, mapMutations, mapGetters} from 'vuex';
    import HMenu from '@c/HMenu';
    import {menu} from './data';
    import {lStorage} from '@js/common';
    import pick from 'lodash/pick';

    export default {
        name: 'frame',
        components: {HMenu},
        data() {
            return {
                collapsed: false,
                menu,
                menuOpen: [],
                unReadList: [],
                version: process.env.HSBC_VERSION,
                projectId: this.$store.state.projectId,
                full_name: ''
            };
        },
        provide() {
            const vm = this;
            return {
                getMenu(name) {
                    return name ? vm.getMenu(name) : vm.menu;
                }
            };
        },
        created() {
        },
        computed: {
            ...mapState(['user', 'projectShow', 'locationTags']),
            ...mapGetters(['nameTags']),
            tagClosable() {
                return this.locationTags.length > 1;
            },
            header() {
                return this.$t('page.frame.header');
            },
            tagRoute() {
                const route = this.$route.matched[1];
                let result = pick(route, ['name', 'path']);
                result.meta = Object.assign({}, route.meta);
                return result;
            },
            breadcrumb() {
                let name = this.tagRoute.name,
                    result = this.getMenu(name);
                if (result) result.reverse();
                else result = [{name}];
                return result;
            },
            totalCount(){
                let count = 0;
                this.unReadList.forEach(item => {
                    // 总价 = 价格 * 数量
                    count += item.count;
                });
                return count;
            }
        },
        methods: {
            ...mapMutations(['updateProject', 'updateTags']),
            getMenu(name) {
                let result;

                function main(tree, ...path) {
                    for (let i = 0, l = tree.length; i < l; i++) {
                        const item = tree[i];
                        if (item.name === name) {
                            result = [item].concat(path);
                            break;
                        } else if (item.children) {
                            main(item.children, item, ...path);
                        }
                    }
                }

                name && main(this.menu);
                return result;
            },
            menuTrigger() {
                this.$refs.mainSider.toggleCollapse();
            },
            pageTitle(name) {
                return this.$t(`page.${name}.title`);
            },
            toggleLang(event) {
                this.$i18n.locale = event;
                lStorage('language', event);
                location.reload();
            },
            tagColor(name) {
                if (this.tagRoute.name === name) return 'error';
            },
            onTagClick(index) {
                const route = this.locationTags[index];
                this.$router.push(route.path);
            },
            onTagClose(event, index) {
                const route = this.tagRoute;
                if (typeof index === 'number') {
                    const tags = this.locationTags,
                        tag = tags[index];
                    this.updateTags(index);
                    // 关闭指定tag
                    if (route.name === tag.name)
                        this.$router.push(tags[tags.length - 1].path);
                } else {
                    // 关闭所有，保留当前
                    this.updateTags([route]);
                }
            }
        },
        watch: {
            $route() {
                const route = this.tagRoute;
                if (!route.meta.tag) return;

                // 更新标签
                let index = this.nameTags.indexOf(route.name);
                if (index < 0) this.updateTags({index, data: route});

                // 展开菜单
                let menuPath = this.getMenu(route.name);
                if (menuPath) this.menuOpen = menuPath.map(v => v.name);

                //only leave current tag on UI
                this.updateTags([route]);
            }
        }
    };
</script>

<style lang="less" scoped>
    @import '../../style/variables';

    body {
        height: 100%;
        width: 100%;
    }

    .frame {
        position: absolute;
        height: 100%;
        width: 100%;
    }

    .main-sider {
        overflow-y: auto;

        .sider-title {
            font-size: 1.5rem;
            text-align: center;
            color: white;
            background-color: @dark;
            margin-top: 1.2rem;
        }
    }

    .main-layout {
        .header {
            position: sticky;
            top: 0;
            z-index: 500;

            .main-title {
                position: absolute;
                left: 0;
                right: 0;
            }

            .breadcrumb {
                display: inline-block;
                vertical-align: middle;
                margin-bottom: 0;

                > span:last-child {
                    font-weight: normal;
                    color: @red;
                }
            }

            .menu-trigger {
                transition-duration: 0.3s;
                transition-property: transform;
                vertical-align: middle;

                &.collapsed {
                    transform: rotate(90deg);
                }
            }

            .menu-trigger {
                cursor: pointer;

                &:hover {
                    color: @red;
                }
            }

            .project-select {
                display: inline-block;
            }
        }

        .frame-tags {
            position: relative;
            padding: 2px 2rem;
            background-color: rgba(250, 250, 250, 0.8);
            line-height: initial;
            .m-x(-2rem);

            .tags {
                float: left;
            }

            .close-tags {
                float: right;
                margin-top: 2px;
            }
        }
    }

    @media (max-width: 768px) {
        .main-layout {
            .header {
                padding: 0 1rem;

                .breadcrumb {
                    display: none;
                }
            }

            .clearfix .float-left {
                padding-top: 10px;
                width: 30px;
            }

            .clearfix .float-right {
                padding: 0.75rem 0;
            }
        }
    }
</style>
