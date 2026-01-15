import { mount } from '@vue/test-utils'
import { describe, it, expect } from 'vitest'
import App from '../App.vue'

// 1) Testet, ob App.vue korrekt mountet und router-view rendert
describe('App', () => {
  it('mounts', () => {
    const wrapper = mount(App, {
      global: {
        stubs: {
          'router-view': { template: '<div>You did it!</div>' },
        },
      },
    })

    expect(wrapper.text()).toContain('You did it!')
  })
})
