import { render, screen, waitFor } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import Home from '@/app/page'
import * as api from '@/lib/api'

jest.mock('@/lib/api')
jest.mock('next/navigation', () => ({
  useRouter: () => ({ push: jest.fn() }),
}))

const mockSearchPeople = api.searchPeople as jest.MockedFunction<typeof api.searchPeople>

describe('Home Page', () => {
  beforeEach(() => {
    jest.clearAllMocks()
  })

  const mockSearchResults = {
    results: [
      {
        id: '1',
        username: 'johndoe',
        name: 'John Doe',
        professionalHeadline: 'Engineer',
        picture: null,
      },
    ],
  }

  it('should render search components', () => {
    render(<Home />)
    expect(screen.getByPlaceholderText(/Search people or organizations/)).toBeInTheDocument()
  })

  it('should render search header', () => {
    render(<Home />)
    expect(screen.getByText('Search')).toBeInTheDocument()
  })

  it('should render empty results message initially', () => {
    render(<Home />)
    expect(screen.getByText(/Your search results will appear here/)).toBeInTheDocument()
  })

  it('should perform search and display results', async () => {
    const user = userEvent.setup()
    mockSearchPeople.mockResolvedValue(mockSearchResults as any)
    render(<Home />)

    const input = screen.getByPlaceholderText(/Search people or organizations by name/) as HTMLInputElement
    await user.type(input, 'javascript{enter}')

    await waitFor(() => {
      expect(screen.getByText('John Doe')).toBeInTheDocument()
    })
  })

  it('should handle search error', async () => {
    const user = userEvent.setup()
    mockSearchPeople.mockRejectedValue(new Error('Search failed'))
    render(<Home />)

    const input = screen.getByPlaceholderText(/Search people or organizations by name/) as HTMLInputElement
    await user.type(input, 'test{enter}')

    await waitFor(() => {
      expect(screen.getByText(/Failed to fetch search results/)).toBeInTheDocument()
    })
  })

  it('should show loading state', async () => {
    const user = userEvent.setup()
    mockSearchPeople.mockImplementation(() => new Promise(() => {}))
    render(<Home />)

    const input = screen.getByPlaceholderText(/Search people or organizations by name/) as HTMLInputElement
    await user.type(input, 'test{enter}')

    expect(screen.getByText(/Searching/)).toBeInTheDocument()
  })
})
