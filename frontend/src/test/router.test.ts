import { describe, it, expect } from 'vitest'
import { createRouter, createMemoryHistory } from 'vue-router'
import { routes } from '@/router'

// 6) Testet, ob beim Aufruf von "/" der Nutzer auf "/login" weitergeleitet wird
describe('Router', () => {
  it('Default-Route / redirectet auf /login', async () => {
    const router = createRouter({
      history: createMemoryHistory(),
      routes,
    })

    await router.push('/')
    await router.isReady()

    expect(router.currentRoute.value.fullPath).toBe('/login')
  })
})
