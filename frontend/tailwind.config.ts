import type { Config } from 'tailwindcss'
import { nextui } from '@nextui-org/react';

const colors = {
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
}

const config: Config = {
  content: [
    './pages/**/*.{js,ts,jsx,tsx,mdx}',
    './components/**/*.{js,ts,jsx,tsx,mdx}',
    './app/**/*.{js,ts,jsx,tsx,mdx}',
    "./node_modules/@nextui-org/theme/dist/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: { ...colors },
      boxShadow: {
        'terminal': `inset 0 -35px 60px -15px ${colors.background}`,
      },
      backgroundImage: {
        'gradient-radial': 'radial-gradient(var(--tw-gradient-stops))',
      }
    }
  },
  darkMode: "class",
  plugins: [
    require('tailwind-scrollbar'), // ({ nocompatible: true })
    nextui(),
  ],
}
export default config
