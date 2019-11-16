<template>
  <v-app>
    <v-app-bar app>
      <v-toolbar-title class="headline text-uppercase">
        <span>БИБЛИОТЕКА</span>
      </v-toolbar-title>
      <v-spacer></v-spacer>
    </v-app-bar>

    <v-content>
      <BookList/>
    </v-content>
  </v-app>
</template>

<script>
import BookList from "./components/BookList";

export default {
  name: 'App',
  components: {
    BookList
  },
  data: () => ({
    //
  }),
};
</script>
