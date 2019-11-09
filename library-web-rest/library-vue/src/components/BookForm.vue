<template>
    <v-card>
        <v-card-title>
            <span class="headline">Новая книга</span>
        </v-card-title>
        <v-card-text>
            <v-form v-model="valid">
                <v-container>
                    <v-row>
                        <v-text-field v-model="book.name" label="Название книги"
                                      required
                                      :rules="[v=>!!v||'Название не может быть пустым']"></v-text-field>
                    </v-row>
                    <v-row>
                        <v-autocomplete :items="genres"
                                        item-text="name"
                                        chips multiple
                                        item-value="id"
                                        v-model="book.genreId"
                                        label="Жанр"
                                        required
                                        deletable-chips
                                        :rules="genresRules"></v-autocomplete>
                    </v-row>
                    <v-row>
                        <v-autocomplete :items="authors"
                                        item-text="name"
                                        chips multiple
                                        item-value="id"
                                        v-model="book.authorId"
                                        label="Автор"
                                        required
                                        deletable-chips
                                        :rules="authorsRules"></v-autocomplete>
                    </v-row>
                </v-container>
            </v-form>
        </v-card-text>
        <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="blue darken-1" text @click="close">Отмена</v-btn>
            <v-btn :disabled="!valid" color="blue darken-1" text @click="save(book)">Сохранить</v-btn>
        </v-card-actions>
    </v-card>
</template>

<script>
    import Api from "./Api";

    export default {
        name: "BookForm",
        data: () => ({
            valid: true,
            genres: [],
            authors: [],
            genresRules: [
                v => (v && v.length > 0) || 'Жанр должен быть выбран'
            ],
            authorsRules: [
                v => (v && v.length > 0) || 'Автор должен быть выбран'
            ]
        }),
        props: {
            book: {},
            close: Function,
            save: Function,
            showing: {
                type: Boolean,
                default: false
            }
        },
        mounted() {
            this.loadAuthors();
            this.loadGenres();
        },
        methods: {
            loadGenres() {
                Api.getAllGenres()
                    .then(list => this.genres = list);
            },
            loadAuthors() {
                Api.getAllAuthors()
                    .then(list => this.authors = list);
            }
        }

    }
</script>

<style scoped>

</style>