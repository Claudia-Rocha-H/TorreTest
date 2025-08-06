import type { NextConfig } from "next";

const nextConfig: NextConfig = {
  images: {
    domains: [
      'torre-profile-pictures.s3.us-west-2.amazonaws.com',
      'cdn.torre.ai',
      // Agrega otros dominios según sea necesario
    ],
    remotePatterns: [
      {
        protocol: 'https',
        hostname: '**.amazonaws.com',
      },
      {
        protocol: 'https',
        hostname: 'torre.ai',
      },
      {
        protocol: 'https',
        hostname: '**.torre.ai',
      },
    ],
  },
};

export default nextConfig;
