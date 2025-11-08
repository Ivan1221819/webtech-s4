const RAW = (import.meta.env as any).VITE_API_BASE as string | undefined;
export const BASE = (RAW && /^https?:\/\//i.test(RAW) ? RAW : 'http://localhost:8080').replace(/\/+$/, '');

export type RawMeal = {
  idMeal: string;
  strMeal?: string; strMealThumb?: string; strInstructions?: string;
  [k: `strIngredient${number}`]: string | undefined;
  [k: `strMeasure${number}`]: string | undefined;
};
export type MealsResponse = { meals: RawMeal[] | null };

function url(path: string, params?: Record<string,string|undefined>) {
  const u = new URL(BASE + path);
  if (params) for (const [k,v] of Object.entries(params)) if (v) u.searchParams.set(k, v);
  return u.toString();
}

export async function searchMeals(q: string): Promise<MealsResponse> {
  const res = await fetch(url('/api/meals', { q }));
  if (!res.ok) throw new Error(`HTTP ${res.status} for ${res.url}`);
  return res.json();
}

