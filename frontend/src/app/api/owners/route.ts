// app/api/owners/route.ts
import { OwnerApi } from '@generated/api';
import { Configuration } from '@generated/configuration';
import { NextResponse } from 'next/server';
// import { Configuration, OwnerApi } from '@generated';

export async function POST(request: Request) {
  const data = await request.json();
  
  const configuration = new Configuration({
    basePath: process.env.API_BASE_URL,
  });
  
  const ownerApi = new OwnerApi(configuration);
  
  try {
    const response = await ownerApi.addOwner({
      ownerFields: data
    });
    
    return NextResponse.json(response.data);
  } catch (error) {
    return NextResponse.json({ error: 'Failed to add owner' }, { status: 500 });
  }
}