const BASE_URL = import.meta.env.VITE_BACKEND_URL ?? 'http://localhost:8080/api';

export async function searchMeals(query: string) {
  const res = await fetch(`${BASE_URL}/meals?q=${encodeURIComponent(query)}`);
  if (!res.ok) throw new Error('Fehler bei der Suche');
  return res.json();
}

export async function getMerkliste() {
  const res = await fetch(`${BASE_URL}/merkliste`);
  if (!res.ok) throw new Error('Fehler beim Laden der Merkliste');
  return res.json();
}

export async function addToMerkliste(meal: any) {
  const body = {
    mealId: meal.idMeal,
    title: meal.strMeal,
    thumbnail: meal.strMealThumb,
    instructions: meal.strInstructions ?? null,
  };

  const res = await fetch(`${BASE_URL}/merkliste`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(body),
  });
  if (!res.ok) throw new Error('Fehler beim Speichern in Merkliste');
  return res.json();
}

export async function removeFromMerkliste(mealId: string) {
  const res = await fetch(`${BASE_URL}/merkliste/${encodeURIComponent(mealId)}`, {
    method: 'DELETE',
  });
  if (!res.ok) throw new Error('Fehler beim Löschen aus Merkliste');
}
