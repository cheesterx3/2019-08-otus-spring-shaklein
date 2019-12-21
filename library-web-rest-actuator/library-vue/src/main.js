import Vue from 'vue'
import App from './App.vue'
import vuetify from './plugins/vuetify';
import VueResource from './plugins/vue-resource'
import store from './store'

Vue.config.productionTip = false;

new Vue({
  store,
  vuetify,
  VueResource,
  render: h => h(App)
}).$mount('#app');
