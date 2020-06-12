const path = require('path');

// 项目环境变量
const HSBC_ENV = {
    VERSION: process.env.npm_package_version
};

const alias = {
    '@js': 'script',
    '@css': 'style',
    '@c': 'components',
    '@v': 'views',
    '@a': 'assets'
};

let result = {
    publicPath: './',
    productionSourceMap: false,
    chainWebpack(config) {
        config.plugin('define').tap(function (args) {
            let defineEnv = args[0]['process.env'];
            for (const key in HSBC_ENV) {
                defineEnv['HSBC_' + key] = JSON.stringify(HSBC_ENV[key]);
            }
            return args;
        });

        for (const key in alias) {
            config.resolve.alias.set(
                key,
                path.join(__dirname, 'src', alias[key])
            );
        }

    },
    css: {
        loaderOptions: {
            less: {javascriptEnabled: true}
        }
    },
    devServer: {
        proxy: 'http://localhost:80',
        // 设置端口, 缺省是8080, 这个端口仅用在开发模式
        port: 8003
    }
};

if (process.env.npm_config_noproxy)
    result.devServer.before = require('./server/index');


module.exports = result;
