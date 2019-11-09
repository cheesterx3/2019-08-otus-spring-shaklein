<template>
    <v-data-table :headers="headers" :items="books" class="elevation-1">
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
                    <v-btn dark text @click="error = false">
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
    import Api from "./Api";

    export default {
        name: "BookList",
        components: {BookAddForm},
        data: () => ({
            dialog: false,
            error: false,
            errorText: '',
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
            books: [],
            editedIndex: -1,
            currentBook: {},
        }),
        watch: {
            dialog(val) {
                val || this.close()
            },
        },
        created() {
            this.initialize();
        },
        methods: {
            initialize() {
                Api.getAllBooks().then(list => this.books = list);
            },
            editItem(book) {
                this.editedIndex = this.books.indexOf(book);
                this.currentBook = Object.assign({}, book);
                this.dialog = true
            },
            deleteItem(book) {
                const index = this.books.indexOf(book);
                if (confirm('Вы уверены, что желаете удалить книгу?')) {
                    Api.deleteBook(book.id)
                        .then(this.handleErrors)
                        .then(() => this.books.splice(index, 1))
                        .catch(error => {
                            this.error = true;
                            this.errorText = error;
                        });
                }
            },
            close() {
                this.dialog = false;
                setTimeout(() => {
                    this.currentBook = Object.assign({}, {});
                    this.editedIndex = -1
                }, 300)
            },
            save(book) {
                if (book.id !== undefined && book.id !== '') {
                    const index = this.books.findIndex(value => value.id === book.id);
                    Api.saveBook(book)
                        .then(this.handleErrors)
                        .then(response => response.json())
                        .then(result => Object.assign(this.books[index], result))
                        .catch(error => {
                            this.error = true;
                            this.errorText = error;
                        })
                } else {
                    Api.createBook(book)
                        .then(this.handleErrors)
                        .then(response => response.json())
                        .then(savedBook => this.books.push(savedBook))
                        .catch(error => {
                            this.error = true;
                            this.errorText = error;
                        })
                }
                this.close()
            },
            handleErrors(response) {
                if (!response.ok) {
                    return response.text().then(err => {
                        throw err;
                    });
                }
                return response;
            }
        },
    }

</script>

<style scoped>

</style>