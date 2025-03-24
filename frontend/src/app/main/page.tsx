'use client'
import { NextPage } from "next"
import Button from "@/components/button"
import { useCountStore } from "@/store/count"

const Main: NextPage = () => {

  const { count , increment, decrement, reset } = useCountStore()

  return (
    <div> 
      <h1> Main </h1>
      <Button></Button>
      <div className="count">
        <p>count : {count}</p>
        <button onClick={increment}>+</button>
        <button onClick={decrement}>-</button>
        <button onClick={reset}>RESET</button>
      </div>
    </div>
  )
}

export default Main