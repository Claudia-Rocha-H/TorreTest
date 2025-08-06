/**
 * Skill Analysis Types - Domain-specific types for skill analysis features
 * Following screaming architecture - skill analysis types are centralized here
 */

/**
 * Skill proficiency levels enum-like type
 */
export type SkillProficiencyLevel = 
  | 'beginner' 
  | 'intermediate' 
  | 'advanced' 
  | 'expert' 
  | 'master'
  | 'proficient'
  | 'novice'
  | 'no-experience-interested'
  | 'unknown';

/**
 * Compensation currency types
 */
export type CurrencyCode = 'USD' | 'EUR' | 'GBP' | 'CAD' | 'AUD' | 'COP';

/**
 * Compensation periodicity
 */
export type CompensationPeriodicity = 'hourly' | 'monthly' | 'yearly';

/**
 * Skill analysis request parameters
 */
export interface SkillAnalysisRequest {
  skill: string;
  userProficiency?: SkillProficiencyLevel;
  location?: string;
  experience?: string;
}

/**
 * Skill insight panel props
 */
export interface SkillAnalysisPanelProps {
  /** Name of the skill being analyzed */
  skillName: string;
  /** User's proficiency level in this skill */
  userProficiency: string | null;
  /** Function to close the analysis panel */
  onClose: () => void;
  /** Whether the panel is currently visible */
  isVisible: boolean;
}

/**
 * Compensation data point for visualization
 */
export interface CompensationDataPoint {
  label: string;
  value: number;
  currency: CurrencyCode;
  periodicity: CompensationPeriodicity;
  color?: string;
}

/**
 * Proficiency distribution item for visualization
 */
export interface ProficiencyDistributionItem {
  level: SkillProficiencyLevel;
  percentage: number;
  count: number;
  color: string;
  label: string;
}
