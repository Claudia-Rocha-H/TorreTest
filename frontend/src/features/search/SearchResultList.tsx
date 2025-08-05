import React, { useState } from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faUser } from '@fortawesome/free-solid-svg-icons';
import { PersonResult } from '../../lib/api';

/**
 * Props interface for the SearchResultList component.
 */
interface SearchResultListProps {
  /** Array of person results to display */
  results: PersonResult[];
  
  /** Callback when a person card is clicked */
  onSelectPerson: (personId: string) => void;
}

/**
 * Displays search results in a grid layout with Torre.ai-inspired design.
 * 
 * Features:
 * - Hexagonal profile images with gradient borders
 * - 3D shadow effects mimicking Torre.ai's card design
 * - Automatic fallback to user icon for missing profile images
 * - Responsive grid layout (1/2/3 columns based on screen size)
 * - Hover effects with smooth scaling transitions
 * - Professional image error handling
 */
const SearchResultList: React.FC<SearchResultListProps> = ({ results, onSelectPerson }) => {
  
  /**
   * Internal component for handling profile images with graceful fallbacks.
   * Automatically detects image loading failures and displays a professional
   * user icon in hexagonal format matching Torre.ai's design.
   */
  const ProfileImage: React.FC<{ src: string; alt: string; className: string }> = ({ src, alt, className }) => {
    const [imageError, setImageError] = useState(false);

    // Render fallback avatar for missing or failed images
    if (imageError || !src) {
      return (
        <div className="hexagon-avatar">
          <div className="avatar-fallback">
            <FontAwesomeIcon 
              icon={faUser} 
              className="text-gray-500" 
              size="lg"
            />
          </div>
        </div>
      );
    }

    // Render actual profile image with error handling
    return (
      <div className="hexagon-avatar">
        <img
          src={src}
          alt={alt}
          onError={() => setImageError(true)}
          onLoad={() => setImageError(false)}
        />
      </div>
    );
  };

  // Show empty state when no results found
  if (results.length === 0) {
    return (
      <div className="mt-8 p-6 bg-dark-card rounded-lg h-96 flex items-center justify-center text-light-text">
        <p>No results found. Try a different search query.</p>
      </div>
    );
  }

  // Render search results in responsive grid with Torre.ai-inspired styling
  return (
    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 mt-8">
      {results.map((person) => (
        <div
          key={person.id}
          className="glow-card cursor-pointer hover:scale-105 transition-transform duration-200"
          onClick={() => onSelectPerson(person.id)}
        >
          {/* Card content with hexagonal avatar and person details */}
          <div className="glow-card-content flex items-center space-x-4">
            {/* Hexagonal profile image with automatic fallback handling */}
            <ProfileImage
              src={person.picture || ''}
              alt={person.name}
              className=""
            />
            
            {/* Person information section */}
            <div>
              {/* Primary name display */}
              <h3 className="text-white-text text-lg font-semibold">{person.name}</h3>
              
              {/* Professional headline if available */}
              {person.professionalHeadline && (
                <p className="text-light-text text-sm">{person.professionalHeadline}</p>
              )}
            </div>
          </div>
        </div>
      ))}
    </div>
  );
};

export default SearchResultList;