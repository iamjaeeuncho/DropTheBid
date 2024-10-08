const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: [
    'vuetify'
  ],

  outputDir: '../backend/src/main/resources/static',
  devServer: {
    proxy: 'http://localhost:8080'
  }
})
