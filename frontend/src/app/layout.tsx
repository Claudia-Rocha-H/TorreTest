// Global styles and font setup
import './globals.css'; 
import { Inter, Poppins } from 'next/font/google'; 

// Font configurations
const inter = Inter({ 
  subsets: ['latin'], 
  variable: '--font-inter' 
});

const poppins = Poppins({
  subsets: ['latin'],
  weight: ['400', '500', '600', '700'],
  variable: '--font-poppins',
});

/**
 * Root layout component that wraps all pages
 */
export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html 
      lang="en" 
      className={`${inter.variable} ${poppins.variable} font-sans`}
    >
      <body className="bg-dark-bg text-light-text">{children}</body>
    </html>
  );
}


