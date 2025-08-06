'use client';

import React, { useState, useEffect } from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { 
  faTimes, 
  faChartBar, 
  faDollarSign, 
  faSpinner,
  faExclamationTriangle 
} from '@fortawesome/free-solid-svg-icons';
import { 
  analyzeSkillCompensation, 
  getSkillProficiencyDistribution
} from '../lib/api';
import type {
  SkillInsightData,
  ProficiencyLevel,
  SkillAnalysisPanelProps
} from '../types';

/**
 * Interactive panel for detailed skill analysis including compensation insights
 * and proficiency distribution across Torre.ai user base.
 */
export default function SkillAnalysisPanel({ 
  skillName, 
  userProficiency, 
  onClose, 
  isVisible 
}: SkillAnalysisPanelProps) {
  const [analysisData, setAnalysisData] = useState<SkillInsightData>({
    compensation: undefined,
    distribution: undefined,
    isLoading: true,
    error: null
  });

  /**
   * Load skill analysis data when panel becomes visible
   */
  useEffect(() => {
    if (!isVisible) return;

    const loadAnalysisData = async () => {
      setAnalysisData((prev: SkillInsightData) => ({ ...prev, isLoading: true, error: null }));

      try {
        // Load compensation and distribution data in parallel
        const [compensationData, distributionData] = await Promise.all([
          analyzeSkillCompensation(skillName, userProficiency || undefined).catch((err: Error) => {
            console.warn('Compensation analysis failed:', err);
            return undefined;
          }),
          getSkillProficiencyDistribution(skillName).catch((err: Error) => {
            console.warn('Distribution analysis failed:', err);
            return undefined;
          })
        ]);

        setAnalysisData((prev: SkillInsightData) => ({
          ...prev,
          compensation: compensationData,
          distribution: distributionData?.distribution,
          isLoading: false
        }));

      } catch (error) {
        console.error('Error loading skill analysis:', error);
        setAnalysisData((prev: SkillInsightData) => ({
          ...prev,
          error: error instanceof Error ? error.message : 'Failed to load analysis',
          isLoading: false
        }));
      }
    };

    loadAnalysisData();
  }, [isVisible, skillName, userProficiency]);

  /**
   * Format currency values for display
   */
  const formatCurrency = (value: number, currency: string) => {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: currency,
      maximumFractionDigits: 0
    }).format(value);
  };

  /**
   * Get color scheme for proficiency levels
   */
  const getProficiencyColor = (level: string) => {
    switch (level) {
      case 'expert':
      case 'master':
        return 'bg-green-500/20 text-green-400';
      case 'advanced':
      case 'proficient':
        return 'bg-blue-500/20 text-blue-400';
      case 'intermediate':
      case 'novice':
        return 'bg-yellow-500/20 text-yellow-400';
      case 'beginner':
      case 'no-experience-interested':
        return 'bg-purple-500/20 text-purple-400';
      default:
        return 'bg-gray-500/20 text-gray-400';
    }
  };

  /**
   * Format proficiency level names for display
   */
  const formatProficiencyLevel = (level: string) => {
    switch (level) {
      case 'no-experience-interested':
        return 'Interested';
      case 'unknown':
        return 'Unspecified';
      default:
        return level.charAt(0).toUpperCase() + level.slice(1);
    }
  };

  if (!isVisible) return null;

  return (
    <div className="fixed inset-0 bg-black/60 backdrop-blur-sm z-50 flex items-center justify-center p-4">
      <div className="bg-dark-card rounded-xl max-w-4xl w-full max-h-[90vh] overflow-y-auto border border-border">
        {/* Header */}
        <div className="flex items-center justify-between p-6 border-b border-border">
          <div>
            <h2 className="text-2xl font-bold text-white-text flex items-center">
              <FontAwesomeIcon icon={faChartBar} className="mr-3 text-primary" />
              Skill Analysis: {skillName}
            </h2>
            {userProficiency && (
              <p className="text-light-text mt-1">
                Your level: <span className="text-primary font-medium">{userProficiency}</span>
              </p>
            )}
          </div>
          <button
            onClick={onClose}
            className="text-light-text hover:text-white-text p-2 hover:bg-border/20 rounded-lg transition-colors"
          >
            <FontAwesomeIcon icon={faTimes} size="lg" />
          </button>
        </div>

        {/* Content */}
        <div className="p-6">
          {analysisData.isLoading ? (
            <div className="flex items-center justify-center py-12">
              <div className="text-center">
                <FontAwesomeIcon icon={faSpinner} size="2x" className="text-primary animate-spin mb-4" />
                <p className="text-light-text">Analyzing skill data...</p>
              </div>
            </div>
          ) : analysisData.error ? (
            <div className="bg-red-500/20 border border-red-500/30 rounded-lg p-6 text-center">
              <FontAwesomeIcon icon={faExclamationTriangle} className="text-red-400 text-2xl mb-3" />
              <h3 className="text-xl font-semibold text-red-400 mb-2">Analysis Unavailable</h3>
              <p className="text-red-300">{analysisData.error}</p>
            </div>
          ) : (
            <div className="space-y-8">
              {/* Compensation Analysis */}
              {analysisData.compensation && (
                <div className="bg-dark-bg/50 rounded-lg p-6">
                  <h3 className="text-xl font-semibold text-white-text mb-4 flex items-center">
                    <FontAwesomeIcon icon={faDollarSign} className="mr-2 text-primary" />
                    Compensation Insights
                  </h3>
                  
                  <div className="grid md:grid-cols-2 gap-6">
                    {analysisData.compensation.medianCompensation > 0 && (
                      <div className="bg-primary/10 border border-primary/30 rounded-lg p-4">
                        <h4 className="text-primary font-semibold mb-2">
                          Suggested Compensation
                          <span className="text-xs text-gray-400 ml-2 font-normal">
                            (Torre.ai recommendation)
                          </span>
                        </h4>
                        <div className="text-2xl font-bold text-white-text">
                          {formatCurrency(
                            analysisData.compensation.medianCompensation,
                            analysisData.compensation.currency
                          )}
                        </div>
                        <p className="text-light-text text-sm mt-1">
                          {analysisData.compensation.periodicity}
                        </p>
                      </div>
                    )}
                    
                    {analysisData.compensation.averageCompensation > 0 && (
                      <div className="bg-blue-500/10 border border-blue-500/30 rounded-lg p-4">
                        <h4 className="text-blue-400 font-semibold mb-2">
                          Average Compensation
                          <span className="text-xs text-gray-400 ml-2 font-normal">
                            (Market average)
                          </span>
                        </h4>
                        <div className="text-2xl font-bold text-white-text">
                          {formatCurrency(
                            analysisData.compensation.averageCompensation,
                            analysisData.compensation.currency
                          )}
                        </div>
                        <p className="text-light-text text-sm mt-1">
                          {analysisData.compensation.periodicity}
                        </p>
                      </div>
                    )}
                  </div>
                  
                  <div className="text-light-text text-sm mt-4 space-y-2">
                    <p>
                      * Based on Torre.ai data from {analysisData.compensation.dataPoints?.toLocaleString()} profiles
                    </p>
                    <p className="text-gray-400 text-xs">
                      <strong>Suggested:</strong> Torre.ai&apos;s recommended compensation based on market analysis and experience level.
                      <br />
                      <strong>Average:</strong> Mathematical average of all reported compensations for this skill.
                    </p>
                  </div>
                </div>
              )}

              {/* Proficiency Distribution */}
              {analysisData.distribution && analysisData.distribution.length > 0 && (
                <div className="bg-dark-bg/50 rounded-lg p-6">
                  <h3 className="text-xl font-semibold text-white-text mb-4 flex items-center">
                    <FontAwesomeIcon icon={faChartBar} className="mr-2 text-primary" />
                    Proficiency Distribution
                  </h3>
                  
                  <div className="space-y-3">
                    {analysisData.distribution
                      .filter((item: ProficiencyLevel) => item.count > 0)
                      .sort((a: ProficiencyLevel, b: ProficiencyLevel) => b.percentage - a.percentage)
                      .map((item: ProficiencyLevel) => (
                        <div key={item.level} className="flex items-center justify-between p-3 bg-border/10 rounded-lg">
                          <div className="flex items-center space-x-3">
                            <span className={`px-3 py-1 rounded-full text-sm font-medium ${getProficiencyColor(item.level)}`}>
                              {formatProficiencyLevel(item.level)}
                            </span>
                            <span className="text-light-text text-sm">
                              {item.count.toLocaleString()} users
                            </span>
                          </div>
                          
                          <div className="flex items-center space-x-3">
                            <div className="w-32 bg-border/20 rounded-full h-2">
                              <div 
                                className="bg-primary h-2 rounded-full transition-all duration-300"
                                style={{ width: `${Math.max(item.percentage, 2)}%` }}
                              />
                            </div>
                            <span className="text-white-text font-medium min-w-[3rem] text-right">
                              {item.percentage.toFixed(1)}%
                            </span>
                          </div>
                        </div>
                      ))}
                  </div>
                  
                  <p className="text-light-text text-sm mt-4">
                    * Showing distribution across {analysisData.distribution?.reduce((sum: number, item: ProficiencyLevel) => sum + item.count, 0).toLocaleString()} Torre.ai profiles with this skill
                  </p>
                </div>
              )}

              {/* No data available */}
              {!analysisData.compensation && (!analysisData.distribution || analysisData.distribution.length === 0) && (
                <div className="bg-border/10 rounded-lg p-8 text-center">
                  <FontAwesomeIcon icon={faExclamationTriangle} className="text-light-text text-3xl mb-3" />
                  <h3 className="text-lg font-semibold text-light-text mb-2">No Analysis Data Available</h3>
                  <p className="text-light-text">
                    Insufficient data to provide insights for this skill at the moment.
                  </p>
                </div>
              )}
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
