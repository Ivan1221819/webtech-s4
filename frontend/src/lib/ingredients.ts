export function buildIngredients(meal: any): string[] {
  const list: string[] = []
  for (let i = 1; i <= 20; i++) {
    const ing = meal?.[`strIngredient${i}`]
    const meas = meal?.[`strMeasure${i}`]

    if (typeof ing === 'string' && ing.trim()) {
      const m = typeof meas === 'string' && meas.trim() ? ` (${meas.trim()})` : ''
      list.push(`${ing.trim()}${m}`)
    }
  }
  return list
}
