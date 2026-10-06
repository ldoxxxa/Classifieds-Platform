<script setup lang="ts">
import { RouterView, RouterLink } from "vue-router"
import { useInfo } from "@/composables/useInfo"
import { useLoginStore } from "@/stores/loginstore"

const { info, loescheInfo } = useInfo()

const loginStore = useLoginStore()
const { loginState } = loginStore
</script>

<template>
  <div class="page">
    <header>
      <h1>
        <span class="links">
          Kleinstanzeigen
          <RouterLink v-if="loginState.loggedIn" to="/anzeige" class="nav-link">| Anzeigen</RouterLink>
        </span>
        <RouterLink to="/login" class="nav-link">Login</RouterLink>
      </h1>
    </header>

    <div v-if="info !== ''" class="infobox">        
      <strong>Info</strong>
      <p>{{ info }}</p>
      <button @click="loescheInfo()">x</button>
    </div>

    <main>
      <RouterView />
    </main>

    <footer>
      <p>{{ loginState.username }}</p>
    </footer>
  </div>
</template>

<style scoped>
.page {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

main {
  flex: 1;
}

header {
  background-color: #a1cbcb;
  padding: 1rem;
}

header h1 {
  font-size: 1.8rem;
  font-weight: 600;
  color: #1d4d4d;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

header h1 .links {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

header a {
  font-size: 1rem;
  color: #1d4d4d;
  text-decoration: none;
}

.infobox {
  background-color: #cbd7d7;
  padding: 0.5rem 1rem;
  margin: 0.5rem;
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
}

.infobox p {
  font-size: 1rem;
  color: #1d4d4d;
}

footer {
  background-color: #a1cbcb;
  padding: 1rem;
  text-align: center;
}
</style>