import type { Config } from 'tailwindcss'

const config: Config = {
  content: [
    './pages/**/*.{js,ts,jsx,tsx,mdx}',
    './components/**/*.{js,ts,jsx,tsx,mdx}',
    './app/**/*.{js,ts,jsx,tsx,mdx}',
  ],
  theme: {
    extend: {
      colors: {
        background: '#1E2838',
        'background-dark': '#0E1828',
        foreground: '#FFFFFF',
        dollar: '#787878',
        path: '#32C864',
        directory: '#3264DC',
        file: '#8246B4',
        success: '#32DC32',
        error: '#DC3232',
        info: '#3232DC',
      },
    }
  },
  plugins: [
    require('tailwind-scrollbar'), // ({ nocompatible: true })
  ],
}
export default config
