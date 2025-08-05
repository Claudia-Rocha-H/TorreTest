import React from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faChevronLeft, faChevronRight } from '@fortawesome/free-solid-svg-icons';

/**
 * Props interface for the Pagination component.
 */
interface PaginationProps {
  /** Current active page number (1-based) */
  currentPage: number;
  
  /** Total number of results available */
  totalResults: number;
  
  /** Number of items displayed per page */
  pageSize: number;
  
  /** Callback function when user navigates to different page */
  onPageChange: (page: number) => void;
}

/**
 * Pagination component that replicates Torre.ai's pagination design.
 * 
 * Features:
 * - Displays result range (e.g., "1 - 20 resultados de aproximadamente 150")
 * - Previous/next navigation buttons with disabled states
 * - Current page indicator
 * - Responsive design matching Torre.ai's dark theme
 * - FontAwesome icons for navigation
 */
const Pagination: React.FC<PaginationProps> = ({ currentPage, totalResults, pageSize, onPageChange }) => {
  const totalPages = Math.ceil(totalResults / pageSize);
  const startResult = (currentPage - 1) * pageSize + 1;
  const endResult = Math.min(currentPage * pageSize, totalResults);

  // Don't render pagination if no results
  if (totalResults === 0) return null;

  return (
    <div className="flex items-center justify-between mt-6 px-4 py-3 bg-dark-card rounded-lg">
      <div className="text-light-text text-sm">
        {startResult} - {endResult} results of approximately {totalResults}
      </div>
      
      <div className="flex items-center space-x-2">
        <button
          onClick={() => onPageChange(currentPage - 1)}
          disabled={currentPage === 1}
          className="flex items-center justify-center w-8 h-8 rounded text-light-text hover:text-white hover:bg-dark-bg transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
        >
          <FontAwesomeIcon icon={faChevronLeft} size="sm" />
        </button>
        
        <span className="text-light-text text-sm">
          Page {currentPage} of {totalPages}
        </span>
        
        <button
          onClick={() => onPageChange(currentPage + 1)}
          disabled={currentPage === totalPages}
          className="flex items-center justify-center w-8 h-8 rounded text-light-text hover:text-white hover:bg-dark-bg transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
        >
          <FontAwesomeIcon icon={faChevronRight} size="sm" />
        </button>
      </div>
    </div>
  );
};

export default Pagination;
