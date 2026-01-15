import '@testing-library/jest-dom/vitest'
import { vi } from 'vitest'

// Fürs Mocken zuständig
vi.stubGlobal('alert', vi.fn())
