import React, { Suspense } from 'react'
import { Canvas } from '@react-three/fiber'
import { OrbitControls } from '@react-three/drei'
import { SilverGuitar } from '../Guitar/SG_transfer'

export default function SilverGuitarScene() {
  return (
    <div style={{ width: '100vw', height: '100vh' }}>
      <Canvas 
        camera={{ 
          position: [0, 0, 5], 
          fov: 45 
        }}
        style={{ background: '#22252d' }}
      >        
        {/* 방향성 조명 */}
        <directionalLight position={[0, 0, 60]} intensity={10}/>
        <directionalLight position={[-10, 0, 0]} intensity={3}/>
        <directionalLight position={[10, 0, 0]} intensity={3}/>
        <directionalLight position={[0, 10, 0]} intensity={3}/>
        <directionalLight position={[0, -10, 0]} intensity={3}/>

        {/* 모델 로딩 중 대기 */}
        <Suspense fallback={null}>
          <SilverGuitar position={[0, 0, 0]} />
        </Suspense>

        {/* 모델 컨트롤 */}
        <OrbitControls />
      </Canvas>
    </div>
  )
}