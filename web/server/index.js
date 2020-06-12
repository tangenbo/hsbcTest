const path = require('path');
const Mock = require('mockjs');

function getFilePath(name) {
    return path.join(__dirname, 'static', name);
}

/**
 * @param {Object} app Express 实例
 * http://expressjs.com/zh-cn/guide/routing.html
 */
module.exports = function (app) {
    /**
     * 静态json数据匹配，数据来源：/server/static
     */
    app.all('/*', function (req, res) {
        let path = getFilePath(req.params[0].replace(/\//g, '-') + '.json');
        res.sendFile(path, function (error) {
            console.info(req.path, '-->', path);
            if (error) {
                res.json([req.query, req.body]);
                console.warn('找不到文件，响应[query, body]', '\n');
            } else {
                console.info('本地数据', '\n');
            }
        });
    });

    /**
     * 文件匹配，数据来源：/server/static
     * @param {String} name 文件名，如：captcha.jpg
     */
    app.get('/file/:name', function (req, res) {
        const params = req.params;
        let path = getFilePath(params.name);
        res.sendFile(path);
    });

    /**
     * 专门模拟错误
     */
    app.all('/api/error/:status', function (req, res) {
        const status = req.params.status;
        res.status(status).json({ message: status });
    });

    /**
     * Mock 数据匹配模板
     * https://github.com/nuysoft/Mock/wiki/Syntax-Specification
     */
    app.all('/api/mock', function (req, res) {
        let data = Mock.mock({
            'array|1-20': [{ key1: '@word', key2: '@word' }]
        });
        res.json(data);
    });
};
