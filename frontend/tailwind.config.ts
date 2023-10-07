import type { Config } from 'tailwindcss';
import { nextui } from '@nextui-org/react';
import colors from 'tailwindcss/colors';

// const colors = {
//   background: '#1E2838',
//   foreground: '#FFFFFF',
//   dollar: '#787878',
//   path: '#32C864',
//   directory: '#3264DC',
//   file: '#8246B4',
//   success: '#32DC32',
//   error: '#DC3232',
//   info: '#3232DC',
// }

const theme = {
  background: colors.neutral,
  foreground: colors.neutral[100],
  dollar: colors.neutral[500],
  path: colors.green[500],
  directory: colors.blue[600],
  file: colors.purple[600],
  success: colors.lime[600],
  error: colors.red[500],
  warning: colors.yellow[500],
  accent: colors.neutral
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
      colors: { ...theme },
      boxShadow: {
        'terminal': `inset 0 -35px 60px -15px ${theme.background}`,
      },
      backgroundImage: {
        'gradient-radial': 'radial-gradient(var(--tw-gradient-stops))',
      }
    }
  },
  darkMode: "class",
  plugins: [
    require('tailwind-scrollbar')({ nocompatible: true }),
    nextui(),
  ],
}
export default config
