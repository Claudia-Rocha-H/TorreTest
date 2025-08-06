'use client';

import React, { useState, useEffect } from 'react';
import { useParams, useRouter } from 'next/navigation';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { 
  faUser, 
  faMapMarkerAlt, 
  faArrowLeft,
  faBriefcase,
  faGraduationCap,
  faStar
} from '@fortawesome/free-solid-svg-icons';
import GlobalHeader from '../../../components/GlobalHeader';
import SkillAnalysisPanel from '../../../components/SkillAnalysisPanel';
import type { PersonDetailsResponse, Experience, Education, Organization } from '../../../types';
import { getPersonDetails } from '../../../lib/api';

/**
 * Dynamic profile page component for Torre.ai user profiles.
 * Displays comprehensive profile information with skills, experience, and education.
 */
export default function ProfilePage() {
  const params = useParams();
  const router = useRouter();
  const username = params.username as string;
  
  const [profileData, setProfileData] = useState<PersonDetailsResponse | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [imageError, setImageError] = useState(false);
  const [showAllSkills, setShowAllSkills] = useState(false);
  const [selectedSkill, setSelectedSkill] = useState<string | null>(null);

  /**
   * Load profile data on component mount or username change
   */
  useEffect(() => {
    const loadProfile = async () => {
      if (!username) {
        setError('Username not provided');
        setLoading(false);
        return;
      }

      setLoading(true);
      setError(null);

      try {
        const data = await getPersonDetails(username);
        setProfileData(data);
      } catch (err) {
        console.error('Failed to load profile:', err);
        setError(err instanceof Error ? err.message : 'Failed to load profile');
      } finally {
        setLoading(false);
      }
    };

    loadProfile();
  }, [username]);

  const handleGoBack = () => {
    router.back();
  };

  const formatDateRange = (fromMonth: string | null, fromYear: string | null, toMonth: string | null, toYear: string | null) => {
    const from = fromYear ? `${fromMonth || ''} ${fromYear}`.trim() : '';
    const to = toYear ? `${toMonth || ''} ${toYear}`.trim() : 'Present';
    
    if (!from) return to === 'Present' ? 'Present' : to;
    return `${from} - ${to}`;
  };

  const getSkillColor = (proficiency: string | null) => {
    switch (proficiency?.toLowerCase()) {
      case 'expert':
      case 'master':
        return 'bg-green-500/20 text-green-400 border-green-500/30';
      case 'proficient':
      case 'advanced':
        return 'bg-blue-500/20 text-blue-400 border-blue-500/30';
      case 'novice':
      case 'beginner':
        return 'bg-yellow-500/20 text-yellow-400 border-yellow-500/30';
      default:
        return 'bg-primary/20 text-primary border-primary/30';
    }
  };

  const getSkillColorCompact = (proficiency: string | null | undefined) => {
    switch (proficiency?.toLowerCase()) {
      case 'expert':
      case 'master':
        return 'bg-green-500/30 text-green-300 border border-green-500/40';
      case 'proficient':
      case 'advanced':
        return 'bg-blue-500/30 text-blue-300 border border-blue-500/40';
      case 'novice':
      case 'beginner':
        return 'bg-yellow-500/30 text-yellow-300 border border-yellow-500/40';
      default:
        return 'bg-primary/30 text-primary border border-primary/40';
    }
  };

  const getSkillLevel = (proficiency: string | null | undefined) => {
    switch (proficiency?.toLowerCase()) {
      case 'expert':
      case 'master':
        return '★★★';
      case 'proficient':
      case 'advanced':
        return '★★';
      case 'novice':
      case 'beginner':
        return '★';
      case 'no-experience-interested':
        return '☆';
      default:
        return '';
    }
  };

  const getSkillStarCount = (proficiency: string | null | undefined) => {
    switch (proficiency?.toLowerCase()) {
      case 'expert':
      case 'master':
        return 3;
      case 'proficient':
      case 'advanced':
        return 2;
      case 'novice':
      case 'beginner':
        return 1;
      case 'no-experience-interested':
        return 0.5;
      default:
        return 0;
    }
  };

  /**
   * Sort skills by proficiency level and apply pagination
   */
  const getSortedSkills = () => {
    if (!profileData?.strengths) return [];
    
    const sorted = [...profileData.strengths].sort((a, b) => {
      const aStars = getSkillStarCount(a.proficiency);
      const bStars = getSkillStarCount(b.proficiency);
      return bStars - aStars;
    });

  return showAllSkills ? sorted : sorted.slice(0, 20);
  };

  /**
   * Handle skill click to show analysis panel
   */
  const handleSkillClick = (skillName: string) => {
    setSelectedSkill(skillName);
  };

  /**
   * Handle closing the skill analysis panel
   */
  const handleCloseSkillAnalysis = () => {
    setSelectedSkill(null);
  };

  /**
   * Get user proficiency for selected skill
   */
  const getSelectedSkillProficiency = (): string | null => {
    if (!selectedSkill || !profileData?.strengths) return null;
    const skill = profileData.strengths.find(s => s.name === selectedSkill);
    return skill?.proficiency || null;
  };

  if (loading) {
    return (
      <div className="min-h-screen bg-dark-bg text-light-text">
        <GlobalHeader />
        <main className="px-16 py-10">
          <div className="flex items-center justify-center h-96">
            <div className="text-center">
              <div className="flex justify-center mb-4">
                <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary"></div>
              </div>
              <p>Loading profile...</p>
            </div>
          </div>
        </main>
      </div>
    );
  }

  if (error) {
    return (
      <div className="min-h-screen bg-dark-bg text-light-text">
        <GlobalHeader />
        <main className="px-16 py-10">
          <button
            onClick={handleGoBack}
            className="flex items-center text-primary hover:text-primary/80 mb-6 transition-colors"
          >
            <FontAwesomeIcon icon={faArrowLeft} className="mr-2" />
            Back to Search
          </button>
          
          <div className="bg-red-500/20 border border-red-500/30 rounded-lg p-6 text-center">
            <h2 className="text-xl font-semibold text-red-400 mb-2">Profile Not Found</h2>
            <p className="text-red-300">{error}</p>
          </div>
        </main>
      </div>
    );
  }

  if (!profileData) {
    return (
      <div className="min-h-screen bg-dark-bg text-light-text">
        <GlobalHeader />
        <main className="px-16 py-10">
          <p>No profile data available</p>
        </main>
      </div>
    );
  }

  const { person, strengths, experiences, education } = profileData;

  return (
    <div className="min-h-screen bg-dark-bg text-light-text font-sans">
      <GlobalHeader />
      
      <main className="px-16 py-10">
        <button
          onClick={handleGoBack}
          className="flex items-center text-primary hover:text-primary/80 mb-8 transition-colors"
        >
          <FontAwesomeIcon icon={faArrowLeft} className="mr-2" />
          Back to Search
        </button>

        <div className="bg-dark-card rounded-lg p-8 mb-8">
          <div className="flex items-center space-x-8">
            <div className="flex-shrink-0 flex items-center">
              {imageError || !person.picture ? (
                <div className="hexagon-avatar hexagon-profile">
                  <div className="avatar-fallback">
                    <FontAwesomeIcon icon={faUser} className="text-gray-500" size="4x" />
                  </div>
                </div>
              ) : (
                <div className="hexagon-avatar hexagon-profile">
                  <img
                    src={person.picture}
                    alt={`${person.name}'s profile`}
                    onError={() => setImageError(true)}
                    onLoad={() => setImageError(false)}
                  />
                </div>
              )}
            </div>

            <div className="flex-1">
              <h1 className="text-3xl font-bold text-white-text mb-2">{person.name}</h1>
              
              {person.professionalHeadline && (
                <p className="text-xl text-primary mb-4">{person.professionalHeadline}</p>
              )}
              
              {person.location && (
                <div className="flex items-center text-light-text mb-4">
                  <FontAwesomeIcon icon={faMapMarkerAlt} className="mr-2 text-primary" />
                  <span>{person.location.name}</span>
                </div>
              )}
              
              {person.summaryOfBio && (
                <div className="text-light-text leading-relaxed">
                  <p>{person.summaryOfBio}</p>
                </div>
              )}
            </div>
          </div>
        </div>

        {strengths && strengths.length > 0 && (
          <div className="bg-dark-card rounded-lg p-8 mb-8">
            <h2 className="text-2xl font-semibold text-white-text mb-6 flex items-center">
              <FontAwesomeIcon icon={faStar} className="mr-3 text-primary" />
              Skills & Strengths ({strengths.length})
            </h2>
            
            <div className="flex flex-wrap gap-2 mb-4">
              {getSortedSkills().map((skill, index) => (
                <button
                  key={skill.id || index}
                  onClick={() => handleSkillClick(skill.name)}
                  className={`inline-flex items-center px-3 py-1 rounded-full text-sm font-medium ${getSkillColorCompact(skill.proficiency)} transition-all hover:scale-105 cursor-pointer hover:shadow-lg`}
                  title={`Click to analyze ${skill.name}${skill.proficiency ? ` - ${skill.proficiency}` : ''}`}
                >
                  {skill.name}
                  {skill.proficiency && (
                    <span className="ml-1 text-xs opacity-75">
                      {getSkillLevel(skill.proficiency)}
                    </span>
                  )}
                </button>
              ))}
            </div>

            {strengths.length > 20 && (
              <button
                onClick={() => setShowAllSkills(!showAllSkills)}
                className="text-primary hover:text-green-400 text-sm font-medium transition-colors flex items-center bg-primary/10 hover:bg-primary/20 px-3 py-2 rounded-lg border border-primary/30 hover:border-green-400/50"
              >
                {showAllSkills ? (
                  <>Show less...</>
                ) : (
                  <>Show more... ({strengths.length - 20} additional skills)</>
                )}
              </button>
            )}
          </div>
        )}

        {experiences && experiences.length > 0 && (
          <div className="bg-dark-card rounded-lg p-6 mb-6">
            <h2 className="text-xl font-semibold text-white-text mb-4 flex items-center">
              <FontAwesomeIcon icon={faBriefcase} className="mr-2 text-primary" />
              Professional Experience
            </h2>
            
            <div className="space-y-4">
              {experiences.map((exp: Experience, index: number) => (
                <div key={exp.id || index} className="border-l-2 border-primary/30 pl-4 pb-4">
                  <h3 className="text-lg font-semibold text-primary mb-1">{exp.name}</h3>
                  
                  {exp.organizations && exp.organizations.length > 0 && (
                    <div className="text-primary font-medium mb-1 text-sm">
                      {exp.organizations.map((org: Organization) => org.name).join(', ')}
                    </div>
                  )}
                  
                  <div className="text-light-text text-xs">
                    {formatDateRange(exp.fromMonth, exp.fromYear, exp.toMonth, exp.toYear)}
                  </div>
                </div>
              ))}
            </div>
          </div>
        )}

        {education && education.length > 0 && (
          <div className="bg-dark-card rounded-lg p-6">
            <h2 className="text-xl font-semibold text-white-text mb-4 flex items-center">
              <FontAwesomeIcon icon={faGraduationCap} className="mr-2 text-primary" />
              Education
            </h2>
            
            <div className="space-y-4">
              {education.map((edu: Education, index: number) => (
                <div key={edu.id || index} className="border-l-2 border-primary/30 pl-4 pb-4">
                  <h3 className="text-lg font-semibold text-primary mb-1">{edu.name}</h3>
                  
                  {edu.organizations && edu.organizations.length > 0 && (
                    <div className="text-primary font-medium mb-1 text-sm">
                      {edu.organizations.map((org: Organization) => org.name).join(', ')}
                    </div>
                  )}
                  
                  <div className="text-light-text text-xs">
                    {formatDateRange(edu.fromMonth, edu.fromYear, edu.toMonth, edu.toYear)}
                  </div>
                </div>
              ))}
            </div>
          </div>
        )}
      </main>

      {/* Skill Analysis Panel */}
      {selectedSkill && (
        <SkillAnalysisPanel
          skillName={selectedSkill}
          userProficiency={getSelectedSkillProficiency()}
          onClose={handleCloseSkillAnalysis}
          isVisible={true}
        />
      )}
    </div>
  );
}