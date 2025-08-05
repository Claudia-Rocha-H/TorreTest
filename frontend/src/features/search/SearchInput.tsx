import React from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSearch } from '@fortawesome/free-solid-svg-icons';

/**
 * Main search input field
 */
const SearchInput: React.FC = () => {
  return (
    <div className="flex justify-center">
      <div className="relative w-3/5 max-w-2xl">
        <FontAwesomeIcon 
          icon={faSearch} 
          className="absolute left-5 top-1/2 -translate-y-1/2 text-input-placeholder transition-colors duration-300 w-4 h-4" 
        />
        <input
          type="text"
          placeholder="Search people or organizations by name"
          className="w-full py-4 px-4 pl-12 bg-input-bg border border-border rounded-50 text-light-text text-base outline-none transition-colors duration-300 focus:border-primary placeholder:text-input-placeholder"
        />
      </div>
    </div>
  );
};

export default SearchInput;
