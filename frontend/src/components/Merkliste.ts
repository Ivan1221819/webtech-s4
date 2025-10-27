import { ref } from 'vue';
import type { Meal } from '@/lib/backend';

const KEY = 'wishlist-v1';
const items = ref<Meal[]>(load());

function load(): Meal[] {
  try { return JSON.parse(localStorage.getItem(KEY) || '[]'); } catch { return []; }
}
function persist() { localStorage.setItem(KEY, JSON.stringify(items.value)); }

export function useWishlist() {
  function add(meal: Meal) {
    if (!items.value.some(m => m.idMeal === meal.idMeal)) {
      items.value.push({ idMeal: meal.idMeal, name: meal.name ?? meal.strMeal, thumbUrl: meal.thumbUrl ?? meal.strMealThumb });
      persist();
    }
  }
  function remove(idMeal: string) {
    items.value = items.value.filter(m => m.idMeal !== idMeal);
    persist();
  }
  function clear() { items.value = []; persist(); }
  function has(idMeal: string) { return items.value.some(m => m.idMeal === idMeal); }

  return { items, add, remove, clear, has };
}

export default class Merkliste {
}
