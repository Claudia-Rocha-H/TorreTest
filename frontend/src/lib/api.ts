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
  
  /** Torre.ai username for profile routing */
  username: string;
}

/**
 * Detailed person profile information from Torre.ai genome/bios endpoint.
 * Contains comprehensive profile data for individual person pages.
 */
export interface PersonDetailsResponse {
  /** Basic person information */
  person: PersonDetails;
  
  /** List of professional strengths and skills */
  strengths: Skill[];
  
  /** Professional experience history */
  experiences: Experience[];
  
  /** Educational background */
  education: Education[];
}

/**
 * Location information with geographic details.
 */
export interface Location {
  /** Full location name */
  name: string;
  
  /** Short location name */
  shortName: string | null;
  
  /** Country name */
  country: string | null;
  
  /** ISO country code */
  countryCode: string | null;
  
  /** Geographic latitude */
  latitude: number | null;
  
  /** Geographic longitude */
  longitude: number | null;
  
  /** Timezone identifier */
  timezone: string | null;
  
  /** Google Places ID */
  placeId: string | null;
}

/**
 * Core person profile information.
 */
export interface PersonDetails {
  /** Torre.ai unique identifier */
  id: string;
  
  /** Full name */
  name: string;
  
  /** Professional headline */
  professionalHeadline: string | null;
  
  /** Profile picture URL */
  picture: string | null;
  
  /** Professional bio summary */
  summaryOfBio: string | null;
  
  /** Public username for routing */
  publicId: string;
  
  /** Location information */
  location: Location | null;
}

/**
 * Professional skill or strength with proficiency information.
 */
export interface Skill {
  /** Skill identifier */
  id: string;
  
  /** Skill name */
  name: string;
  
  /** Experience level */
  experience: string | null;
  
  /** Proficiency rating */
  proficiency: string | null;
  
  /** Skill importance weight */
  weight: number | null;
}

/**
 * Professional experience entry.
 */
export interface Experience {
  /** Experience identifier */
  id: string;
  
  /** Job title or role */
  name: string;
  
  /** Associated organizations */
  organizations: Organization[];
  
  /** Start date components */
  fromMonth: string | null;
  fromYear: string | null;
  
  /** End date components */
  toMonth: string | null;
  toYear: string | null;
}

/**
 * Organization information for experiences and education.
 */
export interface Organization {
  /** Organization identifier */
  id: string;
  
  /** Organization name */
  name: string;
  
  /** Organization logo URL */
  picture: string | null;
}

/**
 * Educational background entry.
 */
export interface Education {
  /** Education identifier */
  id: string;
  
  /** Degree or program name */
  name: string;
  
  /** Educational institutions */
  organizations: Organization[];
  
  /** Start date components */
  fromMonth: string | null;
  fromYear: string | null;
  
  /** End date components */
  toMonth: string | null;
  toYear: string | null;
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

/**
 * Retrieves detailed profile information for a specific Torre.ai user.
 * 
 * Fetches comprehensive profile data including personal details, professional skills,
 * work experience, and educational background through our backend proxy.
 * 
 * @param username Torre.ai username/publicId to retrieve profile for
 * @returns Promise resolving to PersonDetailsResponse with complete profile information
 * @throws Error if the request fails, user not found, or returns non-200 status
 */
export const getPersonDetails = async (username: string): Promise<PersonDetailsResponse> => {
  try {
    const response = await fetch(`${BASE_URL}/profile/${encodeURIComponent(username)}`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
      },
    });

    if (!response.ok) {
      if (response.status === 404) {
        throw new Error(`Profile not found for username: ${username}`);
      }
      throw new Error(`Failed to fetch profile: ${response.status} ${response.statusText}`);
    }

    return response.json();
  } catch (error) {
    console.error('Error fetching person details:', error);
    throw error;
  }
};