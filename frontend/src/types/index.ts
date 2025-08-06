/**
 * Types Index - Central export point for all types
 * Following screaming architecture - all types are accessible from this single entry point
 */

// API Types
export type {
  BaseApiResponse,
  PersonResult,
  SearchResponse,
  PaginationInfo,
  Organization,
  Experience,
  Education,
  PersonDetailsResponse,
  SkillCompensationResponse,
  ProficiencyLevel,
  SkillDistributionResponse,
  SkillInsightData,
  ApiError
} from './api';

// Skill Analysis Types
export type {
  SkillProficiencyLevel,
  CurrencyCode,
  CompensationPeriodicity,
  SkillAnalysisRequest,
  SkillAnalysisPanelProps,
  CompensationDataPoint,
  ProficiencyDistributionItem
} from './skill-analysis';

// Search Types
export type {
  SearchRequest,
  SearchFilters,
  CompensationRange,
  PaginationOptions,
  SearchMetadata,
  EnhancedSearchResponse,
  SearchInputProps,
  SearchResultListProps
} from './search';

// UI Types (if we need them later)
export interface UIState {
  isLoading: boolean;
  error: string | null;
}

export interface ComponentProps {
  className?: string;
  children?: React.ReactNode;
}
