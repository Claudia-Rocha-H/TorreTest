import { render } from '@testing-library/react';
import RootLayout from '@/app/layout';

describe('RootLayout', () => {
  it('should render children content', () => {
    const { getByText } = render(
      <RootLayout>
        <main>Test Content</main>
      </RootLayout>
    );
    expect(getByText('Test Content')).toBeInTheDocument();
  });

  it('should render with main element', () => {
    const { container } = render(
      <RootLayout>
        <main>Content</main>
      </RootLayout>
    );
    expect(container.querySelector('main')).toBeInTheDocument();
  });
});
