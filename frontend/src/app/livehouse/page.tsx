'use client';

import React from 'react';
import { useOpenVidu } from '@/hooks/useOpenVidu';
import JoinForm from '@/components/livehouse/JoinForm';
import SessionControls from '@/components/livehouse/SessionControls';
import VideoContainer from '@/components/livehouse/VideoContainer';

export default function VideoApp() {
  const {
    state,
    handleChangeSessionId,
    handleChangeUserName,
    handleMainVideoStream,
    joinSession,
    leaveSession,
    switchCamera
  } = useOpenVidu();

  const { 
    mySessionId, 
    myUserName, 
    session, 
    mainStreamManager, 
    publisher, 
    subscribers 
  } = state;

  return (
    <div className="container">
      {session === undefined ? (
        <JoinForm
          mySessionId={mySessionId}
          myUserName={myUserName}
          handleChangeSessionId={handleChangeSessionId}
          handleChangeUserName={handleChangeUserName}
          joinSession={joinSession}
        />
      ) : (
        <div id="session">
          <SessionControls
            sessionId={mySessionId}
            leaveSession={leaveSession}
            switchCamera={switchCamera}
          />
          <VideoContainer
            mainStreamManager={mainStreamManager}
            publisher={publisher}
            subscribers={subscribers}
            handleMainVideoStream={handleMainVideoStream}
          />
        </div>
      )}
    </div>
  );
}