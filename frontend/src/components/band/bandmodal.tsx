import React from 'react';

interface BandModalProps {
  name: string;
  region: string;
  onClose: () => void;
}

const BandModal: React.FC<BandModalProps> = ({ name, region, onClose }) => {
  return (
    <div
      style={{
        position: 'fixed',
        top: 0, left: 0,
        width: '100vw', height: '100vh',
        backgroundColor: 'rgba(0, 0, 0, 0.5)',
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        zIndex: 9999
      }}
      onClick={onClose}
    >
      <div
        style={{
          backgroundColor: 'white',
          padding: '24px',
          borderRadius: '8px',
          width: '360px',
          maxWidth: '90%',
        }}
        onClick={(e) => e.stopPropagation()}
      >
        <h2>{name}</h2>
        <p>{region}</p>
        <p>여기는 {name}의 상세 설명입니다.</p>
        <button onClick={onClose}>닫기</button>
      </div>
    </div>
  );
};

export default BandModal;
