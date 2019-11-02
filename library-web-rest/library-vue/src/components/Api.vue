<script>
    export default {
        name: "Api",
        getAllBooks: () => fetch('/api/books').then(response => response.json()),
        deleteBook: (id) => fetch('/api/books/' + id, {method: 'DELETE'}),
        createBook: (book) => fetch('/api/books', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(book)
        }),
        saveBook: (book) => fetch('/api/books/' + book.id, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(book)
        }),
        getAllGenres: () => fetch('/api/genres').then(response => response.json()),
        getAllAuthors: () => fetch('/api/authors').then(response => response.json())
    }
</script>

<style scoped>

</style>