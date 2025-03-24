import React from "react";
import { useGLTF } from "@react-three/drei";

interface ModelProps {
  glbPath: string;
  position?: [number, number, number];
  rotation?: [number, number, number];
  scale?: number | [number, number, number];
}

export function Model({ glbPath, position = [0, 0, 0], rotation = [0, 0, 0], scale = 1 }: ModelProps) {
  const { nodes, materials } = useGLTF(glbPath);

  return (
    <group position={position} rotation={rotation} scale={scale} dispose={null}>
      {Object.keys(nodes).map((key) => {
        const node = nodes[key];
        if (!node.geometry) return null; // geometry가 없는 노드는 건너뛰기

        return (
          <mesh
            key={key}
            castShadow
            receiveShadow
            geometry={node.geometry}
            material={materials[node.material?.name] || undefined} // 매칭되는 material 적용
          />
        );
      })}
    </group>
  );
}

// 동적으로 GLB 파일을 미리 로드하는 함수
export function preloadGLB(glbPath: string) {
  useGLTF.preload(glbPath);
}
