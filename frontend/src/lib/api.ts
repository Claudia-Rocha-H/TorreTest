/**
 * API utility functions for backend communication.
 * 
 * This module provides a clean interface for interacting with our Spring Boot backend,
 * which in turn proxies requests to Torre.ai's search API. Handles all HTTP communication,
 * error handling, and type safety for search operations.
 */

import type {
  SearchResponse,
  PersonDetailsResponse,
  SkillCompensationResponse,
  SkillDistributionResponse
} from '../types';

/** Base URL for our Spring Boot backend API */
const BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080/api';

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

/**
 * Analyzes skill compensation using Torre.ai data.
 * @param skill The skill to analyze (e.g., "javascript", "python").
 * @param userProficiency Optional user proficiency level.
 * @returns A promise that resolves to SkillCompensationResponse.
 */
export const analyzeSkillCompensation = async (
  skill: string
): Promise<SkillCompensationResponse> => {
  try {
    const response = await fetch(`${BASE_URL}/analyze/skill-compensation?skill=${encodeURIComponent(skill)}`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
      },
    });

    if (!response.ok) {
      throw new Error(`Failed to analyze skill compensation: ${response.status}`);
    }

    return response.json();
  } catch (error) {
    console.error('Error analyzing skill compensation:', error);
    throw error;
  }
};

/**
 * Gets skill proficiency distribution using Torre.ai data.
 * @param skill The skill to analyze (e.g., "javascript", "python").
 * @returns A promise that resolves to SkillDistributionResponse.
 */
export const getSkillProficiencyDistribution = async (
  skill: string
): Promise<SkillDistributionResponse> => {
  try {
    const response = await fetch(`${BASE_URL}/analyze/skill-distribution?skill=${encodeURIComponent(skill)}`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
      },
    });

    if (!response.ok) {
      throw new Error(`Failed to get skill distribution: ${response.status}`);
    }

    return response.json();
  } catch (error) {
    console.error('Error getting skill proficiency distribution:', error);
    throw error;
  }
};