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

## Project Architecture

### **Request: Clean component architecture**
Tool: GitHub Copilot (VS Code)
Model: GPT-4
Prompt: Help me organize my React components following screaming architecture principles. Create a clear folder structure with features/ for feature-specific components and components/ for shared components. Ensure each component has a single responsibility and follows React best practices for a professional codebase.

### **Request: TypeScript configuration optimization**
Tool: GitHub Copilot (VS Code)
Model: GPT-4
Prompt: Review and optimize my TypeScript configuration for a Next.js project. Ensure proper type checking, imports, and that all components are properly typed.
