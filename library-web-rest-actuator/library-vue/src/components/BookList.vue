<template>
    <v-data-table :headers="headers" :items="bookList" class="elevation-1">
        <template v-slot:top>
            <v-toolbar flat color="white">
                <v-toolbar-title>Список книг</v-toolbar-title>
                <v-divider class="mx-4" inset vertical></v-divider>
                <v-spacer></v-spacer>
                <v-dialog v-model="dialog" max-width="500px">
                    <template v-slot:activator="{ on }">
                        <v-btn color="primary" dark class="mb-2" v-on="on">Добавить книгу</v-btn>
                    </template>
                    <BookAddForm v-bind:book="currentBook" v-bind:close="close" v-bind:save="save"
                                 v-bind:showing="dialog"/>
                </v-dialog>
                <v-snackbar v-model="error" color="error">
                    {{errorText}}
                    <v-btn dark text @click="clearError()">
                        Закрыть
                    </v-btn>
                </v-snackbar>
            </v-toolbar>
        </template>
        <template v-slot:item.action="{ item }">
            <v-icon small class="mr-2" @click="editItem(item)">
                Редактировать
            </v-icon>
            <v-icon small @click="deleteItem(item)">
                Удалить
            </v-icon>
        </template>
    </v-data-table>
</template>

<script>
    import BookAddForm from "./BookForm";
    import {mapState} from 'vuex';

    export default {
        name: "BookList",
        components: {BookAddForm},
        data: () => ({
            dialog: false,
            headers: [
                {
                    text: 'Наименование',
                    align: 'left',
                    sortable: false,
                    value: 'name',
                },
                {text: 'Жанры', value: 'genresInfo'},
                {text: 'Авторы', value: 'authorsInfo'},
                {text: 'Действия', value: 'action', sortable: false},
            ],
            currentBook: {},
        }),
        watch: {
            dialog(val) {
                val || this.close()
            },
        },
        mounted() {
            this.$store.dispatch('books/GET_BOOKS');
        },
        computed: {
            ...mapState({
                bookList: state => state.books.books,
                error: state => state.books.bookOperationsError,
                errorText: state => state.books.bookOperationsErrorText
            })
        },
        methods: {
            clearError() {
                this.$store.commit('books/CLEAR_ERROR');
            },
            editItem(book) {
                this.currentBook = Object.assign({}, book);
                this.dialog = true
            },
            deleteItem(book) {
                if (confirm('Вы уверены, что желаете удалить книгу?')) {
                    this.$store.dispatch('books/DELETE_BOOK', book);
                }
            },
            close() {
                this.dialog = false;
                setTimeout(() => {
                    this.currentBook = Object.assign({}, {});
                }, 300)
            },
            save(book) {
                if (book.id !== undefined && book.id !== '') {
                    this.$store.dispatch('books/SAVE_BOOK', book);
                } else {
                    this.$store.dispatch('books/ADD_BOOK', book);
                }
                this.close()
            }
        }
    }

</script>

<style scoped>

</style>