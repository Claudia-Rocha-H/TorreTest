import {
  searchPeople,
  getPersonDetails,
  analyzeSkillCompensation,
  getSkillProficiencyDistribution,
} from '@/lib/api'

const BASE_URL = 'http://localhost:8080/api'

describe('API Service', () => {
  beforeEach(() => {
    jest.clearAllMocks()
  })

  describe('searchPeople', () => {
    it('should successfully search for people', async () => {
      const mockResponse = {
        results: [
          { id: '1', name: 'John Doe' },
          { id: '2', name: 'Jane Smith' },
        ],
        limit: 100,
      }

      global.fetch = jest.fn().mockResolvedValue({
        ok: true,
        json: jest.fn().mockResolvedValue(mockResponse),
      } as any)

      const result = await searchPeople('javascript developer', 100)

      expect(global.fetch).toHaveBeenCalledWith(`${BASE_URL}/search/people`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ query: 'javascript developer', limit: 100 }),
      })
      expect(result).toEqual(mockResponse)
    })

    it('should handle API errors', async () => {
      const mockErrorResponse = { message: 'Invalid query' }

      global.fetch = jest.fn().mockResolvedValue({
        ok: false,
        json: jest.fn().mockResolvedValue(mockErrorResponse),
      } as any)

      await expect(searchPeople('', 100)).rejects.toThrow('Invalid query')
    })

    it('should use default limit of 100', async () => {
      global.fetch = jest.fn().mockResolvedValue({
        ok: true,
        json: jest.fn().mockResolvedValue({ results: [] }),
      } as any)

      await searchPeople('test')

      expect(global.fetch).toHaveBeenCalledWith(expect.any(String), expect.objectContaining({
        body: JSON.stringify({ query: 'test', limit: 100 }),
      }))
    })
  })

  describe('getPersonDetails', () => {
    it('should successfully fetch person details', async () => {
      const mockResponse = {
        id: 'test-user',
        name: 'Test User',
        email: 'test@example.com',
      }

      global.fetch = jest.fn().mockResolvedValue({
        ok: true,
        json: jest.fn().mockResolvedValue(mockResponse),
      } as any)

      const result = await getPersonDetails('test-user')

      expect(global.fetch).toHaveBeenCalledWith(
        `${BASE_URL}/profile/test-user`,
        expect.objectContaining({ method: 'GET' })
      )
      expect(result).toEqual(mockResponse)
    })

    it('should handle 404 not found errors', async () => {
      global.fetch = jest.fn().mockResolvedValue({
        ok: false,
        status: 404,
        statusText: 'Not Found',
      } as any)

      await expect(getPersonDetails('nonexistent')).rejects.toThrow(
        'Profile not found for username: nonexistent'
      )
    })

    it('should encode username in URL', async () => {
      global.fetch = jest.fn().mockResolvedValue({
        ok: true,
        json: jest.fn().mockResolvedValue({}),
      } as any)

      await getPersonDetails('user@domain.com')

      expect(global.fetch).toHaveBeenCalledWith(
        expect.stringContaining(encodeURIComponent('user@domain.com')),
        expect.any(Object)
      )
    })
  })

  describe('analyzeSkillCompensation', () => {
    it('should successfully fetch skill compensation analysis', async () => {
      const mockResponse = {
        skill: 'javascript',
        averageCompensation: 85000,
        currency: 'USD',
      }

      global.fetch = jest.fn().mockResolvedValue({
        ok: true,
        json: jest.fn().mockResolvedValue(mockResponse),
      } as any)

      const result = await analyzeSkillCompensation('javascript')

      expect(global.fetch).toHaveBeenCalledWith(
        expect.stringContaining(`/analyze/skill-compensation?skill=javascript`),
        expect.any(Object)
      )
      expect(result).toEqual(mockResponse)
    })

    it('should handle analysis errors', async () => {
      global.fetch = jest.fn().mockResolvedValue({
        ok: false,
        status: 500,
      } as any)

      await expect(analyzeSkillCompensation('invalid')).rejects.toThrow(
        'Failed to analyze skill compensation: 500'
      )
    })

    it('should encode skill name in URL', async () => {
      global.fetch = jest.fn().mockResolvedValue({
        ok: true,
        json: jest.fn().mockResolvedValue({}),
      } as any)

      await analyzeSkillCompensation('c++')

      expect(global.fetch).toHaveBeenCalledWith(
        expect.stringContaining(encodeURIComponent('c++')),
        expect.any(Object)
      )
    })
  })

  describe('getSkillProficiencyDistribution', () => {
    it('should successfully fetch skill distribution', async () => {
      const mockResponse = {
        skill: 'python',
        distribution: [
          { level: 'beginner', percentage: 20 },
          { level: 'advanced', percentage: 80 },
        ],
      }

      global.fetch = jest.fn().mockResolvedValue({
        ok: true,
        json: jest.fn().mockResolvedValue(mockResponse),
      } as any)

      const result = await getSkillProficiencyDistribution('python')

      expect(global.fetch).toHaveBeenCalledWith(
        expect.stringContaining(`/analyze/skill-distribution?skill=python`),
        expect.any(Object)
      )
      expect(result).toEqual(mockResponse)
    })

    it('should handle distribution errors', async () => {
      global.fetch = jest.fn().mockResolvedValue({
        ok: false,
        status: 503,
      } as any)

      await expect(getSkillProficiencyDistribution('unavailable')).rejects.toThrow(
        'Failed to get skill distribution: 503'
      )
    })
  })
})
