<template>
  <div class="login">
    <h1>Anmelden</h1>

    <form class="card" @submit.prevent="onSubmit">
      <label>
        E-Mail
        <input v-model.trim="email" type="email" autocomplete="username" required />
      </label>

      <label>
        Passwort
        <input v-model="password" type="password" autocomplete="current-password" required />
      </label>

      <button type="submit" :disabled="loading">
        {{ loading ? '...' : 'Login' }}
      </button>

      <p v-if="error" class="error">{{ error }}</p>
    </form>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const email = ref('')
const password = ref('')
const loading = ref(false)
const error = ref<string | null>(null)

async function onSubmit() {
  error.value = null
  loading.value = true

  try {
    const res = await fetch('http://localhost:8080/api/auth/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ email: email.value, password: password.value }),
    })

    if (!res.ok) {
      const msg = await res.text().catch(() => '')
      throw new Error(msg || 'Login fehlgeschlagen')
    }

    // Minimal: Backend gibt z.B. { token: "..." } zurück
    const data = await res.json().catch(() => ({} as any))
    if (data?.token) localStorage.setItem('token', data.token)

    await router.push({ name: 'Startseite' })
  } catch (e: any) {
    error.value = e?.message ?? 'Unbekannter Fehler'
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login { max-width: 420px; margin: 40px auto; padding: 0 16px; }
.card { display: grid; gap: 12px; padding: 16px; border: 1px solid #ddd; border-radius: 10px; }
label { display: grid; gap: 6px; font-size: 14px; }
input { padding: 10px; border: 1px solid #ccc; border-radius: 8px; }
button { padding: 10px; border-radius: 8px; border: 1px solid #222; cursor: pointer; }
.error { color: #b00020; margin: 0; }
</style>

