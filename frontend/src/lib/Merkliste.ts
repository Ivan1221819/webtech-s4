
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

      // Name, Bild, Beschreibung
      const newMeal: Meal = {
        idMeal: meal.idMeal,
        strMeal: meal.strMeal,
        strMealThumb: meal.strMealThumb,
        strInstructions: meal.strInstructions,
      };

      // Zutaten
      const extras: any = {};
      for (let i = 1; i <= 20; i++) {
        const ing = meal[`strIngredient${i}`];
        const mea = meal[`strMeasure${i}`];
        if (ing) extras[`strIngredient${i}`] = ing;
        if (mea) extras[`strMeasure${i}`] = mea;
      }

      // Name, Bild, Beschreibung + Zutaten
      Object.assign(newMeal, extras);

      items.value.push(newMeal);
      persist();
    }
  }

  // Löscht gespeicherte Rezepte
  function remove(idMeal: string) {
    items.value = items.value.filter(m => m.idMeal !== idMeal);
    persist();
  }


  function has(idMeal: string) {
    return items.value.some(m => m.idMeal === idMeal);
  }

  return { items, add, remove, has };
}
