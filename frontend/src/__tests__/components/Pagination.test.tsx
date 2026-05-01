import { render, screen, fireEvent } from '@testing-library/react'
import Pagination from '@/components/Pagination'

describe('Pagination Component', () => {
  const mockOnPageChange = jest.fn()

  beforeEach(() => {
    mockOnPageChange.mockClear()
  })

  it('should not render when totalResults is 0', () => {
    const { container } = render(
      <Pagination
        currentPage={1}
        totalResults={0}
        pageSize={20}
        onPageChange={mockOnPageChange}
      />
    )
    expect(container.firstChild).toBeNull()
  })

  it('should display result range correctly', () => {
    render(
      <Pagination
        currentPage={1}
        totalResults={150}
        pageSize={20}
        onPageChange={mockOnPageChange}
      />
    )
    expect(screen.getByText(/1 - 20 results of approximately 150/)).toBeInTheDocument()
  })

  it('should display correct range on second page', () => {
    render(
      <Pagination
        currentPage={2}
        totalResults={150}
        pageSize={20}
        onPageChange={mockOnPageChange}
      />
    )
    expect(screen.getByText(/21 - 40 results of approximately 150/)).toBeInTheDocument()
  })

  it('should show correct page indicator', () => {
    render(
      <Pagination
        currentPage={3}
        totalResults={150}
        pageSize={20}
        onPageChange={mockOnPageChange}
      />
    )
    expect(screen.getByText('Page 3 of 8')).toBeInTheDocument()
  })

  it('should disable previous button on first page', () => {
    render(
      <Pagination
        currentPage={1}
        totalResults={150}
        pageSize={20}
        onPageChange={mockOnPageChange}
      />
    )
    const prevButton = screen.getAllByRole('button')[0]
    expect(prevButton).toBeDisabled()
  })

  it('should disable next button on last page', () => {
    render(
      <Pagination
        currentPage={8}
        totalResults={150}
        pageSize={20}
        onPageChange={mockOnPageChange}
      />
    )
    const nextButton = screen.getAllByRole('button')[1]
    expect(nextButton).toBeDisabled()
  })

  it('should call onPageChange when clicking next button', () => {
    render(
      <Pagination
        currentPage={2}
        totalResults={150}
        pageSize={20}
        onPageChange={mockOnPageChange}
      />
    )
    const nextButton = screen.getAllByRole('button')[1]
    fireEvent.click(nextButton)
    expect(mockOnPageChange).toHaveBeenCalledWith(3)
  })

  it('should call onPageChange when clicking previous button', () => {
    render(
      <Pagination
        currentPage={3}
        totalResults={150}
        pageSize={20}
        onPageChange={mockOnPageChange}
      />
    )
    const prevButton = screen.getAllByRole('button')[0]
    fireEvent.click(prevButton)
    expect(mockOnPageChange).toHaveBeenCalledWith(2)
  })

  it('should handle last page with partial results', () => {
    render(
      <Pagination
        currentPage={8}
        totalResults={150}
        pageSize={20}
        onPageChange={mockOnPageChange}
      />
    )
    expect(screen.getByText(/141 - 150 results of approximately 150/)).toBeInTheDocument()
  })

  it('should calculate total pages correctly', () => {
    render(
      <Pagination
        currentPage={1}
        totalResults={100}
        pageSize={30}
        onPageChange={mockOnPageChange}
      />
    )
    expect(screen.getByText('Page 1 of 4')).toBeInTheDocument()
  })

  it('should have navigation buttons', () => {
    render(
      <Pagination
        currentPage={2}
        totalResults={150}
        pageSize={20}
        onPageChange={mockOnPageChange}
      />
    )
    const buttons = screen.getAllByRole('button')
    expect(buttons).toHaveLength(2)
  })
})
