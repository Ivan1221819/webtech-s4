import { ref } from 'vue'
import { getMerkliste, addToMerkliste, removeFromMerkliste } from '@/lib/backendClient'

export interface Meal {
  idMeal: string;
  strMeal: string;
  strMealThumb: string;
  strInstructions?: string;
}


const merkliste = ref<Meal[]>([])
const loading = ref(false)
const error = ref<string | null>(null)
let initialized = false

function mapFromBackend(item: any): Meal {
  return {
    idMeal: item.mealId,
    strMeal: item.title,
    strMealThumb: item.thumbnail,
    strInstructions: item.instructions
  }
}

async function loadOnce() {
  if (initialized) return
  initialized = true

  loading.value = true
  error.value = null

  try {
    const items = await getMerkliste()
    merkliste.value = (items ?? []).map(mapFromBackend)
  } catch (e: any) {
    console.error(e)
    error.value = e.message ?? 'Fehler beim Laden der Merkliste'
  } finally {
    loading.value = false
  }
}

function isInMerkliste(mealId: string) {
  return merkliste.value.some(m => m.idMeal === mealId)
}

async function toggle(meal: Meal) {
  await loadOnce()

  if (isInMerkliste(meal.idMeal)) {
    await removeFromMerkliste(meal.idMeal)
    merkliste.value = merkliste.value.filter(m => m.idMeal !== meal.idMeal)
  } else {
    await addToMerkliste(meal)
    merkliste.value.push(meal)
  }
}

export function useMerkliste() {
  void loadOnce()    // beim ersten Aufruf laden

  return {
    merkliste,
    loading,
    error,
    toggle,
    isInMerkliste
  }
}
