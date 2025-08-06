# AI/LLM Prompts Documentation

This document contains the prompts used during the development.

## Project Setup and Initial Configuration


### **Request: Component extraction from HTML mockup**
Tool: GitHub Copilot (VS Code)
Model: GPT-4
Prompt: I need to replicate the Torre.ai website design in React/Next.js. Help me analyze this HTML and CSS structure I created and extract it into reusable React components following a screaming architecture pattern. Create separate components for the header navigation, search functionality, and main layout. Use TypeScript and ensure proper component organization in features/ and components/ folders.

I pasted my HTML - CSS mock here

## Styling and Design Issues

### **Request: Tailwind CSS configuration for custom colors**
Tool: GitHub Copilot (VS Code)
Model: GPT-4
Prompt: I need to configure Tailwind CSS with a custom color palette for Torre.ai brand colors: dark background (#27292d), primary color (#cddc39), light text (#dadadb), and other specific colors. Set up the tailwind.config.ts file with these custom colors and ensure they can be used as classes like bg-dark-bg, text-primary-color, etc.

### **Debug: Tailwind custom colors not rendering**
Tool: GitHub Copilot (VS Code)
Model: GPT-4
Prompt: I have defined custom colors in my tailwind.config.ts file and I'm using them in my components with classes like "bg-dark-bg text-light-text", but the colors are not applying - everything appears white. The classes appear correctly in the browser's HTML but the CSS colors don't load. What could be causing this issue and how can I fix it? I'm using Tailwind CSS v4.

### **Solution: Migrate to Tailwind CSS v4 configuration**
Tool: GitHub Copilot (VS Code)
Model: GPT-4
Prompt: I discovered that Tailwind CSS v4 has deprecated tailwind.config.js in favor of CSS-based configuration using @theme directive. Help me migrate my custom colors and configuration from tailwind.config.ts to the new CSS-based approach. Create a proper @theme block with all my custom Torre.ai colors and organize the configuration following Tailwind v4 best practices.

## Code Quality and Best Practices

### **Request: Professional code commenting standards**
Tool: GitHub Copilot (VS Code)
Model: GPT-4
Prompt: Help me add professional, minimal comments to my React components following industry best practices. The comments should be concise, explain the purpose of each component, and avoid over-commenting  code.

### **Request: Proper CSS architecture organization**
Tool: GitHub Copilot (VS Code)
Model: GPT-4
Prompt: I want to improve my CSS organization by separating Tailwind configuration from global styles. Help me create a dedicated tailwind.css file for Tailwind configuration and keep globals.css only for application-specific global styles. This should follow CSS architecture best practices and maintain clear separation of concerns.

## UI/UX Implementation

### **Request: FontAwesome icon integration and sizing**
Tool: GitHub Copilot (VS Code)
Model: GPT-4
Prompt: I need to integrate FontAwesome icons in my React components with consistent sizing. The icons should be properly sized and not interfere with the layout. Help me set up FontAwesome correctly and add CSS rules to control icon dimensions consistently across the application.


### **Request: English localization of interface**
Tool: GitHub Copilot (VS Code)
Model: GPT-4
Prompt: Convert all the Spanish text in my Torre.ai interface replica to English while maintaining the same layout and functionality. Update navigation items, button labels, and all user-facing text to proper English equivalents that make sense in the context of a job platform.

## Technical Debugging


### **Debug: CSS import and file structure issues**
Tool: GitHub Copilot (VS Code)
Model: GPT-4
Prompt: I'm having issues with CSS imports in my Next.js project. My globals.css file imports a custom tailwind.css file but the styles aren't loading properly. Help me debug the import chain and ensure the CSS files are structured correctly for Next.js to process them in the right order.

### **Debug: API Request Inspection with Burp Suite for User Profiles**

Tool: Burp Suite (Manual Inspection)
Model: Gemini
Prompt: To integrate the user profile visualization functionality, the Torre.ai API endpoint is GET https://torre.ai/api/genome/bios/{username}. However, the Swagger documentation for this GET endpoint did not provide a clear definition of the request body nor exhaustive details about the complete JSON response structure or necessary headers.
I need your help to accurately understand and replicate the request and response of this endpoint. I have intercepted live traffic using Burp Suite and have the raw HTTP request and a screenshot showing multiple sub-requests made after the initial request to the base profile.

Based on this information (which I previously provided, including the raw HTTP request and the Burp Suite image), help me identify:

All critical HTTP headers that the browser sends and that my Spring Boot backend proxy must replicate.
The exact JSON response structure of the base endpoint (/bios/{username}) and relevant sub-endpoints (like /strengths-skills), in order to build the correct Java DTOs in my backend and effectively aggregate the profile information.
How to handle the fragmented nature of profile information obtained through multiple calls to Torre.ai's sub-endpoints.


## Project Architecture

### **Request: Clean component architecture**
Tool: GitHub Copilot (VS Code)
Model: GPT-4
Prompt: Help me organize my React components following screaming architecture principles. Create a clear folder structure with features/ for feature-specific components and components/ for shared components. Ensure each component has a single responsibility and follows React best practices for a professional codebase.

### **Request: TypeScript configuration optimization**
Tool: GitHub Copilot (VS Code)
Model: GPT-4
Prompt: Review and optimize my TypeScript configuration for a Next.js project. Ensure proper type checking, imports, and that all components are properly typed.

## Visual Design and UI Consistency

### **Request: Consistent card sizing in grid layouts**
Tool: GitHub Copilot (VS Code)
Model: GPT-4
Prompt: I have a grid layout displaying user profile cards, but some cards appear narrower than others, creating visual inconsistency. Help me implement uniform sizing for all cards using Tailwind CSS. Ensure all cards have the same width and minimum height, use flexbox for proper content alignment, and add text truncation for long content to maintain consistent appearance across different screen sizes.


### **Request: Color system consistency audit**
Tool: GitHub Copilot (VS Code)
Model: GPT-4
Prompt: I need to audit my entire frontend codebase for color reference consistency. Some components are using incorrect color names that don't exist in my Tailwind configuration (like 'torre-primary' instead of 'primary'). Help me systematically identify and correct all color references throughout the project to ensure they match the colors defined in my CSS configuration. Use regex searches to find inconsistencies and provide a comprehensive fix.


## API Integration and Data Management

### **Request: API proxy implementation for external services**
Tool: GitHub Copilot (VS Code)
Model: GPT-4
Prompt: Create API routes in Next.js to proxy requests to Torre.ai's external API. This is needed to handle CORS issues and provide a clean interface for the frontend. Implement proper error handling, request validation, and response transformation. Ensure the proxy maintains the same endpoint structure while adding necessary headers and handling authentication if required.


### **Request: Data aggregation for profile information**
Tool: GitHub Copilot (VS Code)
Model: GPT-4
Prompt: Torre.ai's profile data comes from multiple API endpoints that need to be aggregated into a single profile view. Help me create functions that fetch data from multiple endpoints (/bios/{username}, /strengths-skills, etc.), combine the responses efficiently, handle partial failures gracefully, and provide loading states for each data section.

## CSS Architecture and Styling Systems

### **Request: CSS duplications cleanup and organization**
Tool: GitHub Copilot (VS Code)
Model: GPT-4
Prompt: My CSS file has become disorganized with inconsistent styling. Help me clean up the CSS by removing duplications, organizing styles into logical sections with comments, ensuring consistent naming conventions, and establishing a proper hierarchy for component-specific styles versus utility classes.


## Performance and Build Optimization

### **Request: Build error troubleshooting for Next.js projects**
Tool: GitHub Copilot (VS Code)
Model: GPT-4
Prompt: I'm encountering build errors in my Next.js project related to file permissions and TypeScript compilation. Help me diagnose and fix common build issues including cleaning build artifacts, resolving dependency conflicts, fixing TypeScript errors, and optimizing the build process for production deployment.



## Advanced Development Techniques

### **Request: Professional documentation cleanup and standardization**
Tool: GitHub Copilot (VS Code)
Model: GPT-4
Prompt: Convert all Spanish comments and documentation to professional English following industry standards. Update code comments, variable names, function descriptions, and README files to use clear, concise English. Ensure consistency in documentation style, proper technical terminology, and maintain the same level of detail while improving readability for international developers.


### **Request: Responsive grid system with consistent card layouts**
Tool: GitHub Copilot (VS Code)
Model: GPT-4
Prompt: Design a responsive grid system that maintains consistent card sizing across different screen sizes. Implement a grid that adapts from 1 column on mobile to 3 columns on desktop, ensures all cards have uniform dimensions regardless of content length, uses proper flexbox techniques for content alignment, and handles text overflow  with ellipsis or line clamping.



## Code Quality and Maintenance

### **Request: Systematic code refactoring for consistency**
Tool: GitHub Copilot (VS Code)
Model: GPT-4
Prompt: Perform a systematic refactoring of my React codebase to improve consistency and maintainability. Focus on extracting reusable components, standardizing naming conventions, removing code duplication, implementing proper error boundaries, and ensuring all components follow React best practices. Provide a step-by-step approach for large-scale refactoring.

### **Request: CSS methodology implementation (BEM/SMACSS)**
Tool: GitHub Copilot (VS Code)
Model: GPT-4
Prompt: Implement a structured CSS methodology to improve maintainability and prevent conflicts. Organize CSS classes using BEM (Block Element Modifier) naming convention or SMACSS principles, create clear separation between layout, module, and state styles, and establish guidelines for component-specific styling that scales well with team development.

### **Request: Performance monitoring and optimization strategies**
Tool: GitHub Copilot (VS Code)
Model: GPT-4
Prompt: Implement performance monitoring and optimization strategies for a Next.js application. Include techniques for measuring Core Web Vitals, optimizing images and assets, implementing proper caching strategies, reducing bundle sizes, and monitoring runtime performance. Focus on practical implementation.

## Testing and Quality Assurance


### **Request: Accessibility audit and implementation**
Tool: GitHub Copilot (VS Code)
Model: GPT-4
Prompt: Conduct a comprehensive accessibility audit of my React application and implement necessary improvements. Focus on keyboard navigation, screen reader compatibility, color contrast ratios, ARIA labels, semantic HTML usage, and focus management. Provide specific code examples and tools for ongoing accessibility testing.

