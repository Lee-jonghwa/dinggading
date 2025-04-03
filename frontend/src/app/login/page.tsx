'use client';
import { NextPage } from 'next';

const Login: NextPage = () => {
  //const url = process.env.NEXT_PUBLIC_API_BASE_URL || 'https://localhost:8080';
  const googleLogin = () => {
    // 이 변수들은 실제로 사용되지 않으므로 제거해도 됩니다
    // const clientId = '748370350960-nmharc155g4t38lu25aa3vabncj4msgp.apps.googleusercontent.com';
    // const redirectUri = 'http://localhost:3000/login/oauth2/code/google';
    // const scope = 'profile email';
    
    // 여기가 중요합니다 - 문자열 제대로 정의
    const authUrl = `https://j12e107.p.ssafy.io/oauth2/authorization/google`;
    window.location.href = authUrl;
  };

  return (
    <div>
      <h1>Login</h1>
      <button onClick={googleLogin}>Google Login</button>
    </div>
  );
};

export default Login;