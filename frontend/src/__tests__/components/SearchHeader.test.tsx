import { render, screen } from '@testing-library/react'
import SearchHeader from '@/features/search/SearchHeader'

describe('SearchHeader Component', () => {
  it('should render page title', () => {
    render(<SearchHeader />)
    expect(screen.getByText('Search')).toBeInTheDocument()
  })

  it('should render filter tabs', () => {
    render(<SearchHeader />)
    expect(screen.getByText('People by name')).toBeInTheDocument()
    expect(screen.getByText('Candidates by skill, etc')).toBeInTheDocument()
    expect(screen.getByText('Jobs')).toBeInTheDocument()
  })

  it('should have navigation element', () => {
    const { container } = render(<SearchHeader />)
    const nav = container.querySelector('nav')
    expect(nav).toBeInTheDocument()
  })

  it('should render filter buttons', () => {
    render(<SearchHeader />)
    const buttons = document.querySelectorAll('button')
    expect(buttons.length).toBe(3)
  })
})
