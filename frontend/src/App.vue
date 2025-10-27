<script setup lang="ts"></script>

<template>
  <Startseite />
</template>

<script>
import Startseite from './components/Startseite.vue'
import Merkliste from './components/Merkliste.ts';

export default {
  name: 'App',
  components: { Startseite, Merkliste }
}
</script>

<style scoped></style>
