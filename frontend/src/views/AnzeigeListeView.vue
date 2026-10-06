<template>
  <div>
    <div>
      <h2 class="title">Unsere aktuellen Anzeigen</h2>
    </div>
    <div class="suchzeile">
      <input
        type="text"
        v-model="suchstring"
        placeholder="Suche nach Titel oder Beschreibung..."
      />
      <button @click="suchstring = ''">Reset</button>
    </div>
    <div>
      <AnzeigeListe :anzeigen="gefilterteListe"></AnzeigeListe>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import AnzeigeListe from '@/components/anzeige/AnzeigeListe.vue'
import { useAnzeigeStore } from '@/stores/anzeigestore'

const anzeigeStore = useAnzeigeStore()
const { anzeigedata } = anzeigeStore

const suchstring = ref('')

const gefilterteListe = computed(() => {
  if (suchstring.value === '') {
    return anzeigedata.anzeigeliste
  }
  const suche = suchstring.value.toLowerCase()
  return anzeigedata.anzeigeliste.filter(
    (a) =>
      a.titel.toLowerCase().includes(suche) ||
      a.beschreibung.toLowerCase().includes(suche)
  )
})

onMounted(() => {
  anzeigeStore.updateAnzeigeListe()
})
</script>

<style scoped>
.title {
  font-size: 1.5rem;
  font-weight: 600;
  margin: 1rem 0 1.5rem 0;
  color: #1a1a2e;
}

.suchzeile {
  display: flex;
  gap: 0.5rem;
  margin-bottom: 1rem;
}

table {
  border-collapse: collapse;
  width: 100%;
}

th {
  font-weight: bold;
  text-align: left;
  padding: 0.5rem 1.5rem;
}

td {
  padding: 0.5rem 1.5rem;
}
</style>