import { render, screen, fireEvent } from '@testing-library/react'
import SearchResultList from '@/features/search/SearchResultList'
import type { PersonResult } from '@/types'

describe('SearchResultList Component', () => {
  const mockOnSelectPerson = jest.fn()

  beforeEach(() => {
    mockOnSelectPerson.mockClear()
  })

  const mockResults: PersonResult[] = [
    {
      id: '1',
      username: 'johndoe',
      name: 'John Doe',
      professionalHeadline: 'Software Engineer',
      picture: 'https://example.com/john.jpg',
    },
    {
      id: '2',
      username: 'janesmith',
      name: 'Jane Smith',
      professionalHeadline: 'Product Manager',
      picture: null,
    },
  ]

  it('should render no results message when list is empty', () => {
    render(<SearchResultList results={[]} onSelectPerson={mockOnSelectPerson} />)
    expect(screen.getByText(/No results found/)).toBeInTheDocument()
  })

  it('should render results grid with person cards', () => {
    render(<SearchResultList results={mockResults} onSelectPerson={mockOnSelectPerson} />)
    expect(screen.getByText('John Doe')).toBeInTheDocument()
    expect(screen.getByText('Jane Smith')).toBeInTheDocument()
  })

  it('should render professional headline', () => {
    render(<SearchResultList results={mockResults} onSelectPerson={mockOnSelectPerson} />)
    expect(screen.getByText('Software Engineer')).toBeInTheDocument()
    expect(screen.getByText('Product Manager')).toBeInTheDocument()
  })

  it('should call onSelectPerson when clicking a result', () => {
    render(<SearchResultList results={mockResults} onSelectPerson={mockOnSelectPerson} />)
    const buttons = screen.getAllByRole('button')
    fireEvent.click(buttons[0])
    expect(mockOnSelectPerson).toHaveBeenCalledWith('johndoe')
  })

  it('should show fallback avatar when picture is null', () => {
    render(<SearchResultList results={mockResults} onSelectPerson={mockOnSelectPerson} />)
    const fallbackIcons = document.querySelectorAll('.avatar-fallback')
    expect(fallbackIcons.length).toBeGreaterThan(0)
  })

  it('should handle result without headline', () => {
    const resultsNoHeadline: PersonResult[] = [
      {
        id: '3',
        username: 'testuser',
        name: 'Test User',
        professionalHeadline: null,
        picture: null,
      },
    ]
    render(<SearchResultList results={resultsNoHeadline} onSelectPerson={mockOnSelectPerson} />)
    expect(screen.getByText('Test User')).toBeInTheDocument()
  })
})
