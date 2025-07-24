import React, { useEffect } from 'react';

const Location = ({ lat, lng }) => {
  useEffect(() => {
    console.log(lat);
    console.log(lng);

    var container = document.getElementById('map');
    var options = {
      //지도의 중간 위치를 가져옴
      center: new window.kakao.maps.LatLng(lat, lng),
      //우리에게 보이는 지도 크기 => 숫자가 클 수록 멀리서 보는 st
      level: 3,
    };

    var map = new window.kakao.maps.Map(container, options);
    var markerPosition = new window.kakao.maps.LatLng(lat, lng);
    var marker = new window.kakao.maps.Marker({
      position: markerPosition,
    });
    marker.setMap(map);
  }, []);

  return (
    <div
      className="map"
      id="map"
      style={{
        width: '100%',
        maxWidth: '500px',
        height: '350px',
        border: '1px solid #ddd',
        borderRadius: '12px',
        boxShadow: '0 4px 12px rgba(0, 0, 0, 0.1)',
        backgroundColor: '#fff',
        overflow: 'hidden',
        margin: '0 auto', // 가운데 정렬용
      }}
    ></div>
  );
};

export default Location;
