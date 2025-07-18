import React, { useEffect, useRef } from 'react';
import { StreamManager } from 'openvidu-browser';

interface OvVideoProps {
  streamManager: StreamManager;
}

const OpenViduVideoComponent: React.FC<OvVideoProps> = ({ streamManager }) => {
  const videoRef = useRef<HTMLVideoElement>(null);
  
  useEffect(() => {
    if (streamManager && videoRef.current) {
      streamManager.addVideoElement(videoRef.current);
    }
  }, [streamManager]);

  return <video autoPlay={true} ref={videoRef} />;
};

export default OpenViduVideoComponent;