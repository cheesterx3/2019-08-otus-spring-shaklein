import Api from "../../components/Api"

const state = {
    genres: [],
    authors: []
}

const getters = {
    GENRES: state => {
        return state.genres;
    },
    AUTHORS: state => {
        return state.authors;
    }
}

const mutations = {
    SET_AUTHORS: (state, payload) => {
        state.authors = payload;
    },
    SET_GENRES: (state, payload) => {
        state.genres = payload;
    }
}

const actions = {
    getAllAuthors: async (context) => {
        Api.getAllAuthors().then(list => context.commit('SET_AUTHORS', list));
    },
    getAllGenres: async (context) => {
        Api.getAllGenres().then(list => context.commit('SET_GENRES', list));
    }
}

export default {
    namespaced: true,
    state,
    getters,
    actions,
    mutations
}