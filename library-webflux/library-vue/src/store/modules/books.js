import Api from "../../components/Api"

const state = {
    books: [],
    bookOperationsError: false,
    bookOperationsErrorText: null
}
const getters = {
    BOOKS: state => {
        return state.books;
    },
    BOOK_OP_ERROR: state => {
        return state.bookOperationsError;
    },
    BOOK_OP_ERROR_TEXT: state => {
        return state.bookOperationsErrorText;
    }
}
const mutations = {
    SET_BOOKS: (state, payload) => {
        state.books = payload;
    },
    SAVE_BOOK: (state, book) => {
        const index = state.books.findIndex(value => value.id === book.id);
        Object.assign(state.books[index], book);
    },
    SET_BOOK_OP_ERROR: (state, payload) => {
        state.bookOperationsError = payload;
    },
    SET_BOOK_OP_ERROR_TEXT: (state, payload) => {
        state.bookOperationsErrorText = payload;
    },
    CLEAR_ERROR: (state) => {
        state.bookOperationsError = false;
        state.bookOperationsError = null;
    }
}
const actions = {
    GET_BOOKS: async (context) => {
        Api.getAllBooks().then(list => context.commit('SET_BOOKS', list));
    },
    ADD_BOOK: async (context, book) => {
        Api.createBook(book)
        // eslint-disable-next-line no-unused-vars
            .then(savedBook => context.dispatch('GET_BOOKS'))
            .catch(error => {
                context.commit('SET_BOOK_OP_ERROR', true);
                context.commit('SET_BOOK_OP_ERROR_TEXT', error);
            })
    },
    SAVE_BOOK: async (context, book) => {
        Api.saveBook(book)
            .then(savedBook => context.commit('SAVE_BOOK', savedBook))
            .catch(error => {
                context.commit('SET_BOOK_OP_ERROR', true);
                context.commit('SET_BOOK_OP_ERROR_TEXT', error);
            })
    },
    DELETE_BOOK: async (context, book) => {
        Api.deleteBook(book.id)
            .then(() => context.dispatch('GET_BOOKS'))
            .catch(error => {
                context.commit('SET_BOOK_OP_ERROR', true);
                context.commit('SET_BOOK_OP_ERROR_TEXT', error);
            });
    }
}

export default {
    namespaced: true,
    state,
    getters,
    actions,
    mutations
}