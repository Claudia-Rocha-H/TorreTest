/**
 * API Types - Core API interfaces and response structures
 * Following screaming architecture - API types are centralized here
 */

/**
 * Base API response structure
 */
export interface BaseApiResponse {
  source: string;
}

/**
 * Person result from search API
 */
export interface PersonResult {
  id: string;
  name: string;
  professionalHeadline: string | null;
  picture: string | null;
  username: string;
}

/**
 * Search response structure
 */
export interface SearchResponse {
  results: PersonResult[];
  pagination?: PaginationInfo;
}

/**
 * Pagination information structure
 */
export interface PaginationInfo {
  total: number;
  currentPage: number;
  pageSize: number;
  totalResults: number;
}

/**
 * Organization structure for person details
 */
export interface Organization {
  id: string;
  name: string;
}

/**
 * Experience entry for person details
 */
export interface Experience {
  id: string;
  name: string;
  organizations: Organization[];
  fromMonth: string;
  fromYear: string;
  toMonth: string | null;
  toYear: string | null;
}

/**
 * Education entry for person details
 */
export interface Education {
  id: string;
  name: string;
  organizations: Organization[];
  fromMonth: string;
  fromYear: string;
  toMonth: string | null;
  toYear: string | null;
}

/**
 * Person details response structure
 */
export interface PersonDetailsResponse {
  person: {
    id: string;
    name: string;
    professionalHeadline: string | null;
    picture: string | null;
    location: {
      name: string;
    } | null;
    summaryOfBio?: string | null;
  };
  strengths?: Array<{
    id?: string;
    name: string;
    proficiency?: string | null;
  }>;
  experiences?: Experience[];
  education?: Education[];
}

/**
 * Skill compensation response from Torre.ai
 */
export interface SkillCompensationResponse extends BaseApiResponse {
  skill: string;
  averageCompensation: number;
  medianCompensation: number;
  minCompensation: number;
  maxCompensation: number;
  currency: string;
  periodicity: string;
  dataPoints: number;
}

/**
 * Proficiency level data structure
 */
export interface ProficiencyLevel {
  level: string;
  percentage: number;
  count: number;
  averageExperience?: string | null;
}

/**
 * Skill distribution response from Torre.ai
 */
export interface SkillDistributionResponse extends BaseApiResponse {
  skill: string;
  distribution: ProficiencyLevel[];
  totalProfiles: number;
}

/**
 * Combined skill insight data for UI state management
 */
export interface SkillInsightData {
  compensation?: SkillCompensationResponse;
  distribution?: ProficiencyLevel[];
  isLoading: boolean;
  error: string | null;
}

/**
 * API Error structure
 */
export interface ApiError {
  message: string;
  code?: string;
  status?: number;
}
