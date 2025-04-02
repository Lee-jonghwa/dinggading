import type { NextConfig } from "next";

const nextConfig: NextConfig = {
  output: "standalone",
  images : {
    unoptimized : true
  }, 
};

export default nextConfig;


// import type { NextConfig } from "next";

// const config = {
//   output: "standalone",
// };

// module.exports = config

// const nextConfig: NextConfig = config

// export default nextConfig;
