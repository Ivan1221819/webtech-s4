import { describe, it, expect } from 'vitest'
import { buildIngredients } from '../lib/ingredients'

// 5) Testet, ob Zutaten richtig dargestellt werden
describe('buildIngredients', () => {
  it('baut Zutatenliste korrekt aus strIngredient1..20, ignoriert leere', () => {
    const meal = {
      strIngredient1: 'Chicken',
      strMeasure1: '200g',
      strIngredient2: '   ',
      strMeasure2: '1 tsp',
      strIngredient3: 'Salt',
      strMeasure3: '',
      strIngredient4: null,
    }

    expect(buildIngredients(meal)).toEqual([
      'Chicken (200g)',
      'Salt',
    ])
  })
})
