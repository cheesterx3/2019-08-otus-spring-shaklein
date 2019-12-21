<script>


    export default {
        name: "Api",
        getAllBooks: () => fetch('/api/books').then(response => response.json()),
        deleteBook: (id) => fetch('/api/books/' + id, {method: 'DELETE'})
            .then(response => handleErrors(response)),
        createBook: (book) => fetch('/api/books', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(book)
        }).then(response => handleErrors(response)).then(response => response.json()),
        saveBook: (book) => fetch('/api/books/' + book.id, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(book)
        }).then(response => handleErrors(response)).then(response => response.json()),
        getAllGenres: () => fetch('/api/genres').then(response => response.json()),
        getAllAuthors: () => fetch('/api/authors').then(response => response.json()),
    }

    function handleErrors(response) {
        if (!response.ok) {
            return response.text().then(err => {
                throw err;
            });
        }
        return response;
    }
</script>

<style scoped>

</style>