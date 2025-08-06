import type { Config } from 'tailwindcss';
import defaultTheme from 'tailwindcss/defaultTheme';

const config: Config = {
  content: [
    './src/pages/**/*.{js,ts,jsx,tsx,mdx}',
    './src/components/**/*.{js,ts,jsx,tsx,mdx}',
    './src/app/**/*.{js,ts,jsx,tsx,mdx}',
    './src/features/**/*.{js,ts,jsx,tsx,mdx}',
  ],
  theme: {
    extend: {
      colors: {
        'dark-bg': '#27292d',
        'dark-bg-light': '#27292d', 
        'dark-card': '#2d2f33', 
        'light-text': '#dadadb',
        'primary-color': '#cddc39',
        'torre-primary': '#cddc39', 
        'active-tab-bg': '#4c4c4c', 
        'border-color': '#444', 
        'input-bg': '#27292d', 
        'input-placeholder': '#888', 
        'badge-bg': '#ff6b6b', 
        'badge': '#ff6b6b',
        'white-text': '#ffffff', 
      },
      fontFamily: {
        sans: ['Poppins', ...defaultTheme.fontFamily.sans],
        inter: ['var(--font-inter)', ...defaultTheme.fontFamily.sans],
      },
    },
  },
  plugins: [],
};

export default config;
