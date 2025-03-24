'use client'
import { NextPage } from "next"
import Button from "@/components/button"
import { useCountStore } from "@/store/count"
import { Canvas } from "@react-three/fiber"
import { Model } from "@/components/glbmodels"

const Main: NextPage = () => {

  const { count , increment, decrement, reset } = useCountStore()

  return (
    <div> 
      <h1> Main </h1>
      <Button></Button>
      <div className="GLBModel">
        <Canvas camera={{position : [0, 0, 5]}}>
          <ambientLight color={"#FFFFFF"} intensity={2.5} />
          <directionalLight position={[1, 1, 1]} color={"#FFFFFF"} intensity={5} />
          {/* <Model glbPath="/models/Iron.glb" scale={2}/> */}
          {/* <Model glbPath="/models/Bronze.glb" scale={2}/> */}
          <Model glbPath="/models/Silver.glb" position={[0, 0, 2]} rotation={[0, Math.PI, 0]} scale={4} />
          {/* <Model glbPath="/models/Gold.glb" scale={2}/> */}
        </Canvas>
      </div>


      <div className="count border-1">
        <p> store 테스트 코드  </p>
        <p>count : {count}</p>
        <div onClick={increment}>+</div> 
        <div onClick={decrement}>-</div> 
        <div onClick={reset}>RESET</div> 
      </div>
    </div>
  )
}

export default Main