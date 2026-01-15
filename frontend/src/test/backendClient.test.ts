import { describe, it, expect, vi, beforeEach } from 'vitest'
import { buildMealsSearchUrl, searchMeals } from '../lib/backendClient'

// Für Mocken zuständig
describe('backendClient', () => {
  beforeEach(() => {
    vi.stubGlobal('fetch', vi.fn())
  })

  // 2) Testet das Bauen der URL
  it('baut die richtige URL (/api/meals?q=... + encodeURIComponent)', () => {
    expect(buildMealsSearchUrl('chicken soup', '/api')).toBe('/api/meals?q=chicken%20soup')
    expect(buildMealsSearchUrl('curry & rice', '/api')).toBe('/api/meals?q=curry%20%26%20rice')
  })

  // 3) Testet, ob der Code bei inkonsistenten Daten crasht
  it('wirft Fehler bei !res.ok', async () => {
    ;(fetch as any).mockResolvedValue({ ok: false })
    await expect(searchMeals('chicken')).rejects.toThrow('Fehler bei der Suche')
  })

  // 4) Testet, ob Daten im richtigen Format geliefert werden
  it('parst JSON korrekt', async () => {
    const payload = { meals: [{ idMeal: '1', strMeal: 'Chicken' }] }

    ;(fetch as any).mockResolvedValue({
      ok: true,
      json: async () => payload,
    })

    const data = await searchMeals('chicken')
    expect(data).toEqual(payload)
    expect(fetch).toHaveBeenCalledTimes(1)
    expect((fetch as any).mock.calls[0][0]).toMatch(/\/api\/meals\?q=chicken$/)
  })
})
