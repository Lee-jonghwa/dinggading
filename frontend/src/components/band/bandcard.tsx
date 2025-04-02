import React, { useState } from 'react';
import BandModal from './bandmodal';

interface BandCardProps {
  name: string;
  region: string;
}

const BandCard: React.FC<BandCardProps> = ({ name, region }) => {
  const [isOpen, setIsOpen] = useState(false);

  return (
    <>
      <div className="band-card-wrapper" onClick={() => setIsOpen(true)}>
        <div className="band-card">
          <h3>{name}</h3>
          <p>{region}</p>
        </div>
      </div>
      {isOpen && (
        <BandModal name={name} region={region} onClose={() => setIsOpen(false)} />
      )}
    </>
  );
};

export default BandCard;
