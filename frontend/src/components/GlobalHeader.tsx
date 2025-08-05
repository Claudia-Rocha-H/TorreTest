import React from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import {
  faPlus,
  faBriefcase,
  faSearch,
  faHeart,
  faCog,
  faComment,
  faBell,
  faQuestionCircle,
  faUserCircle,
} from '@fortawesome/free-solid-svg-icons';

/**
 * Main application header with navigation
 */
const GlobalHeader: React.FC = () => {
  return (
    <header className="flex justify-between items-center px-8 py-4 bg-dark-bg border-b border-border">
      {/* Logo */}
      <div className="flex items-center">
        <a href="#" className="flex items-center font-bold text-xl text-white-text">
          <span className="text-white-text">torre</span>
          <span className="text-primary">.ai</span>
        </a>
      </div>

      {/* Navigation */}
      <nav>
        <ul className="flex items-center gap-6">
          <li>
            <a href="#" className="flex flex-col items-center text-sm text-light-text hover:text-white-text transition-colors duration-300">
              <FontAwesomeIcon icon={faPlus} className="text-lg mb-1 w-5 h-5" />
              <span>Publish a job</span>
            </a>
          </li>
          <li>
            <a href="#" className="flex flex-col items-center text-sm text-light-text hover:text-white-text transition-colors duration-300">
              <FontAwesomeIcon icon={faBriefcase} className="text-lg mb-1 w-5 h-5" />
              <span>Your vacancies</span>
            </a>
          </li>
          <li>
            <a href="#" className="flex flex-col items-center text-sm text-light-text hover:text-white-text transition-colors duration-300">
              <FontAwesomeIcon icon={faSearch} className="text-lg mb-1 w-5 h-5" />
              <span>Search jobs</span>
            </a>
          </li>
          <li>
            <a href="#" className="flex flex-col items-center text-sm text-light-text hover:text-white-text transition-colors duration-300">
              <FontAwesomeIcon icon={faHeart} className="text-lg mb-1 w-5 h-5" />
              <span>Cultural match</span>
            </a>
          </li>
          <li>
            <a href="#" className="flex flex-col items-center text-sm text-light-text hover:text-white-text transition-colors duration-300">
              <FontAwesomeIcon icon={faCog} className="text-lg mb-1 w-5 h-5" />
              <span>Preferences</span>
            </a>
          </li>
          <li>
            <a href="#" className="flex flex-col items-center text-sm text-light-text hover:text-white-text transition-colors duration-300">
              <FontAwesomeIcon icon={faComment} className="text-lg mb-1 w-5 h-5" />
              <span>Messages</span>
            </a>
          </li>
          <li>
            <a href="#" className="flex flex-col items-center text-sm text-light-text hover:text-white-text transition-colors duration-300">
              <FontAwesomeIcon icon={faBell} className="text-lg mb-1 w-5 h-5" />
              <span>Notifications</span>
            </a>
          </li>
          <li>
            <a href="#" className="flex flex-col items-center text-sm text-light-text hover:text-white-text transition-colors duration-300">
              <FontAwesomeIcon icon={faQuestionCircle} className="text-lg mb-1 w-5 h-5" />
              <span>Help</span>
            </a>
          </li>
          <li>
            <a href="#" className="flex flex-col items-center text-sm text-light-text hover:text-white-text transition-colors duration-300">
              <FontAwesomeIcon icon={faUserCircle} className="text-lg mb-1 w-5 h-5" />
              <span>You</span>
            </a>
          </li>
        </ul>
      </nav>
    </header>
  );
};

export default GlobalHeader;
