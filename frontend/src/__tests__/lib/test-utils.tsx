// Shared test utilities and mock data - reduces duplicated lines across test files
import { render, RenderOptions } from '@testing-library/react';
import type { ReactElement } from 'react';

// Mock search results for SearchResultList and Home tests
export const mockSearchResults = [
  { id: '1', username: 'johndoe', name: 'John Doe', professionalHeadline: 'Engineer', picture: null },
  { id: '2', username: 'janesmith', name: 'Jane Smith', professionalHeadline: 'Designer', picture: null },
];

// Mock profile data for ProfilePage tests
export const mockProfileData = {
  person: {
    id: 'testuser',
    name: 'Test User',
    professionalHeadline: 'Software Engineer',
    picture: 'https://example.com/test.jpg',
    location: { name: 'New York' },
    summaryOfBio: 'A passionate developer',
  },
  strengths: [
    { id: '1', name: 'JavaScript', proficiency: 'expert' },
    { id: '2', name: 'React', proficiency: 'advanced' },
  ],
  experiences: [
    {
      id: 'exp1',
      name: 'Senior Developer',
      organizations: [{ id: 'org1', name: 'Tech Corp' }],
      fromMonth: 'Jan',
      fromYear: '2020',
      toMonth: null,
      toYear: null,
    },
  ],
  education: [
    {
      id: 'edu1',
      name: 'Computer Science',
      organizations: [{ id: 'org2', name: 'University' }],
      fromMonth: 'Sep',
      fromYear: '2016',
      toMonth: 'May',
      toYear: '2020',
    },
  ],
};

// Mock compensation data for SkillAnalysisPanel tests
export const mockCompensationData = {
  skill: 'javascript',
  averageCompensation: 90000,
  medianCompensation: 85000,
  currency: 'USD',
  periodicity: 'yearly',
};

// Mock distribution data for SkillAnalysisPanel tests
export const mockDistributionData = {
  skill: 'javascript',
  distribution: [{ level: 'advanced', percentage: 60, count: 120 }],
  totalProfiles: 200,
};

// Create jest mock function
export const createMockFn = () => jest.fn();

// Mock API functions for SkillAnalysisPanel tests
export const mockApi = {
  searchPeople: createMockFn(),
  getPersonDetails: createMockFn(),
  analyzeSkillCompensation: jest.fn().mockResolvedValue(mockCompensationData),
  getSkillProficiencyDistribution: jest.fn().mockResolvedValue(mockDistributionData),
};

// Setup mock fetch for success responses
export const setupMockFetch = (response: unknown, ok = true) => {
  global.fetch = jest.fn().mockResolvedValue({
    ok,
    status: ok ? 200 : 500,
    json: jest.fn().mockResolvedValue(response),
  } as any);
};

// Setup mock fetch for error responses
export const setupErrorFetch = (status: number) => {
  global.fetch = jest.fn().mockResolvedValue({
    ok: false,
    status,
    statusText: 'Error',
  } as any);
};

// Mock router for next/navigation
export const mockRouter = () => ({ push: createMockFn(), back: createMockFn() });

// Mock next/navigation hooks
export const mockNextNavigation = () => ({
  useRouter: () => mockRouter(),
  useParams: () => ({ username: 'testuser' }),
});

// Custom render with providers
export const renderWithProviders = (
  ui: ReactElement,
  options?: Omit<RenderOptions, 'wrapper'>
) => render(ui, options);

// Helper to delay execution
export const delay = (ms: number) => new Promise(resolve => setTimeout(resolve, ms));
