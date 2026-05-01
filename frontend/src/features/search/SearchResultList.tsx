import Image from 'next/image';
import { useState } from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faUser } from '@fortawesome/free-solid-svg-icons';
import type { PersonResult } from '../../types';

interface SearchResultListProps {
  results: PersonResult[];
  onSelectPerson: (username: string) => void;
}

/**
 * Search results grid with Torre.ai-inspired card design
 */
export default function SearchResultList({ results, onSelectPerson }: SearchResultListProps) {
  
  const ProfileImage = ({ src, alt }: { src: string; alt: string }) => {
    const [imageError, setImageError] = useState(false);

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

    return (
      <div className="hexagon-avatar">
        <Image
          src={src}
          alt={alt}
          width={64}
          height={64}
          className="h-[60px] w-[60px]"
          unoptimized
          onError={() => setImageError(true)}
          onLoad={() => setImageError(false)}
        />
      </div>
    );
  };

  if (results.length === 0) {
    return (
      <div className="mt-8 p-6 bg-dark-card rounded-lg h-96 flex items-center justify-center text-light-text">
        <p>No results found. Try a different search query.</p>
      </div>
    );
  }

  return (
    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 mt-8">
      {results.map((person) => (
        <button
          type="button"
          key={person.id}
          className="glow-card min-h-[120px] w-full cursor-pointer text-left transition-transform duration-200 hover:scale-105"
          onClick={() => onSelectPerson(person.username)}
        >
          <div className="glow-card-content flex items-center space-x-4 h-full">
            <div className="flex-shrink-0">
              <ProfileImage
                src={person.picture || ''}
                alt={person.name}
              />
            </div>
            
            <div className="flex-grow min-w-0">
              <h3 className="text-white-text text-lg font-semibold truncate">{person.name}</h3>
              
              {person.professionalHeadline && (
                <p className="text-light-text text-sm line-clamp-2">{person.professionalHeadline}</p>
              )}
            </div>
          </div>
        </button>
      ))}
    </div>
  );
}
