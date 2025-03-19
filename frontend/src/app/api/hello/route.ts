import { NextResponse } from 'next/server';
//src/app/api/hello/route.ts
export async function GET() {
  try {
    const apiUrl = process.env.API_URL;
    // 외부 서버로 GET 요청 보내기
    console.log(`http://${apiUrl}:8080/hello`);
    const apiResponse = await fetch(`http://${apiUrl}:8080/hello`);
    
    // 외부 서버에서 받은 응답 처리
    if (!apiResponse.ok) {
      throw new Error('외부 서버 응답이 실패했습니다.');
    }

    const data = await apiResponse.text();

    // 응답 반환
    return NextResponse.json(data);
  } catch (error) {
    console.error('외부 서버 요청 오류:', error);
    return NextResponse.json({ error: '외부 서버 요청 실패' }, { status: 500 });
  }
}
