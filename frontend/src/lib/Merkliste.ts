
import { ref } from 'vue';
import type { RawMeal as Meal } from '@/lib/backendClient';

const KEY = 'wishlist-v1';
const items = ref<Meal[]>(load());

function load(): Meal[] {
  try {
    return JSON.parse(localStorage.getItem(KEY) || '[]');
  } catch {
    return [];
  }
}
function persist() {
  localStorage.setItem(KEY, JSON.stringify(items.value));
}

export function useWishlist() {
  function add(meal: Meal) {
    if (!items.value.some(m => m.idMeal === meal.idMeal)) {
      items.value.push({
        idMeal: meal.idMeal,
        strMeal: meal.strMeal,
        strMealThumb: meal.strMealThumb
      });
      persist();
    }
  }
  function remove(idMeal: string) {
    items.value = items.value.filter(m => m.idMeal !== idMeal);
    persist();
  }
  function clear() {
    items.value = [];
    persist();
  }
  function has(idMeal: string) {
    return items.value.some(m => m.idMeal === idMeal);
  }

  return { items, add, remove, clear, has };
}
