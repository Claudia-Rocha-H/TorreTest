# Torre Search & Skills Analysis Platform

A full-stack application that integrates with Torre.ai's API to provide advanced search capabilities and skill analysis insights, featuring compensation data and proficiency distribution analytics.

## 🚀 Live Demo

- **Frontend**: [https://torre-test1.vercel.app](https://torre-test1.vercel.app)
- **Backend API**: [https://torretest1.onrender.com](https://torretest1.onrender.com)

## 🏗️ Architecture

### Frontend (Next.js 15)
- **Framework**: Next.js 15.4.5 with App Router
- **Styling**: Tailwind CSS with custom design system
- **Icons**: FontAwesome for consistent iconography
- **Deployment**: Vercel with automatic deployments

### Backend (Spring Boot)
- **Framework**: Spring Boot 3.5.4 with Java 17
- **Architecture**: RESTful API with proxy pattern
- **Deployment**: Docker container on Render
- **Database**: In-memory processing (stateless)

## 🎯 Key Features

### 🔍 Advanced Search
- Real-time people search with Torre.ai integration
- Pagination with configurable result limits
- Error handling with user-friendly feedback

### 👤 Profile Analysis
- Detailed user profiles with skills and experience
- Interactive skill analysis modal
- Professional timeline visualization

### 📊 Skill Insights
- **Compensation Analysis**: Market salary insights and Torre.ai recommendations
- **Proficiency Distribution**: Skill level analytics across user base
- **Dynamic Data**: Real-time proficiency calculations
## 🛠️ Technical Implementation

### Frontend Architecture
- **Component-based**: Modular React components with clear separation of concerns
- **Type Safety**: Full TypeScript implementation with strict typing
- **State Management**: React hooks for local state, async operations
- **API Layer**: Centralized API utilities with error handling

### Backend Architecture
- **Proxy Pattern**: Secure API intermediary between frontend and Torre.ai
- **CORS Configuration**: Production-ready cross-origin resource sharing
- **Error Handling**: Comprehensive exception management
- **Health Checks**: Monitoring endpoints for deployment verification
