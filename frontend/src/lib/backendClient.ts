const BASE = import.meta.env.VITE_API_BASE ?? 'http://localhost:8080';

// Suche: /api/meals?q=
export async function searchMeals(q: string) {
  const res = await fetch(`${BASE}/api/meals?q=${encodeURIComponent(q ?? '')}`);
  if (!res.ok) throw new Error(`HTTP ${res.status}`);
  return res.json() as Promise<{ meals: Meal[] | null }>;
}

// Detail: /api/meals/{id}  (liefert ebenfalls { meals: [...] } mit genau 1 Meal)
export async function getMealById(id: string) {
  const res = await fetch(`${BASE}/api/meals/${encodeURIComponent(id)}`);
  if (!res.ok) throw new Error(`HTTP ${res.status}`);
  return res.json() as Promise<{ meals: Meal[] | null }>;
}

// Minimaler Typ (passt auf beide Endpunkte)
export type Meal = {
  idMeal: string;
  name?: string;           // vom Backend normalisiert (optional)
  thumbUrl?: string;       // dito
  strMeal?: string;        // original TheMealDB-Feld
  strMealThumb?: string;   // original
  strInstructions?: string;
  // Zutaten/Measures kommen im Detail als strIngredient1..20 / strMeasure1..20
  [k: string]: any;
};
