<template>
  <div class="recipe-search" :class="{ dark: darkMode, light: !darkMode }">
    <!-- Header -->
    <header class="header">
      <button @click="toggleDarkMode" class="mode-toggle">
        {{ darkMode ? ' Light' : ' Dark' }}
      </button>

      <h1 class="logo">🍴 Food Book</h1>

      <button class="wishlist-btn" @click="toggleMerkliste">
        📋 Merkliste <span v-if="savedMeals.length">({{ savedMeals.length }})</span>
      </button>
    </header>

    <!--Suchleiste-->
    <div class="search-wrap">
      <div class="search">
        <span class="icon">🔍</span>
        <input
          ref="searchInput"
          type="text"
          v-model="query"
          placeholder="Rezept suchen (z. B. Soup, Muffin, Spaghetti, ...)"
          @input="onQueryInput"
          @keyup.enter="runSearch"
          aria-label="Rezeptsuche"
        />
        <button v-if="query" class="clear" @click="clearSearch" aria-label="Eingabe löschen">✕</button>
      </div>
      <small class="hint">Mindestens ein Begriff (auf Englisch !!) · Drücke Enter zum Suchen</small>
    </div>

    <!--Error-->
    <div v-if="error" class="msg err">{{ error }}</div>
    <div v-else-if="loading" class="msg">Lade…</div>

    <!--Merkliste-->
    <section v-if="showMerkliste" class="list-section">
      <h2>📌 Gespeicherte Rezepte</h2>
      <div v-if="savedMeals.length" class="grid">
        <article v-for="m in savedMeals" :key="m.idMeal" class="card" @click="openMeal(m)">
          <img :src="m.strMealThumb" :alt="m.strMeal" />
          <div class="card-footer">
            <div class="title">{{ m.strMeal }}</div>
            <button class="remove" @click.stop="removeFromList(m.idMeal)">Entfernen</button>
          </div>
        </article>
      </div>
      <div v-else class="msg muted">Noch nichts gespeichert.</div>
    </section>

    <!--Suchergebnisse-->
    <section v-else class="grid">
      <article v-for="m in results" :key="m.idMeal" class="card" @click="openMeal(m)">
        <img :src="m.strMealThumb" :alt="m.strMeal" />
        <div class="title">{{ m.strMeal }}</div>
      </article>
      <div v-if="!loading && results.length === 0 && query.length >= 2" class="msg muted">
        Keine Treffer.
      </div>
    </section>

    <!--Rezept_Menü-->
    <div v-if="selectedMeal" class="modal" @click.self="selectedMeal = null">
      <div class="modal-content">
        <img v-if="selectedMeal.strMealThumb" :src="selectedMeal.strMealThumb" :alt="selectedMeal.strMeal" class="modal-poster" />
        <div class="modal-info">
          <button class="back" @click="selectedMeal = null">← Zur Suche</button>
          <h2>{{ selectedMeal.strMeal }}</h2>

          <h3>Zutaten</h3>
          <ul class="ingredients">
            <li v-for="(ing, idx) in ingredients" :key="idx">{{ ing }}</li>
          </ul>

          <h3>Beschreibung</h3>
          <p v-if="selectedMeal.strInstructions">{{ selectedMeal.strInstructions }}</p>
          <p v-else class="muted">Keine Beschreibung verfügbar.</p>

          <button class="save" @click="addToList(selectedMeal)">
            {{ isSaved(selectedMeal.idMeal) ? '✓ Bereits gemerkt' : 'Zur Merkliste hinzufügen' }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue';
import { searchMeals } from '@/lib/backendClient';
import { useMerkliste } from '@/lib/Merkliste';
import type { Meal } from '@/lib/Merkliste';

// Such-/UI-State
const query = ref('');
const results = ref<Meal[]>([]);
const loading = ref(false);
const error = ref('');
const selectedMeal = ref<Meal | null>(null);
const showMerkliste = ref(false);
const darkMode = ref(true);
const searchInput = ref<HTMLInputElement | null>(null);
let _debounceTimer: number | null = null;

// Merkliste aus Composable
const { merkliste, toggle, isInMerkliste } = useMerkliste();

// abgeleitete Werte
const savedMeals = computed(() => merkliste.value ?? []);

const ingredients = computed(() => {
  if (!selectedMeal.value) return [];
  const m: any = selectedMeal.value;
  const list: string[] = [];
  for (let i = 1; i <= 20; i++) {
    const ing = m[`strIngredient${i}`];
    const meas = m[`strMeasure${i}`];
    if (ing && ing.trim()) list.push(`${ing}${meas ? ` (${meas})` : ''}`);
  }
  return list;
});

/* ---- UI ---- */
function toggleDarkMode() {
  darkMode.value = !darkMode.value;
}

function toggleMerkliste() {
  showMerkliste.value = !showMerkliste.value;
}

function clearSearch() {
  query.value = '';
  results.value = [];
  error.value = '';
  loading.value = false;
  if (searchInput.value) {
    searchInput.value.focus();
  }
}

/* ---- Suche (mit Debounce) ---- */
function onQueryInput() {
  const q = query.value.trim();

  if (q.length < 1) {
    results.value = [];
    error.value = '';
    loading.value = false;
    return;
  }

  if (_debounceTimer !== null) {
    clearTimeout(_debounceTimer);
  }

  if (q.length < 2) {
    results.value = [];
    return;
  }

  _debounceTimer = window.setTimeout(runSearch, 250);
}

async function runSearch() {
  const q = query.value.trim();
  if (q.length < 2) return;

  loading.value = true;
  error.value = '';
  showMerkliste.value = false;

  try {
    const data = await searchMeals(q);
    results.value = data.meals ?? [];
  } catch (e) {
    console.error(e);
    error.value = 'Suche fehlgeschlagen';
    results.value = [];
  } finally {
    loading.value = false;
  }
}

/* ---- Rezept öffnen ---- */
function openMeal(meal: Meal) {
  selectedMeal.value = meal;
}

/* ---- Merkliste ---- */
function isSaved(id: string) {
  return isInMerkliste(id);
}

async function addToList(meal: Meal) {
  try {
    await toggle(meal);
    alert('Rezept gemerkt.');
  } catch (e) {
    console.error('Fehler beim Speichern in die Merkliste', e);
    alert('Fehler beim Speichern.');
  }
}

async function removeFromList(id: string) {
  try {
    const meal = merkliste.value.find(m => m.idMeal === id);
    if (meal) {
      await toggle(meal);
    }
  } catch (e) {
    console.error('Fehler beim Entfernen aus der Merkliste', e);
    alert('Fehler beim Entfernen.');
  }
}
</script>


<style scoped>
/* Layout - Farben */
.recipe-search { min-height: 100vh; background: #0f0f0f; color: #f5f5f5; }
.light { background: #f6f6f6; color: #111; }

/* Header */
.header {
  position: sticky; top: 0; z-index: 10;
  display: grid; grid-template-columns: 1fr auto 1fr; align-items: center;
  padding: 14px 20px; background: #0b0b0b; border-bottom: 1px solid #1f1f1f;
}
.logo { justify-self: center; margin: 0; font-size: 1.6rem; font-weight: 800; letter-spacing: .3px; }
.mode-toggle { justify-self: start; }
.wishlist-btn { justify-self: end; }

/* Knöpfe */
.mode-toggle, .wishlist-btn {
  padding: .45rem .9rem; border-radius: 999px; border: 1px solid #2b2b2b;
  background: #171717; color: #fff; cursor: pointer;
}
.mode-toggle:hover, .wishlist-btn:hover { background: #222; }

/* Suchleiste */
.search-wrap { max-width: 760px; margin: 28px auto 8px; text-align: center; }
.search { position: relative; display:flex; align-items:center; background:#1a1a1a; border:1px solid #272727;
  border-radius: 999px; padding: 10px 14px; box-shadow: 0 6px 24px rgba(0,0,0,.35);
}
.search .icon { margin: 0 10px 0 4px; opacity:.9; }
.search input {
  flex:1; background: transparent; border: none; outline: none; color: inherit;
  font-size: 1.05rem; padding: 6px 0;
}
.search .clear {
  border: none; background: transparent; color: #aaa; cursor: pointer; padding: 6px;
}
.hint { display:block; opacity:.7; margin-top: 6px; }

/* Grid */
.grid { max-width: 1100px; margin: 18px auto 40px; display:grid; gap:16px;
  grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
}
.card {
  background:#151515; border:1px solid #232323; border-radius: 14px; overflow:hidden; cursor:pointer;
  transition: transform .15s ease, box-shadow .15s ease;
}
.card:hover { transform: translateY(-4px); box-shadow: 0 10px 26px rgba(0,0,0,.35); }
.card img { width:100%; aspect-ratio: 16/9; object-fit:cover; display:block; }
.card .title { padding: .6rem .75rem .8rem; font-weight: 600; }

/* Merkliste */
.list-section { max-width: 1100px; margin: 10px auto; padding: 0 10px; }
.card-footer { display:flex; justify-content:space-between; align-items:center; padding:.6rem .75rem; }
.remove {
  border:1px solid #2b2b2b; background:#1b1b1b; color:#eee; border-radius: 999px; padding:.25rem .6rem; cursor:pointer;
}
.remove:hover { background:#232323; }

/* Nachrichten */
.msg { text-align:center; margin: 14px 0; }
.err { color:#ff6161; }
.muted { color:#9a9a9a; }

/* Model */
.modal { position: fixed; inset:0; background: rgba(0,0,0,.85); display:flex; align-items:center; justify-content:center; z-index:99; padding:18px; }
.modal-content { background:#171717; border:1px solid #242424; border-radius: 14px; max-width: 980px; width:100%;
  display:grid; grid-template-columns: 360px 1fr; gap:16px; padding:18px; color:#eee; }
.modal-poster { width:100%; border-radius:10px; border:1px solid #242424; object-fit:cover; }
.modal-info h2 { margin: 4px 0 10px; }
.ingredients { display:grid; grid-template-columns: repeat(auto-fill, minmax(180px,1fr)); gap:6px; list-style:none; padding:0; }
.back, .save { margin-top: 8px; padding:.45rem .9rem; border-radius:10px; border:1px solid #2b2b2b; background:#1b1b1b; color:#fff; cursor:pointer; }
.save { background:#1f3d1f; border-color:#2b4b2b; }
.save:hover { background:#265326; }
.back:hover { background:#232323; }

@media (max-width: 860px) {
  .modal-content { grid-template-columns: 1fr; }
}
</style>
