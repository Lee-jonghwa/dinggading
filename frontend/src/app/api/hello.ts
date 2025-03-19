// pages/api/hello.ts
import type { NextApiRequest, NextApiResponse } from 'next';

export default async function handler(
    req: NextApiRequest,
    res: NextApiResponse
  ) {
    try {
      // 서버 측에서 Docker 네트워크 이름으로 요청
      const apiResponse = await fetch('http://spring:8080/hello');
      const data = await apiResponse.text();
      res.status(200).send(data);
    } catch (error) {
      console.error('API 호출 오류:', error);
      res.status(500).json({ error: 'API 호출 중 오류가 발생했습니다.' });
    }
  }