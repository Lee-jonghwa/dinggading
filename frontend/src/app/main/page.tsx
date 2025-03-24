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
        <Canvas style={{width : "700px" , height : "700px"}} camera={{position : [0, 0, 5]}}>
          <ambientLight color={"#FFFFFF"} intensity={2.5} />
          <directionalLight position={[10, 11.5, 20]} color={"#FFFFFF"} intensity={5} />
          {/* <Model glbPath="/models/Platinum.glb" scale={2} /> */}
          <Model glbPath="/models/Iron.glb" rotation={[2, 0, 3]} scale={2} />
          {/* <Model glbPath="/models/Platinum.glb" position={[0, 0, -1]} rotation={[2, Math.PI, 0]} scale={2} /> */}
        </Canvas>
      </div>


      <div className="count border-1">
        <p>count : {count}</p>
        <button onClick={increment}>+</button>
        <button onClick={decrement}>-</button>
        <button onClick={reset}>RESET</button>
      </div>
    </div>
  )
}

export default Main