<template>
  <tr>
    <td>{{ anzeige.id }}</td>
    <td>{{ anzeige.titel }}</td>
    <td>{{ anzeige.preis }}</td>
    <td>{{ anzeige.verfuegbar }} von {{ anzeige.anzahl }}</td>
    <td>{{ anzeige.ablaufdatum }}</td>
    <td><button @click="toggle">+</button></td>
  </tr>
  <tr v-if="ausgeklappt">
    <td colspan="6">
      <p>{{ anzeige.anbieterName }}</p>
      <p>
        {{ anzeige.anbieterAdresse }}
        <a :href="kartenLink" target="_blank" class="karten-link">[Karte]</a>
      </p>
      <p>{{ anzeige.beschreibung }}</p>
      <div class="bestell-buttons">
        <button v-if="anzeige.verfuegbar > 0" @click="bestellen">bestellen</button>
        <button v-if="anzeige.verfuegbar < anzeige.anzahl" @click="stornieren">stornieren</button>
      </div>
    </td>
  </tr>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import type { IAnzeigeDTD } from '@/stores/IAnzeigeDTD'
import { useLoginStore } from '@/stores/loginstore'
import { useInfo } from '@/composables/useInfo'

const props = defineProps<{
  anzeige: IAnzeigeDTD
}>()

const ausgeklappt = ref(false)

function toggle(): void {
  ausgeklappt.value = !ausgeklappt.value
}

const kartenLink = computed(() =>
  'https://nominatim.openstreetmap.org/ui/search.html?q=' +
  encodeURI(props.anzeige.anbieterAdresse)
)

const loginStore = useLoginStore()
const { setzeInfo } = useInfo()

async function bestellen(): Promise<void> {
  if (!loginStore.loginState.loggedIn) {
    setzeInfo('Bitte zuerst einloggen, um zu bestellen.')
    return
  }
  try {
    const res = await fetch(
      `/api/anzeige/${props.anzeige.id}/bestellung/${loginStore.loginState.username}`,
      { method: 'POST' }
    )
    if (!res.ok) {
      let nachricht = `Fehler ${res.status}`
      try {
        const body = await res.json()
        nachricht = body.message || nachricht
      } catch {
        // Antwort war kein JSON, dann bleibt der Statuscode als Nachricht
      }
      setzeInfo(`Bestellung fehlgeschlagen: ${nachricht}`)
    }
    // kein manuelles Update von anzeige.verfuegbar hier!
    // die Aktualisierung kommt live über das FrontendNachrichtEvent per STOMP
  } catch (e) {
    setzeInfo('Bestellung fehlgeschlagen: Backend nicht erreichbar')
  }
}

async function stornieren(): Promise<void> {
  if (!loginStore.loginState.loggedIn) {
    setzeInfo('Bitte zuerst einloggen, um zu stornieren.')
    return
  }
  try {
    const res = await fetch(
      `/api/anzeige/${props.anzeige.id}/bestellung/${loginStore.loginState.username}`,
      { method: 'DELETE' }
    )
    if (!res.ok) {
      let nachricht = `Fehler ${res.status}`
      try {
        const body = await res.json()
        nachricht = body.message || nachricht
      } catch {
        // Antwort war kein JSON, dann bleibt der Statuscode als Nachricht
      }
      setzeInfo(`Stornierung fehlgeschlagen: ${nachricht}`)
    }
  } catch (e) {
    setzeInfo('Stornierung fehlgeschlagen: Backend nicht erreichbar')
  }
}
</script>

<style scoped>
.karten-link {
  color: #178888;
  text-decoration: underline;
}

.bestell-buttons {
  display: flex;
  gap: 0.5rem;
  margin-top: 0.5rem;
}
</style>