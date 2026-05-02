import { render, screen, waitFor } from '@testing-library/react'
import ProfilePage from '@/app/profile/[username]/page'
import * as api from '@/lib/api'

jest.mock('@/lib/api')
jest.mock('next/navigation', () => ({
  useParams: () => ({ username: 'testuser' }),
  useRouter: () => ({ back: jest.fn() }),
}))

const mockGetPersonDetails = api.getPersonDetails as jest.MockedFunction<typeof api.getPersonDetails>

describe('ProfilePage Component', () => {
  const mockProfileData = {
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
  }

  beforeEach(() => {
    jest.clearAllMocks()
  })

  it('should render loading state initially', () => {
    mockGetPersonDetails.mockImplementation(() => new Promise(() => {}))
    render(<ProfilePage />)
    expect(screen.getByText(/Loading profile/)).toBeInTheDocument()
  })

  it('should render profile data when loaded', async () => {
    mockGetPersonDetails.mockResolvedValue(mockProfileData as any)
    render(<ProfilePage />)
    await waitFor(() => {
      expect(screen.getByText('Test User')).toBeInTheDocument()
    })
  })

  it('should render professional headline', async () => {
    mockGetPersonDetails.mockResolvedValue(mockProfileData as any)
    render(<ProfilePage />)
    await waitFor(() => {
      expect(screen.getByText('Software Engineer')).toBeInTheDocument()
    })
  })

  it('should render location', async () => {
    mockGetPersonDetails.mockResolvedValue(mockProfileData as any)
    render(<ProfilePage />)
    await waitFor(() => {
      expect(screen.getByText('New York')).toBeInTheDocument()
    })
  })

  it('should render skills section', async () => {
    mockGetPersonDetails.mockResolvedValue(mockProfileData as any)
    render(<ProfilePage />)
    await waitFor(() => {
      expect(screen.getByText(/Skills & Strengths/)).toBeInTheDocument()
    })
  })

  it('should render experience section', async () => {
    mockGetPersonDetails.mockResolvedValue(mockProfileData as any)
    render(<ProfilePage />)
    await waitFor(() => {
      expect(screen.getByText(/Professional Experience/)).toBeInTheDocument()
    })
  })

  it('should render education section', async () => {
    mockGetPersonDetails.mockResolvedValue(mockProfileData as any)
    render(<ProfilePage />)
    await waitFor(() => {
      expect(screen.getByText(/Education/)).toBeInTheDocument()
    })
  })

  it('should render error state when API fails', async () => {
    mockGetPersonDetails.mockRejectedValue(new Error('Profile not found'))
    render(<ProfilePage />)
    await waitFor(() => {
      expect(screen.getByText(/Profile Not Found/)).toBeInTheDocument()
    })
  })

  it('should render back button', async () => {
    mockGetPersonDetails.mockResolvedValue(mockProfileData as any)
    render(<ProfilePage />)
    await waitFor(() => {
      expect(screen.getByText(/Back to Search/)).toBeInTheDocument()
    })
  })
})
