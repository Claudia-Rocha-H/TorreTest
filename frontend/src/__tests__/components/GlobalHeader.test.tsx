import { render, screen } from '@testing-library/react'
import GlobalHeader from '@/components/GlobalHeader'

describe('GlobalHeader Component', () => {
  it('should render header', () => {
    const { container } = render(<GlobalHeader />)
    const header = container.querySelector('header')
    expect(header).toBeInTheDocument()
  })

  it('should render logo with brand name', () => {
    render(<GlobalHeader />)
    const logoText = screen.getByText('torre')
    expect(logoText).toBeInTheDocument()
  })

  it('should render all navigation menu items', () => {
    render(<GlobalHeader />)
    expect(screen.getByText('Publish a job')).toBeInTheDocument()
    expect(screen.getByText('Your vacancies')).toBeInTheDocument()
    expect(screen.getByText('Search jobs')).toBeInTheDocument()
    expect(screen.getByText('Cultural match')).toBeInTheDocument()
    expect(screen.getByText('Preferences')).toBeInTheDocument()
    expect(screen.getByText('Messages')).toBeInTheDocument()
    expect(screen.getByText('Notifications')).toBeInTheDocument()
    expect(screen.getByText('Help')).toBeInTheDocument()
    expect(screen.getByText('You')).toBeInTheDocument()
  })

  it('should have navigation buttons', () => {
    render(<GlobalHeader />)
    const buttons = screen.getAllByRole('button')
    expect(buttons.length).toBeGreaterThanOrEqual(9)
  })

it('should have home link', () => {
    render(<GlobalHeader />)
    const logoLink = screen.getByRole('link', { name: /torre/i })
    expect(logoLink).toBeInTheDocument()
  })

  it('should render navigation as list', () => {
    const { container } = render(<GlobalHeader />)
    const navList = container.querySelector('nav ul')
    expect(navList).toBeInTheDocument()
  })

  it('should have proper header styling classes', () => {
    const { container } = render(<GlobalHeader />)
    const header = container.querySelector('header')
    expect(header).toHaveClass('bg-dark-bg')
    expect(header).toHaveClass('border-b')
  })
})
