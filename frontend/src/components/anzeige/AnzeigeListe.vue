<template>
  <table>
    <thead>
      <tr>
        <th>ID</th>
        <th>Titel</th>
        <th>Preis</th>
        <th>verfügbar</th>
        <th>Ablaufdatum</th>
        <th></th>
      </tr>
    </thead>
    <tbody>
      <AnzeigeListeZeile
        v-for="a in anzeigen"
        :key="a.id"
        :anzeige="a"
      />
    </tbody>
  </table>
</template>

<script setup lang="ts">
import type { IAnzeigeDTD } from '@/stores/IAnzeigeDTD'
import AnzeigeListeZeile from './AnzeigeListeZeile.vue'

defineProps<{
  anzeigen: IAnzeigeDTD[]
}>()
</script>

<style scoped>
table {
  border-collapse: collapse;
  width: 100%;
}

th {
  text-align: left;
  font-weight: bold;
  padding: 0.5rem 1.5rem;
}
</style>