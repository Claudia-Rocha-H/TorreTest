import React from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faUser, faStar, faFileAlt } from '@fortawesome/free-solid-svg-icons';

/**
 * Search header with filter tabs
 */
const SearchHeader: React.FC = () => {
  return (
    <div className="bg-dark-bg shadow-lg py-5 px-16 relative z-10 grid grid-cols-3 items-center">
      {/* Page title */}
      <h2 className="text-xl font-semibold text-white-text justify-self-start">Search</h2>

      {/* Filter tabs */}
      <nav className="flex justify-center col-span-1">
        <div className="flex gap-3">
          <a href="#" className="flex items-center gap-2 px-5 py-2 bg-active-tab rounded-20 text-white-text font-medium transition-all duration-300">
            <FontAwesomeIcon icon={faUser} className="text-sm w-4 h-4" />
            <span>People by name</span>
          </a>
          <a href="#" className="flex items-center gap-2 px-5 py-2 bg-dark-bg rounded-20 text-primary font-medium transition-all duration-300 hover:bg-active-tab">
            <FontAwesomeIcon icon={faStar} className="text-sm w-4 h-4" />
            <span>Candidates by skill, etc</span>
          </a>
          <a href="#" className="flex items-center gap-2 px-5 py-2 bg-dark-bg rounded-20 text-primary font-medium transition-all duration-300 hover:bg-active-tab">
            <FontAwesomeIcon icon={faFileAlt} className="text-sm w-4 h-4" />
            <span>Jobs</span>
          </a>
        </div>
      </nav>

      <div></div>
    </div>
  );
};

export default SearchHeader;