import React, { useEffect } from "react";
import { selectLocal } from "slices/scheduleCreateSlice";
import { useAppSelector } from "app/hooks";
import { Grid } from "@mui/material";
import CreateInfo from "features/schedule/create/CreateInfo";
import CreateSearch from "features/schedule/create/CreateSearch";

declare global {
  interface Window {
    kakao: any;
  }
}

const { kakao } = window;

function ScheduleCreatePage() {
  const local = useAppSelector(selectLocal);

  function setMap() {
    const container = document.getElementById("map");
    const options = {
      center: new kakao.maps.LatLng(33.450701, 126.570667),
      level: 5,
    };
    // 지도를 생성
    const map = new kakao.maps.Map(container, options);
    // map.addOverlayMapTypeId(kakao.maps.MapTypeId.TERRAIN);

    const geocoder = new kakao.maps.services.Geocoder();
    geocoder.addressSearch(local, function (result: any[], status: string) {
      // 정상적으로 검색이 완료됐으면

      if (status === kakao.maps.services.Status.OK) {
        const coords = new kakao.maps.LatLng(result[0].y, result[0].x);

        // 지도의 중심을 결과값으로 받은 위치로 이동시킵니다
        map.setCenter(coords);
      }
    });
  }

  useEffect(() => {
    setMap();
  }, [local]);

  return (
    <>
      <Grid container columns={6.5} style={{ width: "100%", height: "100%" }}>
        <Grid item xs={1.11} ml={0.5}>
          <CreateInfo />
        </Grid>
        <Grid item xs={4.3}>
          <div id="map" style={{ width: "100%", height: "100%" }}></div>
        </Grid>
        <Grid item xs={1.07}>
          <CreateSearch />
        </Grid>
      </Grid>
    </>
  );
}

export default ScheduleCreatePage;