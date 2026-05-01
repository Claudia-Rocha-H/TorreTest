import { render, screen } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import SearchInput from '@/features/search/SearchInput'

describe('SearchInput Component', () => {
  it('should render search input with placeholder', () => {
    const mockOnSearch = jest.fn()
    render(<SearchInput onSearch={mockOnSearch} />)

    const input = screen.getByPlaceholderText('Search people or organizations by name')
    expect(input).toBeInTheDocument()
  })

it('should display error when submitting empty query', async () => {
    const mockOnSearch = jest.fn()
    const user = userEvent.setup()
    render(<SearchInput onSearch={mockOnSearch} />)

    const input = screen.getByPlaceholderText('Search people or organizations by name') as HTMLInputElement
    // Submit form with empty input by pressing Enter
    await user.type(input, '{enter}')

    // Wait for error message to appear
    await screen.findByText('Search term cannot be empty.')
    expect(mockOnSearch).not.toHaveBeenCalled()
  })

it('should call onSearch when submitting valid query', async () => {
    const mockOnSearch = jest.fn()
    const user = userEvent.setup()
    render(<SearchInput onSearch={mockOnSearch} />)

    const input = screen.getByPlaceholderText('Search people or organizations by name') as HTMLInputElement
    await user.type(input, 'javascript developer{enter}')

    expect(mockOnSearch).toHaveBeenCalledWith('javascript developer')
  })

  it('should allow text input', async () => {
    const mockOnSearch = jest.fn()
    const user = userEvent.setup()
    render(<SearchInput onSearch={mockOnSearch} />)

    const input = screen.getByPlaceholderText('Search people or organizations by name') as HTMLInputElement
    await user.type(input, 'test query')

    expect(input.value).toBe('test query')
  })

  it('should have search icon', () => {
    const mockOnSearch = jest.fn()
    const { container } = render(<SearchInput onSearch={mockOnSearch} />)

    const icon = container.querySelector('svg[data-icon="magnifying-glass"]')
    expect(icon).toBeInTheDocument()
  })

it('should handle special characters in query', async () => {
    const mockOnSearch = jest.fn()
    const user = userEvent.setup()
    render(<SearchInput onSearch={mockOnSearch} />)

    const input = screen.getByPlaceholderText('Search people or organizations by name') as HTMLInputElement
    await user.type(input, 'c++ developer & architect{enter}')

    expect(mockOnSearch).toHaveBeenCalledWith('c++ developer & architect')
  })

  it('should clear error message when user starts typing', async () => {
    const mockOnSearch = jest.fn()
    const user = userEvent.setup()
    render(<SearchInput onSearch={mockOnSearch} />)

    const input = screen.getByPlaceholderText('Search people or organizations by name') as HTMLInputElement
    // Submit with empty to trigger error
    await user.type(input, '{enter}')

    await screen.findByText('Search term cannot be empty.')

    // Type to clear error
    await user.type(input, 'a')

    // Verify error is cleared (element should not exist)
    expect(screen.queryByText('Search term cannot be empty.')).not.toBeInTheDocument()
  })

  it('should have proper accessibility attributes', () => {
    const mockOnSearch = jest.fn()
    render(<SearchInput onSearch={mockOnSearch} />)

    const input = screen.getByPlaceholderText('Search people or organizations by name')
    expect(input).toHaveAttribute('type', 'text')
    expect(input).toHaveAttribute('autoComplete', 'off')
    expect(input).toHaveAttribute('spellCheck', 'false')
  })
})
