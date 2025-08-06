/**
 * Search Types - Domain-specific types for search functionality
 * Following screaming architecture - search types are centralized here
 */

import type { PersonResult } from './api';

/**
 * Search request parameters
 */
export interface SearchRequest {
  query: string;
  filters?: SearchFilters;
  pagination?: PaginationOptions;
}

/**
 * Search filters
 */
export interface SearchFilters {
  skills?: string[];
  location?: string;
  experience?: string;
  compensation?: CompensationRange;
}

/**
 * Compensation range filter
 */
export interface CompensationRange {
  min?: number;
  max?: number;
  currency?: string;
  periodicity?: string;
}

/**
 * Pagination options
 */
export interface PaginationOptions {
  page?: number;
  limit?: number;
  offset?: number;
}

/**
 * Search result metadata
 */
export interface SearchMetadata {
  total: number;
  page: number;
  limit: number;
  hasNext: boolean;
  hasPrevious: boolean;
}

/**
 * Enhanced search response with metadata
 */
export interface EnhancedSearchResponse {
  results: PersonResult[];
  metadata: SearchMetadata;
}

/**
 * Search input component props
 */
export interface SearchInputProps {
  onSearch: (query: string) => void;
  placeholder?: string;
  isLoading?: boolean;
  initialValue?: string;
}

/**
 * Search result list component props
 */
export interface SearchResultListProps {
  results: PersonResult[];
  isLoading?: boolean;
  onPersonClick?: (person: PersonResult) => void;
}

// Re-export PersonResult from api types for convenience
export type { PersonResult } from './api';
