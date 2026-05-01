'use client';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import {
  faArrowLeft,
  faBriefcase,
  faGraduationCap,
  faMapMarkerAlt,
  faStar,
  faUser,
} from '@fortawesome/free-solid-svg-icons';
import Image from 'next/image';
import { useParams, useRouter } from 'next/navigation';
import { useEffect, useState } from 'react';
import GlobalHeader from '../../../components/GlobalHeader';
import SkillAnalysisPanel from '../../../components/SkillAnalysisPanel';
import { getPersonDetails } from '../../../lib/api';
import type {
  Education,
  Experience,
  Organization,
  PersonDetailsResponse,
} from '../../../types';

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

    void loadProfile();
  }, [username]);

  const handleGoBack = () => {
    router.back();
  };

  const formatDateRange = (
    fromMonth: string | null,
    fromYear: string | null,
    toMonth: string | null,
    toYear: string | null
  ) => {
    const from = fromYear ? `${fromMonth || ''} ${fromYear}`.trim() : '';
    const to = toYear ? `${toMonth || ''} ${toYear}`.trim() : 'Present';

    if (!from) {
      return to;
    }

    return `${from} - ${to}`;
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
        return '***';
      case 'proficient':
      case 'advanced':
        return '**';
      case 'novice':
      case 'beginner':
        return '*';
      case 'no-experience-interested':
        return 'o';
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

  const getSortedSkills = () => {
    if (!profileData?.strengths) {
      return [];
    }

    const sorted = [...profileData.strengths].sort((a, b) => {
      const aStars = getSkillStarCount(a.proficiency);
      const bStars = getSkillStarCount(b.proficiency);
      return bStars - aStars;
    });

    return showAllSkills ? sorted : sorted.slice(0, 20);
  };

  const handleSkillClick = (skillName: string) => {
    setSelectedSkill(skillName);
  };

  const handleCloseSkillAnalysis = () => {
    setSelectedSkill(null);
  };

  const getSelectedSkillProficiency = (): string | null => {
    if (!selectedSkill || !profileData?.strengths) {
      return null;
    }

    const skill = profileData.strengths.find((item) => item.name === selectedSkill);
    return skill?.proficiency || null;
  };

  if (loading) {
    return (
      <div className="min-h-screen bg-dark-bg text-light-text">
        <GlobalHeader />
        <main className="px-16 py-10">
          <div className="flex h-96 items-center justify-center">
            <div className="text-center">
              <div className="mb-4 flex justify-center">
                <div className="h-12 w-12 animate-spin rounded-full border-b-2 border-primary" />
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
            type="button"
            onClick={handleGoBack}
            className="mb-6 flex items-center text-primary transition-colors hover:text-primary/80"
          >
            <FontAwesomeIcon icon={faArrowLeft} className="mr-2" />
            Back to Search
          </button>

          <div className="rounded-lg border border-red-500/30 bg-red-500/20 p-6 text-center">
            <h2 className="mb-2 text-xl font-semibold text-red-400">Profile Not Found</h2>
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
    <div className="min-h-screen bg-dark-bg font-sans text-light-text">
      <GlobalHeader />

      <main className="px-16 py-10">
        <button
          type="button"
          onClick={handleGoBack}
          className="mb-8 flex items-center text-primary transition-colors hover:text-primary/80"
        >
          <FontAwesomeIcon icon={faArrowLeft} className="mr-2" />
          Back to Search
        </button>

        <div className="mb-8 rounded-lg bg-dark-card p-8">
          <div className="flex items-center space-x-8">
            <div className="flex flex-shrink-0 items-center">
              {imageError || !person.picture ? (
                <div className="hexagon-avatar hexagon-profile">
                  <div className="avatar-fallback">
                    <FontAwesomeIcon icon={faUser} className="text-gray-500" size="4x" />
                  </div>
                </div>
              ) : (
                <div className="hexagon-avatar hexagon-profile">
                  <Image
                    src={person.picture}
                    alt={`${person.name}'s profile`}
                    width={120}
                    height={120}
                    className="h-[116px] w-[116px]"
                    unoptimized
                    onError={() => setImageError(true)}
                    onLoad={() => setImageError(false)}
                  />
                </div>
              )}
            </div>

            <div className="flex-1">
              <h1 className="mb-2 text-3xl font-bold text-white-text">{person.name}</h1>

              {person.professionalHeadline && (
                <p className="mb-4 text-xl text-primary">{person.professionalHeadline}</p>
              )}

              {person.location && (
                <div className="mb-4 flex items-center text-light-text">
                  <FontAwesomeIcon icon={faMapMarkerAlt} className="mr-2 text-primary" />
                  <span>{person.location.name}</span>
                </div>
              )}

              {person.summaryOfBio && (
                <div className="leading-relaxed text-light-text">
                  <p>{person.summaryOfBio}</p>
                </div>
              )}
            </div>
          </div>
        </div>

        {strengths && strengths.length > 0 && (
          <div className="mb-8 rounded-lg bg-dark-card p-8">
            <h2 className="mb-6 flex items-center text-2xl font-semibold text-white-text">
              <FontAwesomeIcon icon={faStar} className="mr-3 text-primary" />
              Skills & Strengths ({strengths.length})
            </h2>

            <div className="mb-4 flex flex-wrap gap-2">
              {getSortedSkills().map((skill, index) => (
                <button
                  type="button"
                  key={skill.id || index}
                  onClick={() => handleSkillClick(skill.name)}
                  className={`inline-flex cursor-pointer items-center rounded-full px-3 py-1 text-sm font-medium transition-all hover:scale-105 hover:shadow-lg ${getSkillColorCompact(skill.proficiency)}`}
                  title={`Click to analyze ${skill.name}${skill.proficiency ? ` - ${skill.proficiency}` : ''}`}
                >
                  {skill.name}
                  {skill.proficiency && (
                    <span className="ml-1 text-xs opacity-75">{getSkillLevel(skill.proficiency)}</span>
                  )}
                </button>
              ))}
            </div>

            {strengths.length > 20 && (
              <button
                type="button"
                onClick={() => setShowAllSkills(!showAllSkills)}
                className="flex items-center rounded-lg border border-primary/30 bg-primary/10 px-3 py-2 text-sm font-medium text-primary transition-colors hover:border-green-400/50 hover:bg-primary/20 hover:text-green-400"
              >
                {showAllSkills
                  ? 'Show less...'
                  : `Show more... (${strengths.length - 20} additional skills)`}
              </button>
            )}
          </div>
        )}

        {experiences && experiences.length > 0 && (
          <div className="mb-6 rounded-lg bg-dark-card p-6">
            <h2 className="mb-4 flex items-center text-xl font-semibold text-white-text">
              <FontAwesomeIcon icon={faBriefcase} className="mr-2 text-primary" />
              Professional Experience
            </h2>

            <div className="space-y-4">
              {experiences.map((exp: Experience, index: number) => (
                <div key={exp.id || index} className="border-l-2 border-primary/30 pb-4 pl-4">
                  <h3 className="mb-1 text-lg font-semibold text-primary">{exp.name}</h3>

                  {exp.organizations && exp.organizations.length > 0 && (
                    <div className="mb-1 text-sm font-medium text-primary">
                      {exp.organizations.map((org: Organization) => org.name).join(', ')}
                    </div>
                  )}

                  <div className="text-xs text-light-text">
                    {formatDateRange(exp.fromMonth, exp.fromYear, exp.toMonth, exp.toYear)}
                  </div>
                </div>
              ))}
            </div>
          </div>
        )}

        {education && education.length > 0 && (
          <div className="rounded-lg bg-dark-card p-6">
            <h2 className="mb-4 flex items-center text-xl font-semibold text-white-text">
              <FontAwesomeIcon icon={faGraduationCap} className="mr-2 text-primary" />
              Education
            </h2>

            <div className="space-y-4">
              {education.map((edu: Education, index: number) => (
                <div key={edu.id || index} className="border-l-2 border-primary/30 pb-4 pl-4">
                  <h3 className="mb-1 text-lg font-semibold text-primary">{edu.name}</h3>

                  {edu.organizations && edu.organizations.length > 0 && (
                    <div className="mb-1 text-sm font-medium text-primary">
                      {edu.organizations.map((org: Organization) => org.name).join(', ')}
                    </div>
                  )}

                  <div className="text-xs text-light-text">
                    {formatDateRange(edu.fromMonth, edu.fromYear, edu.toMonth, edu.toYear)}
                  </div>
                </div>
              ))}
            </div>
          </div>
        )}
      </main>

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
