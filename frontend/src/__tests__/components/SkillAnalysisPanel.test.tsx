import { render, screen, waitFor, fireEvent } from '@testing-library/react'
import SkillAnalysisPanel from '@/components/SkillAnalysisPanel'
import * as api from '@/lib/api'

jest.mock('@/lib/api')

const mockAnalyzeSkillCompensation = api.analyzeSkillCompensation as jest.MockedFunction<typeof api.analyzeSkillCompensation>
const mockGetSkillProficiencyDistribution = api.getSkillProficiencyDistribution as jest.MockedFunction<typeof api.getSkillProficiencyDistribution>

describe('SkillAnalysisPanel Component', () => {
  const defaultProps = {
    skillName: 'javascript',
    userProficiency: 'advanced',
    onClose: jest.fn(),
    isVisible: true,
  }

  beforeEach(() => {
    jest.clearAllMocks()
  })

  it('should not render when isVisible is false', () => {
    render(<SkillAnalysisPanel {...defaultProps} isVisible={false} />)
    expect(screen.queryByText(/Skill Analysis:/)).not.toBeInTheDocument()
  })

  it('should render loading state initially', () => {
    mockAnalyzeSkillCompensation.mockImplementation(() => new Promise(() => {}))
    mockGetSkillProficiencyDistribution.mockImplementation(() => new Promise(() => {}))

    render(<SkillAnalysisPanel {...defaultProps} />)
    expect(screen.getByText('Analyzing skill data...')).toBeInTheDocument()
  })

  it('should render panel header with skill name', async () => {
    mockAnalyzeSkillCompensation.mockResolvedValue(undefined as any)
    mockGetSkillProficiencyDistribution.mockResolvedValue(undefined as any)

    render(<SkillAnalysisPanel {...defaultProps} />)

    await waitFor(() => {
      expect(screen.getByText('Skill Analysis: javascript')).toBeInTheDocument()
    })
  })

  it('should show user proficiency when provided', async () => {
    mockAnalyzeSkillCompensation.mockResolvedValue(undefined as any)
    mockGetSkillProficiencyDistribution.mockResolvedValue(undefined as any)

    render(<SkillAnalysisPanel {...defaultProps} />)

    await waitFor(() => {
      expect(screen.getByText('Your level:')).toBeInTheDocument()
      expect(screen.getByText('advanced')).toBeInTheDocument()
    })
  })

  it('should call onClose when close button is clicked', async () => {
    mockAnalyzeSkillCompensation.mockResolvedValue(undefined as any)
    mockGetSkillProficiencyDistribution.mockResolvedValue(undefined as any)

    render(<SkillAnalysisPanel {...defaultProps} />)

    await waitFor(() => {
      expect(screen.getByText('Skill Analysis: javascript')).toBeInTheDocument()
    })

    const closeButton = screen.getByRole('button')
    fireEvent.click(closeButton)

    expect(defaultProps.onClose).toHaveBeenCalled()
  })

  it('should display compensation data when available', async () => {
    const compensationData = {
      skill: 'javascript',
      averageCompensation: 90000,
      medianCompensation: 85000,
      currency: 'USD',
      periodicity: 'yearly',
      dataPoints: 150,
      minCompensation: 50000,
      maxCompensation: 150000,
      source: 'Torre.ai',
    }

    mockAnalyzeSkillCompensation.mockResolvedValue(compensationData)
    mockGetSkillProficiencyDistribution.mockResolvedValue(undefined as any)

    render(<SkillAnalysisPanel {...defaultProps} />)

    await waitFor(() => {
      expect(screen.getByText('Compensation Insights')).toBeInTheDocument()
      expect(screen.getByText('Suggested Compensation')).toBeInTheDocument()
      expect(screen.getByText('Average Compensation')).toBeInTheDocument()
    })
  })

  it('should display distribution data when available', async () => {
    const distributionData = {
      skill: 'javascript',
      distribution: [
        { level: 'advanced', percentage: 60, count: 120, averageExperience: '5 years' },
        { level: 'beginner', percentage: 40, count: 80, averageExperience: '1 year' },
      ],
      totalProfiles: 200,
      source: 'Torre.ai',
    }

    mockAnalyzeSkillCompensation.mockResolvedValue(undefined as any)
    mockGetSkillProficiencyDistribution.mockResolvedValue(distributionData)

    render(<SkillAnalysisPanel {...defaultProps} />)

    await waitFor(() => {
      expect(screen.getByText('Proficiency Distribution')).toBeInTheDocument()
      expect(screen.getByText('Advanced')).toBeInTheDocument()
      expect(screen.getByText('Beginner')).toBeInTheDocument()
    })
  })

  it('should show no data message when both APIs return undefined', async () => {
    mockAnalyzeSkillCompensation.mockResolvedValue(undefined as any)
    mockGetSkillProficiencyDistribution.mockResolvedValue(undefined as any)

    render(<SkillAnalysisPanel {...defaultProps} />)

    await waitFor(() => {
      expect(screen.getByText('No Analysis Data Available')).toBeInTheDocument()
    })
  })

  it('should handle API errors gracefully', async () => {
    mockAnalyzeSkillCompensation.mockRejectedValue(new Error('API Error'))
    mockGetSkillProficiencyDistribution.mockRejectedValue(new Error('API Error'))

    render(<SkillAnalysisPanel {...defaultProps} />)

    await waitFor(() => {
      expect(screen.getByText('No Analysis Data Available')).toBeInTheDocument()
    })
  })

  it('should call APIs with correct skill name', async () => {
    mockAnalyzeSkillCompensation.mockResolvedValue(undefined as any)
    mockGetSkillProficiencyDistribution.mockResolvedValue(undefined as any)

    render(<SkillAnalysisPanel {...defaultProps} skillName="python" />)

    await waitFor(() => {
      expect(mockAnalyzeSkillCompensation).toHaveBeenCalledWith('python')
      expect(mockGetSkillProficiencyDistribution).toHaveBeenCalledWith('python')
    })
  })

it('should not show user proficiency when not provided', async () => {
    mockAnalyzeSkillCompensation.mockResolvedValue(undefined as any)
    mockGetSkillProficiencyDistribution.mockResolvedValue(undefined as any)

    render(<SkillAnalysisPanel {...defaultProps} userProficiency={null} />)

    await waitFor(() => {
      expect(screen.getByText('Skill Analysis: javascript')).toBeInTheDocument()
    })

    expect(screen.queryByText('Your level:')).not.toBeInTheDocument()
  })
})
