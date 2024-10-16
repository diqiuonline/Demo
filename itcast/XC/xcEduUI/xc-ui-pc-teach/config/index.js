'use strict'
// Template version: 1.2.4
// see http://vuejs-templates.github.io/webpack for documentation.
const path = require('path')
var proxyConfig = require('./proxyConfig')
let sysConfig = require('./sysConfig')
let xcApiUrl = sysConfig.xcApiUrl
module.exports = {
  dev: {

    // Paths
    assetsSubDirectory: 'static',
    assetsPublicPath: '/',
    //proxyTable: proxyConfig.proxyList,
    proxyTable: {
      '/banner': {
        // target: 'http://localhost:3000/mock/11'
        target: 'http://127.0.0.1:7777'
      },
     /* '/api': {
        // target: 'http://localhost:3000/mock/11'
        target: xcApiUrl
        //target: 'http://127.0.0.1:50201'

      }*/
       '/api/ucenter': {
       // target: 'http://localhost:3000/mock/11'
       target: 'http://127.0.0.1:31200',
         pathRewrite: {
           '^/api': ''
         }
       // target: 'http://127.0.0.1:50201'

       },
       '/api/auth': {
       // target: 'http://localhost:3000/mock/11'
       target: 'http://127.0.0.1:31200/auth',
         pathRewrite: {
           '^/api': ''
         }
       // target: 'http://127.0.0.1:50201/api'

       },
       '/api/course': {
       // target: 'http://localhost:3000/mock/11'
         target: 'http://127.0.0.1:31200',
         pathRewrite: {
           '^/api': ''
         }
       },
       '/api/media': {
         target: 'http://127.0.0.1:31400',
         pathRewrite: {
           '^/api': ''
         }
       },

       '/api/cms': {//cms管理
       //target: 'http://127.0.0.1:31001'
       target: 'http://127.0.0.1:50201',
         pathRewrite: {
           '^/api': ''
         }

       },

       '/api/filesystem': {//文件系统管理
       target: 'http://127.0.0.1:22100',
         pathRewrite: {
           '^/api': ''
         }
       // target: 'http://127.0.0.1:50201'

       },
       '/api/category': {//分类管理
       // target: 'http://127.0.0.1:3000/mock/11'
       target: 'http://127.0.0.1:31200',
         pathRewrite: {
           '^/api': ''
         }
       // target: 'http://127.0.0.1:50201'

       },
       '/api/sys': {//系统管理
       target: 'http://127.0.0.1:31001',
         pathRewrite: {
           '^/api': ''
         }
       // target: 'http://127.0.0.1:50201'

       }
      /*'/static/!*': {//系统管理
       //target: 'http://127.0.0.1:31001'
       target: 'http://127.0.0.1'

       },
       '/group1/*': {//系统管理
       // target: 'http://127.0.0.1:3000/mock/11'
       target: 'http://192.168.101.64'

       }*/
    },
    // Various Dev Server settings
    host: 'localhost', // can be overwritten by process.env.HOST
    port: 12000, // can be overwritten by process.env.PORT, if port is in use, a free one will be determined
    autoOpenBrowser: false,
    errorOverlay: true,
    notifyOnErrors: true,
    poll: false, // https://webpack.js.org/configuration/dev-server/#devserver-watchoptions-

    // Use Eslint Loader?
    // If true, your code will be linted during bundling and
    // linting errors and warnings will be shown in the console.
    useEslint: true,
    // If true, eslint errors and warnings will also be shown in the error overlay
    // in the browser.
    showEslintErrorsInOverlay: false,

    /**
     * Source Maps
     */

    // https://webpack.js.org/configuration/devtool/#development
    devtool: 'eval-source-map',

    // If you have problems debugging vue-files in devtools,
    // set this to false - it *may* help
    // https://vue-loader.vuejs.org/en/options.html#cachebusting
    cacheBusting: true,

    // CSS Sourcemaps off by default because relative paths are "buggy"
    // with this option, according to the CSS-Loader README
    // (https://github.com/webpack/css-loader#sourcemaps)
    // In our experience, they generally work as expected,
    // just be aware of this issue when enabling this option.
    cssSourceMap: false,
  },
  build: {
    // Template for index.html
    index: path.resolve(__dirname, '../dist/index.html'),

    // Paths
    assetsRoot: path.resolve(__dirname, '../dist'),
    assetsSubDirectory: 'static',
    assetsPublicPath: '/',

    /**
     * Source Maps
     */

    productionSourceMap: true,
    // https://webpack.js.org/configuration/devtool/#production
    devtool: '#source-map',

    // Gzip off by default as many popular static hosts such as
    // Surge or Netlify already gzip all static assets for you.
    // Before setting to `true`, make sure to:
    // npm install --save-dev compression-webpack-plugin
    productionGzip: false,
    productionGzipExtensions: ['js', 'css'],

    // Run the build command with an extra argument to
    // View the bundle analyzer report after build finishes:
    // `npm run build --report`
    // Set to `true` or `false` to always turn it on or off
    bundleAnalyzerReport: process.env.npm_config_report
  }
}
