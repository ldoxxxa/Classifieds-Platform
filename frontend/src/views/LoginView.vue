<template>
  <div class="login-bereich">
    <h2>Login</h2>
    <input type="text" v-model="username" placeholder="Username" />
    <input type="password" v-model="passwort" placeholder="Passwort" />
    <button @click="versuchLogin">Login</button>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useInfo } from '@/composables/useInfo'
import { useLoginStore } from '@/stores/loginstore'

const username = ref('')
const passwort = ref('')

const { setzeInfo, loescheInfo } = useInfo()
const loginStore = useLoginStore()
const { loginState } = loginStore
const router = useRouter()

onMounted(() => {
  loginStore.logout()
})

function versuchLogin(): void {
  loginStore.login(username.value, passwort.value)

  if (!loginState.loggedIn) {
    passwort.value = ''
    setzeInfo('Jammer - der Login-Versuch war zwar nicht erfolgreich, aber erfolglos')
  } else {
    loescheInfo()
    router.push('/anzeige')
  }
}
</script>

<style scoped>
.login-bereich {
  max-width: 300px;
  margin: 2rem auto;
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.login-bereich input {
  padding: 0.5rem;
}

.login-bereich button {
  padding: 0.5rem;
  cursor: pointer;
}
</style>