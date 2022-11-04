const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true
})
//代理(解决跨域问题)
let proxyObj = {}
proxyObj['/']={
  //websocker
  ws:false,
  target:'http://localhost:8081',
  //发送请求头host会被设置成target
  changeOrigin: true,
  //不重写请求地址
  pathRewrite:{
    '^/': '/'
  }
}

module.exports={
  devServer:{
      host: 'http://x86_64-apple-darwin13.4.0',
      port: 8080,
      proxy: proxyObj
  }
}