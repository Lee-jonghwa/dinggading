"use client";
import { useState } from 'react';
import type { NextPage } from 'next';

const Home: NextPage = () => {
  const [response, setResponse] = useState<string>('');
  const [loading, setLoading] = useState<boolean>(false);
  const mode = process.env.NEXT_PUBLIC_ENV_MODE;
  const url = process.env.NEXT_PUBLIC_API_BASE_URL;
  console.log("page.tsx", mode);
  console.log("page.tsx", url);



  // /src/app/page.tsx

  const testApi = async () => {
    setLoading(true);
    try {
      // 프록시 API 라우트 호출
      const res = await fetch('/api/hello');

      // 나중에 json으로 변경 res.json()
      const data = await res.text();
      setResponse(data);
    } catch (error) {
      console.error('API 호출 오류:', error);
      setResponse('API 호출 중 오류가 발생했습니다.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{ padding: '20px' }}>
      <h1>API 테스트</h1>

      <h1>현재 환경 {mode}</h1>
      <button 
        onClick={testApi}
        disabled={loading}
      >
        {loading ? '로딩 중...' : 'API 호출하기 - 눌러주세요'}
      </button>
      {response && (
        <div style={{ marginTop: '20px' }}>
          <h2>응답:</h2>
          <pre>{response}</pre>
        </div>
      )}
    </div>
  );
};

export default Home;