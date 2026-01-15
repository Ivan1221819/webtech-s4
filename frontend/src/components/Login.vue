<template>
  <div class="login">
    <h1>{{ mode === 'login' ? 'Anmelden' : 'Registrieren' }}</h1>

    <div class="tabs">
      <button
        type="button"
        class="tab"
        :class="{ active: mode === 'login' }"
        @click="setMode('login')"
        :disabled="loading"
      >
        Login
      </button>
      <button
        type="button"
        class="tab"
        :class="{ active: mode === 'register' }"
        @click="setMode('register')"
        :disabled="loading"
      >
        Registrieren
      </button>
    </div>

    <form class="card" @submit.prevent="onSubmit">
      <label>
        E-Mail
        <input v-model.trim="email" type="email" autocomplete="username" required />
      </label>

      <label>
        Passwort
        <input
          v-model="password"
          type="password"
          :autocomplete="mode === 'login' ? 'current-password' : 'new-password'"
          required
        />
      </label>

      <label v-if="mode === 'register'">
        Passwort wiederholen
        <input v-model="password2" type="password" autocomplete="new-password" required />
      </label>

      <button type="submit" :disabled="loading">
        {{ loading ? '...' : mode === 'login' ? 'Login' : 'Account erstellen' }}
      </button>

      <p v-if="error" class="error">{{ error }}</p>
      <p v-if="info" class="info">{{ info }}</p>
    </form>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'

type Mode = 'login' | 'register'

const router = useRouter()

const mode = ref<Mode>('login')
const email = ref('')
const password = ref('')
const password2 = ref('')

const loading = ref(false)
const error = ref<string | null>(null)
const info = ref<string | null>(null)

function setMode(m: Mode) {
  mode.value = m
  error.value = null
  info.value = null
  password.value = ''
  password2.value = ''
}

async function apiLogin() {
  const res = await fetch('http://localhost:8080/api/auth/login', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ email: email.value, password: password.value })
  })

  if (!res.ok) {
    const msg = await res.text().catch(() => '')
    throw new Error(msg || 'Login fehlgeschlagen')
  }

  const data = await res.json().catch(() => ({} as any))
  if (data?.token) sessionStorage.setItem('token', data.token)

  await router.push({ name: 'Startseite' })
}

async function apiRegister() {
  const res = await fetch('http://localhost:8080/api/auth/register', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ email: email.value, password: password.value })
  })

  // Optional: Backend kann 409 liefern wenn User existiert
  if (!res.ok) {
    const msg = await res.text().catch(() => '')
    throw new Error(msg || 'Registrierung fehlgeschlagen')
  }

  const data = await res.json().catch(() => ({} as any))
  if (data?.token) {
    sessionStorage.setItem('token', data.token)
    await router.push({ name: 'Startseite' })
    return
  }

  // Falls Register kein Token liefert: direkt danach einloggen
  info.value = 'Account erstellt. Login...'
  await apiLogin()
}

async function onSubmit() {
  error.value = null
  info.value = null
  loading.value = true

  try {
    if (mode.value === 'register') {
      if (password.value.length < 6) {
        throw new Error('Passwort muss mindestens 6 Zeichen haben')
      }
      if (password.value !== password2.value) {
        throw new Error('Passwörter stimmen nicht überein')
      }
      await apiRegister()
    } else {
      await apiLogin()
    }
  } catch (e: any) {
    error.value = e?.message ?? 'Unbekannter Fehler'
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login {
  max-width: 420px;
  margin: 40px auto;
  padding: 0 16px;
}

.card {
  display: grid;
  gap: 12px;
  padding: 16px;
  border: 1px solid #ddd;
  border-radius: 10px;
}

.tabs {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 8px;
  margin: 12px 0;
}

.tab {
  padding: 10px;
  border-radius: 8px;
  border: 1px solid #bbb;
  background: #fff;
  cursor: pointer;
}

.tab.active {
  border-color: #222;
  font-weight: 600;
}

label {
  display: grid;
  gap: 6px;
  font-size: 14px;
}

input {
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 8px;
}

button {
  padding: 10px;
  border-radius: 8px;
  border: 1px solid #222;
  cursor: pointer;
}

.error {
  color: #b00020;
  margin: 0;
}

.info {
  color: #1a5;
  margin: 0;
}
</style>
