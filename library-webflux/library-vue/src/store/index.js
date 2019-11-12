import Vue from 'vue'
import Vuex from 'vuex'
import books from "./modules/books";
import registry from "./modules/registry";

Vue.use(Vuex);

export default new Vuex.Store({
    modules: {
        books,
        registry
    }
})
