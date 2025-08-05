/**
 * API utility functions for backend communication.
 * 
 * This module provides a clean interface for interacting with our Spring Boot backend,
 * which in turn proxies requests to Torre.ai's search API. Handles all HTTP communication,
 * error handling, and type safety for search operations.
 */

/** Base URL for our Spring Boot backend API */
const BASE_URL = process.env.NEXT_PUBLIC_BACKEND_URL || 'http://localhost:8080/api';

/**
 * Represents a person result from Torre.ai search.
 * Maps the essential fields needed for displaying search results in the UI.
 */
export interface PersonResult {
  /** Unique identifier (Torre.ai ggId) */
  id: string;
  
  /** Person's full name */
  name: string;
  
  /** Professional headline or job title */
  professionalHeadline: string | null;
  
  /** Profile image URL */
  picture: string | null;
}

/**
 * Pagination metadata for managing large result sets.
 * Provides all information needed for displaying pagination controls and result counts.
 */
export interface PaginationInfo {
  /** Total number of pages available */
  total: number;
  
  /** Current page being displayed (1-based) */
  currentPage: number;
  
  /** Number of items per page */
  pageSize: number;
  
  /** Total number of results found */
  totalResults: number;
}

/**
 * Complete search response structure.
 * Combines search results with pagination information for comprehensive UI support.
 */
export interface SearchResponse {
  /** Array of person results from the search */
  results: PersonResult[];
  
  /** Pagination metadata for result navigation */
  pagination: PaginationInfo;
}

/**
 * Searches for people by name through our backend proxy.
 * 
 * This function communicates with our Spring Boot backend, which handles the actual
 * Torre.ai API integration. Supports configurable result limits for pagination optimization.
 * 
 * @param query The search term to look for (person names, skills, etc.)
 * @param limit Maximum number of results to fetch from Torre.ai (default: 100)
 * @returns Promise resolving to SearchResponse with results and pagination info
 * @throws Error if the request fails or returns non-200 status
 */
export const searchPeople = async (query: string, limit: number = 100): Promise<SearchResponse> => {
  try {
    const response = await fetch(`${BASE_URL}/search/people`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ query, limit }),
    });

    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.message || `HTTP error! status: ${response.status}`);
    }

    return response.json();
  } catch (error) {
    console.error('Error searching people:', error);
    throw error;
  }
};