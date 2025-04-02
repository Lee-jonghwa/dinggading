import React, { useState } from 'react';

const cities = [
  "서울특별시", "부산광역시", "대구광역시",
  "인천광역시", "광주광역시", "대전광역시",
  "울산광역시", "세종특별자치시", "제주특별자치도"
];

interface Props {
  onSelect: (city: string) => void;
  selectedCity: string;
}

const CitySelector: React.FC<Props> = ({ onSelect, selectedCity }) => {
  const [open, setOpen] = useState(false);

  const handleSelect = (city: string) => {
    onSelect(city);
    setOpen(false);
  };

  return (
    <div className="relative inline-block z-10">
      <button
        className="bg-transparent border-2 border-white/20 text-black text-lg font-medium px-6 py-3 h-auto hover:bg-white/10 rounded"
        onClick={() => setOpen(!open)}
      >
        {selectedCity || "지역 선택"}
      </button>

      {open && (
        <div className="absolute mt-2 w-[300px] bg-white rounded-md shadow-lg p-4 grid grid-cols-3 gap-4">
          {cities.map((city) => (
            <button
              key={city}
              onClick={() => handleSelect(city)}
              className={`text-sm text-gray-800 hover:text-blue-500 ${selectedCity === city ? 'font-bold text-blue-600' : ''}`}
            >
              {city}
            </button>
          ))}
        </div>
      )}
    </div>
  );
};

export default CitySelector;
