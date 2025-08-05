'use client';

import React, { useState } from 'react';
import GlobalHeader from '../components/GlobalHeader';
import SearchHeader from '../features/search/SearchHeader';
import SearchInput from '../features/search/SearchInput';
import SearchResultList from '../features/search/SearchResultList';
import Pagination from '../components/Pagination';
import { searchPeople, PersonResult, PaginationInfo } from '../lib/api';
import { useRouter } from 'next/navigation';

/**
 * Main search page component replicating Torre.ai's people search functionality.
 * 
 * This page provides:
 * - Global header with Torre.ai navigation
 * - Search header with category tabs
 * - Search input with real-time query processing
 * - Paginated results display with hexagonal avatars
 * - Professional error handling and loading states
 * 
 * The component manages search state, pagination, and integrates with our backend
 * to provide a seamless Torre.ai-like experience.
 */
export default function Home() {
  // Search results and pagination state
  const [searchResults, setSearchResults] = useState<PersonResult[]>([]);
  const [pagination, setPagination] = useState<PaginationInfo | null>(null);
  const [currentPage, setCurrentPage] = useState(1);
  const [currentQuery, setCurrentQuery] = useState('');
  
  // UI state management
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  
  const router = useRouter();

  /** Number of results to display per page (matches Torre.ai) */
  const PAGE_SIZE = 21;

  /**
   * Handles initial search submission from the search input.
   * Resets pagination to first page and initiates new search.
   * 
   * @param query The search term entered by the user
   */
  const handleSearch = async (query: string) => {
    setCurrentQuery(query);
    setCurrentPage(1);
    await performSearch(query, 1);
  };

  /**
   * Performs the actual search API call with comprehensive error handling.
   * 
   * Fetches up to 100 results from Torre.ai through our backend, then implements
   * client-side pagination for smooth UX. This approach provides better performance
   * than server-side pagination for moderate result sets.
   * 
   * @param query The search term to send to Torre.ai
   * @param page The page number for UI state (used for pagination calculation)
   */
  const performSearch = async (query: string, page: number) => {
    setLoading(true);
    setError(null);

    try {
      // Fetch comprehensive result set from Torre.ai
      const data = await searchPeople(query, 100);
      setSearchResults(data.results);
      
      // Configure client-side pagination
      const totalResults = data.results.length;
      setPagination({
        total: Math.ceil(totalResults / PAGE_SIZE),
        currentPage: page,
        pageSize: PAGE_SIZE,
        totalResults: totalResults
      });
    } catch (err) {
      console.error('Failed to fetch search results:', err);
      setError('Failed to fetch search results. Please try again.');
      setSearchResults([]);
      setPagination(null);
    } finally {
      setLoading(false);
    }
  };

  /**
   * Handles pagination navigation when user clicks previous/next.
   * Provides smooth scrolling to top for better UX.
   * 
   * @param page The target page number to navigate to
   */
  const handlePageChange = (page: number) => {
    setCurrentPage(page);
    // Smooth scroll to top when changing pages for better UX
    window.scrollTo({ top: 0, behavior: 'smooth' });
  };

  /**
   * Calculates and returns the subset of results for the current page.
   * Implements client-side pagination by slicing the full result array.
   * 
   * @returns Array of PersonResult objects for the current page
   */
  const getCurrentPageResults = () => {
    const startIndex = (currentPage - 1) * PAGE_SIZE;
    const endIndex = startIndex + PAGE_SIZE;
    return searchResults.slice(startIndex, endIndex);
  };

  /**
   * Navigates to individual person profile page.
   * Currently implements basic routing - can be extended to show detailed profile views.
   * 
   * @param personId The Torre.ai ggId of the selected person
   */
  const handleSelectPerson = (personId: string) => {
    router.push(`/profile/${personId}`);
  };

  return (
    <div className="min-h-screen bg-dark-bg text-light-text font-sans">
      <GlobalHeader />
      <SearchHeader />

      <main className="px-16 py-10">
        <div className="mt-10">
          <SearchInput onSearch={handleSearch} />
        </div>

        {loading && (
          <div className="mt-8 p-6 bg-dark-card rounded-lg h-96 flex items-center justify-center text-light-text">
            <p>Searching...</p>
          </div>
        )}

        {error && (
          <div className="mt-8 p-6 bg-badge text-white-text rounded-lg h-auto flex items-center justify-center">
            <p>{error}</p>
          </div>
        )}

        {!loading && !error && searchResults.length > 0 && (
          <>
            <SearchResultList results={getCurrentPageResults()} onSelectPerson={handleSelectPerson} />
            {pagination && (
              <Pagination
                currentPage={currentPage}
                totalResults={pagination.totalResults}
                pageSize={PAGE_SIZE}
                onPageChange={handlePageChange}
              />
            )}
          </>
        )}

        {!loading && !error && searchResults.length === 0 && (
          <div className="mt-8 p-6 bg-dark-card rounded-lg h-96 flex items-center justify-center text-light-text">
            <p>Your search results will appear here.</p>
          </div>
        )}
      </main>
    </div>
  );
}