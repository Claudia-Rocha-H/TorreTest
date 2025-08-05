import GlobalHeader from '../components/GlobalHeader';
import SearchHeader from '../features/search/SearchHeader';
import SearchInput from '../features/search/SearchInput';

/**
 * Main search page
 */
export default function Home() {
  return (
    <div className="min-h-screen bg-dark-bg text-light-text font-sans">
      {/* Header navigation */}
      <GlobalHeader />

      {/* Search header with tabs */}
      <SearchHeader />

      {/* Main content */}
      <main className="px-16 py-10">
        {/* Search input */}
        <div className="mt-10">
          <SearchInput />
        </div>

        {/* Results placeholder */}
        <div className="mt-8 p-6 bg-dark-card rounded-lg h-96 flex items-center justify-center text-light-text">
          <p>Your search results will appear here.</p>
        </div>
      </main>
    </div>
  );
}
